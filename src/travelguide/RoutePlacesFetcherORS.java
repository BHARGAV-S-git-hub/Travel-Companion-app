package travelguide;

import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Minimal helper: geocode via ORS (preferred) or Nominatim fallback, route via ORS, POIs via ORS /pois POST.
 */
public class RoutePlacesFetcherORS {

    public static class Place {
        public final String name;
        public final String kind;
        public final double lat, lon;
        public Place(String name, String kind, double lat, double lon) { this.name = name; this.kind = kind; this.lat = lat; this.lon = lon; }
        @Override public String toString() { return name + " (" + kind + ")"; }
    }

    // main entry
    public static List<Place> fetchPlacesAlongRoute(String origin, String destination, String orsApiKey, int sampleStep, int bufferMeters, int maxPlaces) throws Exception {
        double[] o = geocodePreferORS(origin, orsApiKey);
        double[] d = geocodePreferORS(destination, orsApiKey);
        if (o == null || d == null) return Collections.emptyList();

        // ORS directions (geojson coords: [lon,lat])
        String orsDirUrl = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";
        JSONObject body = new JSONObject();
        JSONArray coords = new JSONArray();
        coords.put(new JSONArray(new double[]{ o[1], o[0] }));
        coords.put(new JSONArray(new double[]{ d[1], d[0] }));
        body.put("coordinates", coords);

        String dirs = httpPostJson(orsDirUrl, body.toString(), Map.of("Authorization", orsApiKey, "Content-Type", "application/json"));
        JSONObject droot = new JSONObject(dirs);
        JSONArray features = droot.optJSONArray("features");
        if (features == null || features.length() == 0) return Collections.emptyList();
        JSONArray routeCoords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

        // build linestring coordinates (lon,lat)
        JSONArray lines = new JSONArray();
        for (int i = 0; i < routeCoords.length(); i++) {
            JSONArray p = routeCoords.getJSONArray(i);
            JSONArray q = new JSONArray();
            q.put(p.getDouble(0)); q.put(p.getDouble(1));
            lines.put(q);
        }

        // create /pois POST body: request=pois, geometry = Linestring, buffer
        JSONObject poisBody = new JSONObject();
        poisBody.put("request", "pois");
        JSONObject geom = new JSONObject();
        geom.put("type", "LineString");
        geom.put("coordinates", lines);
        poisBody.put("geometry", geom);
        poisBody.put("buffer", bufferMeters);
        poisBody.put("limit", Math.max(10, maxPlaces));

        String poisUrl = "https://api.openrouteservice.org/pois";
        String poisResp = httpPostJson(poisUrl, poisBody.toString(), Map.of("Authorization", orsApiKey, "Content-Type", "application/json"));
        JSONObject proot = new JSONObject(poisResp);
        JSONArray pfeatures = proot.optJSONArray("features");
        if (pfeatures == null) return Collections.emptyList();

        LinkedHashMap<String, Place> found = new LinkedHashMap<>();
        for (int i = 0; i < pfeatures.length() && found.size() < maxPlaces; i++) {
            JSONObject f = pfeatures.getJSONObject(i);
            JSONObject props = f.optJSONObject("properties");
            JSONObject geomObj = f.optJSONObject("geometry");
            if (props == null || geomObj == null) continue;
            String name = props.optString("name", props.optString("local_name", null));
            String kind = props.optString("category", props.optString("kind", "poi"));
            JSONArray gcoords = geomObj.optJSONArray("coordinates");
            double lon = gcoords != null ? gcoords.getDouble(0) : Double.NaN;
            double lat = gcoords != null ? gcoords.getDouble(1) : Double.NaN;
            if (name == null || Double.isNaN(lat) || Double.isNaN(lon)) continue;
            String key = String.format("%.6f:%.6f:%s", lat, lon, name);
            if (!found.containsKey(key)) found.put(key, new Place(name, kind, lat, lon));
        }

        return new ArrayList<>(found.values());
    }

    // Prefer ORS geocoding (requires key). Fall back to Nominatim only if ORS key not provided.
    private static double[] geocodePreferORS(String q, String orsApiKey) throws IOException, JSONException {
        if (orsApiKey != null && !orsApiKey.isBlank()) {
            try {
                return geocodeUsingORS(q, orsApiKey);
            } catch (Exception ex) {
                // fallback to nominatim if ORS geocode fails
                System.err.println("ORS geocode failed, falling back to Nominatim: " + ex.getMessage());
            }
        }
        return geocodeNominatim(q);
    }

    // ORS geocode (search endpoint) -> returns [lat, lon]
    private static double[] geocodeUsingORS(String text, String orsKey) throws IOException, JSONException {
        String url = "https://api.openrouteservice.org/geocode/search?text=" + URLEncoder.encode(text, "UTF-8");
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setRequestProperty("Authorization", orsKey);
        c.setRequestProperty("Accept", "application/json");
        c.setConnectTimeout(15000);
        c.setReadTimeout(15000);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
            String s = br.lines().collect(Collectors.joining("\n"));
            JSONObject root = new JSONObject(s);
            JSONArray features = root.optJSONArray("features");
            if (features == null || features.length() == 0) return null;
            JSONArray coords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
            double lon = coords.getDouble(0);
            double lat = coords.getDouble(1);
            return new double[] { lat, lon };
        }
    }

    // Nominatim geocode (fallback) -> returns [lat, lon]
    private static double[] geocodeNominatim(String q) throws IOException, JSONException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(q, "UTF-8") + "&format=json&limit=1&email=contact@example.com";
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        // polite headers; add a contact email via query param and User-Agent
        c.setRequestProperty("User-Agent", "travelguide-app/1.0 (contact@example.com)");
        c.setRequestProperty("Referer", "https://your-app.example.com/");
        c.setConnectTimeout(15000); c.setReadTimeout(15000);
        int code = c.getResponseCode();
        if (code >= 400) throw new IOException("Nominatim returned HTTP response code: " + code + " for URL: " + url);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
            String s = br.lines().collect(Collectors.joining("\n"));
            JSONArray a = new JSONArray(s);
            if (a.length() == 0) return null;
            JSONObject o = a.getJSONObject(0);
            return new double[]{ Double.parseDouble(o.getString("lat")), Double.parseDouble(o.getString("lon")) };
        }
    }

    // HTTP helpers
    private static String httpPostJson(String urlStr, String body, Map<String,String> headers) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(urlStr).openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        for (Map.Entry<String,String> e : headers.entrySet()) c.setRequestProperty(e.getKey(), e.getValue());
        c.setConnectTimeout(20000); c.setReadTimeout(20000);
        try (OutputStream os = c.getOutputStream()) { os.write(body.getBytes("UTF-8")); }
        int code = c.getResponseCode();
        InputStream is = code >= 200 && code < 400 ? c.getInputStream() : c.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}