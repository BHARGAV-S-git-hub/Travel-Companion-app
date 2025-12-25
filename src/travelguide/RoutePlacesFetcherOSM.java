package travelguide;

import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Fetch POIs along a route using:
 *  - Nominatim (geocode)
 *  - OSRM (route polyline)
 *  - Overpass (nearby POIs)
 */
public class RoutePlacesFetcherOSM {

    public static class Place {
        public final String name;
        public final String type;
        public final double lat, lon;
        public Place(String name, String type, double lat, double lon) {
            this.name = name; this.type = type; this.lat = lat; this.lon = lon;
        }
        @Override public String toString() { return name + " (" + type + ")"; }
    }

    public static List<Place> fetchPlacesAlongRoute(String origin, String destination,
                                                   int sampleStepPoints, int radiusMeters, int maxSamples) throws Exception {
        double[] o = geocode(origin);
        double[] d = geocode(destination);
        if (o == null || d == null) return Collections.emptyList();

        String osrmUrl = String.format("https://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=polyline",
                o[1], o[0], d[1], d[0]);
        String osrmJson = httpGet(osrmUrl);
        JSONObject root = new JSONObject(osrmJson);
        JSONArray routes = root.optJSONArray("routes");
        if (routes == null || routes.length() == 0) return Collections.emptyList();
        String poly = routes.getJSONObject(0).getString("geometry");
        List<double[]> path = decodePolyline(poly);

        List<double[]> sampled = samplePoints(path, sampleStepPoints);
        if (sampled.size() > maxSamples) {
            List<double[]> reduced = new ArrayList<>();
            double step = (double) sampled.size() / (double) maxSamples;
            for (int i = 0; i < maxSamples; i++) reduced.add(sampled.get((int)Math.min(sampled.size()-1, Math.round(i*step))));
            sampled = reduced;
        }

        StringBuilder q = new StringBuilder();
        q.append("[out:json][timeout:25];(");
        for (double[] p : sampled) {
            double lat = p[0], lon = p[1];
            q.append(String.format("node(around:%d,%.6f,%.6f)[\"tourism\"];way(around:%d,%.6f,%.6f)[\"tourism\"];relation(around:%d,%.6f,%.6f)[\"tourism\"];",
                    radiusMeters, lat, lon, radiusMeters, lat, lon, radiusMeters, lat, lon));
        }
        q.append(");out center;");

        String overpassUrl = "https://overpass-api.de/api/interpreter?data=" + URLEncoder.encode(q.toString(), "UTF-8");
        String overpassJson = httpGet(overpassUrl);

        JSONObject oroot = new JSONObject(overpassJson);
        JSONArray elements = oroot.optJSONArray("elements");
        if (elements == null) return Collections.emptyList();

        LinkedHashMap<String, Place> found = new LinkedHashMap<>();
        for (int i = 0; i < elements.length(); i++) {
            JSONObject el = elements.getJSONObject(i);
            long id = el.optLong("id", -1);
            JSONObject tags = el.optJSONObject("tags");
            if (tags == null) continue;
            String name = tags.optString("name", null);
            String tourism = tags.optString("tourism", tags.optString("amenity", "place"));
            double lat = el.has("lat") ? el.getDouble("lat") : (el.has("center") ? el.getJSONObject("center").getDouble("lat") : Double.NaN);
            double lon = el.has("lon") ? el.getDouble("lon") : (el.has("center") ? el.getJSONObject("center").getDouble("lon") : Double.NaN);
            if (name == null || Double.isNaN(lat) || Double.isNaN(lon)) continue;
            String key = tourism + ":" + id;
            if (!found.containsKey(key)) found.put(key, new Place(name, tourism, lat, lon));
        }

        return new ArrayList<>(found.values());
    }

    private static double[] geocode(String q) throws IOException, JSONException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(q, "UTF-8") + "&format=json&limit=1";
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setRequestProperty("User-Agent", "travelguide-app/1.0 (contact@example.com)");
        c.setConnectTimeout(15000); c.setReadTimeout(15000);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
            String s = br.lines().collect(Collectors.joining("\n"));
            JSONArray a = new JSONArray(s);
            if (a.length() == 0) return null;
            JSONObject o = a.getJSONObject(0);
            return new double[] { Double.parseDouble(o.getString("lat")), Double.parseDouble(o.getString("lon")) };
        }
    }

    private static String httpGet(String urlStr) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(urlStr).openConnection();
        c.setRequestProperty("User-Agent", "travelguide-app/1.0");
        c.setConnectTimeout(15000);
        c.setReadTimeout(20000);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    private static List<double[]> decodePolyline(String encoded) {
        List<double[]> path = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0; result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            path.add(new double[] { lat / 1E5, lng / 1E5 });
        }
        return path;
    }

    private static List<double[]> samplePoints(List<double[]> path, int step) {
        if (path == null || path.isEmpty()) return Collections.emptyList();
        if (step <= 1) return new ArrayList<>(path);
        List<double[]> out = new ArrayList<>();
        for (int i = 0; i < path.size(); i += step) out.add(path.get(i));
        if (!out.contains(path.get(path.size() - 1))) out.add(path.get(path.size() - 1));
        return out;
    }
}