package travelguide;

/**
 * Put API keys here. DO NOT commit real keys to public repos.
 */
public final class ApiKeys {
    // OpenWeatherMap (weather + air pollution)
    // Key you provided:
    public static final String OPENWEATHERMAP_KEY = "07f0ed07b37395dd0d4930e43bfb6d05";

    // Optional: Crime API (Crimeometer). Sign up if you want incident-based crime indices.
    // If left null/empty, SafetyPanel will use CityData fallback values.
    public static final String CRIMEOMETER_KEY = ""; // e.g. "your_crimeometer_api_key_here"

    private ApiKeys() {}
}