package travelguide;

import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class NewsFetcher {
    private static final String FALLBACK_KEY = "f28a087092a44e79a4269b8b5509326f";

    // Public: fetch with default key and no category (all news)
    public static java.util.List<NewsItem> fetchNewsForState(String stateName) throws Exception {
        String key = System.getProperty("NEWSAPI_KEY", FALLBACK_KEY);
        return fetchNewsForState(stateName, "All News", key);
    }

    // Public: fetch with explicit category (e.g. "All News", "Local News", "Events", "Travel Updates", "Safety Alerts")
    public static java.util.List<NewsItem> fetchNewsForState(String stateName, String category) throws Exception {
        String key = System.getProperty("NEWSAPI_KEY", FALLBACK_KEY);
        return fetchNewsForState(stateName, category, key);
    }

    // Internal: build query according to category and call NewsAPI
    public static java.util.List<NewsItem> fetchNewsForState(String stateName, String category, String apiKey) throws Exception {
        if (stateName == null || stateName.isBlank()) return Collections.emptyList();

        StringBuilder q = new StringBuilder();
        q.append(stateName);

        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("All News")) {
            switch (category) {
                case "Travel Updates":
                    q.append(" (travel OR tourism OR transportation OR metro OR flight)");
                    break;
                case "Events":
                    q.append(" (event OR festival OR concert OR exhibition OR fair)");
                    break;
                case "Local News":
                    q.append(" (local OR community OR municipal OR city)");
                    break;
                case "Safety Alerts":
                    q.append(" (alert OR advisory OR safety OR warning OR incident)");
                    break;
                default:
                    q.append(" ").append(category);
            }
        }

        String encodedQ = URLEncoder.encode(q.toString(), "UTF-8");
        String url = "https://newsapi.org/v2/everything?q=" + encodedQ + "&language=en&sortBy=publishedAt&pageSize=30&apiKey=" + URLEncoder.encode(apiKey, "UTF-8");

        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setRequestProperty("User-Agent", "travelguide-app/1.0");
        c.setConnectTimeout(15000);
        c.setReadTimeout(20000);

        int code = c.getResponseCode();
        InputStream is = (code >= 200 && code < 400) ? c.getInputStream() : c.getErrorStream();
        String json;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            json = br.lines().collect(Collectors.joining("\n"));
        }

        JSONObject root = new JSONObject(json);
        if (!"ok".equalsIgnoreCase(root.optString("status"))) {
            String msg = root.optString("message", "News API error");
            throw new IOException("NewsAPI error: " + msg);
        }

        JSONArray articles = root.optJSONArray("articles");
        if (articles == null) return Collections.emptyList();

        java.util.List<NewsItem> out = new ArrayList<>();
        for (int i = 0; i < articles.length(); i++) {
            JSONObject a = articles.getJSONObject(i);
            String title = a.optString("title", "");
            String desc = a.optString("description", "");
            String source = a.optJSONObject("source").optString("name", "");
            String urlToArticle = a.optString("url", "");
            String publishedAt = a.optString("publishedAt", "");
            out.add(new NewsItem(title, desc, source, urlToArticle, publishedAt));
        }
        return out;
    }

    public static class NewsItem {
        public final String title;
        public final String description;
        public final String source;
        public final String url;
        public final String publishedAt;
        public NewsItem(String t, String d, String s, String u, String p) { title=t; description=d; source=s; url=u; publishedAt=p; }
        @Override public String toString() { return title + " — " + source; }
    }
}