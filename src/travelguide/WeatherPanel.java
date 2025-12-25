package travelguide;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.google.gson.*;

public class WeatherPanel extends BasePanel {
    private JLabel tempLabel;
    private JLabel conditionLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
    private JPanel forecastPanel;
    private JPanel aqiPanel;

    public WeatherPanel(SmartTravelCompanion parent) {
        super(parent);
    }

    @Override
    protected void initPanel() {
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        fetchWeatherData();
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SmartTravelCompanion.getBackgroundColor());

        content.add(createHeaderSection("Weather & Air Quality",
            "Current conditions for " + parent.getCurrentCity()));

        JPanel topCard = createCard(null);
        topCard.setLayout(new GridLayout(1, 2, 15, 0));
        // give a bit more height so top summary doesn't look cramped
        topCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel weatherBox = new JPanel();
        weatherBox.setLayout(new BoxLayout(weatherBox, BoxLayout.Y_AXIS));
        weatherBox.setOpaque(false);
        tempLabel = new JLabel("Temp: --°C");
        tempLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        conditionLabel = new JLabel("Condition: --");
        conditionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        humidityLabel = new JLabel("Humidity: --%");
        windLabel = new JLabel("Wind: -- m/s");
        weatherBox.add(tempLabel);
        weatherBox.add(Box.createRigidArea(new Dimension(0,8)));
        weatherBox.add(conditionLabel);
        weatherBox.add(humidityLabel);
        weatherBox.add(windLabel);

        aqiPanel = new JPanel();
        aqiPanel.setLayout(new BoxLayout(aqiPanel, BoxLayout.Y_AXIS));
        aqiPanel.setOpaque(false);
        JLabel aqiLabel = new JLabel("AQI: --");
        aqiLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        aqiPanel.add(aqiLabel);
        JLabel pm25Label = new JLabel("PM2.5: -- µg/m³");
        pm25Label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aqiPanel.add(pm25Label);
        JLabel pm10Label = new JLabel("PM10: -- µg/m³");
        pm10Label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aqiPanel.add(pm10Label);

        topCard.add(weatherBox);
        topCard.add(aqiPanel);

        content.add(topCard);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        forecastPanel = new JPanel();
        // 2-column grid for denser, more balanced layout
        forecastPanel.setLayout(new GridLayout(0, 2, 12, 12));
        // allow the area to expand vertically but not leave too much empty space
        forecastPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 900));
        forecastPanel.setOpaque(false);
        content.add(forecastPanel);

        // reduce excessive vertical glue so content packs tighter
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        return content;
    }

    private JPanel createCityWeatherCard(String cityName, double temp, String cond, String aqiDesc, double pm25, double pm10) {
        JPanel card = new JPanel();
        // larger, cleaner card to occupy space better
        card.setPreferredSize(new Dimension(340, 140));
        card.setBackground(SmartTravelCompanion.getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(220,220,220),1),
            new javax.swing.border.EmptyBorder(10,10,10,10)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(cityName);
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel w = new JLabel(String.format("Temp: %.1f°C  •  %s", temp, cond));
        w.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel aqi = new JLabel("AQI: " + aqiDesc + String.format("  •  PM2.5: %s µg/m³", pm25 >= 0 ? String.format("%.1f", pm25) : "--"));
        aqi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        w.setAlignmentX(Component.LEFT_ALIGNMENT);
        aqi.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(name);
        card.add(Box.createRigidArea(new Dimension(0,6)));
        card.add(w);
        card.add(Box.createRigidArea(new Dimension(0,6)));
        card.add(aqi);
        return card;
    }

    private void fetchWeatherData() {
        new Thread(() -> {
            try {
                // refresh main city top info
                CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
                if (city != null) {
                    double lat = city.latitude;
                    double lon = city.longitude;

                    String weatherUrl = String.format(
                        "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s",
                        lat, lon, ApiKeys.OPENWEATHERMAP_KEY);
                    String weatherJson = ApiClient.get(weatherUrl, null);
                    JsonObject wobj = JsonParser.parseString(weatherJson).getAsJsonObject();
                    double temp = wobj.getAsJsonObject("main").get("temp").getAsDouble();
                    String cond = wobj.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                    int humidity = wobj.getAsJsonObject("main").get("humidity").getAsInt();
                    double wind = wobj.getAsJsonObject("wind").has("speed") ? wobj.getAsJsonObject("wind").get("speed").getAsDouble() : 0.0;

                    SwingUtilities.invokeLater(() -> {
                        tempLabel.setText(String.format("Temp: %.1f°C", temp));
                        conditionLabel.setText("Condition: " + cond);
                        humidityLabel.setText("Humidity: " + humidity + "%");
                        windLabel.setText(String.format("Wind: %.1f m/s", wind));
                    });
                }

                // get top 5 cities for the selected state
                java.util.List<CityData.CityInfo> topCities = CityData.getTopCitiesForState(parent.getCurrentCity());
                // clear previous
                SwingUtilities.invokeLater(() -> {
                    forecastPanel.removeAll();
                    forecastPanel.revalidate();
                    forecastPanel.repaint();
                });

                for (CityData.CityInfo ci : topCities) {
                    try {
                        double lat = ci.latitude;
                        double lon = ci.longitude;
                        String weatherUrl = String.format(
                            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s",
                            lat, lon, ApiKeys.OPENWEATHERMAP_KEY);
                        String weatherJson = ApiClient.get(weatherUrl, null);
                        JsonObject wobj = JsonParser.parseString(weatherJson).getAsJsonObject();
                        double tempC = wobj.getAsJsonObject("main").get("temp").getAsDouble();
                        String cond = wobj.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

                        String aqiUrl = String.format(
                            "https://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s",
                            lat, lon, ApiKeys.OPENWEATHERMAP_KEY);
                        String aqiJson = ApiClient.get(aqiUrl, null);
                        JsonObject aqiObj = JsonParser.parseString(aqiJson).getAsJsonObject();
                        JsonObject first = aqiObj.getAsJsonArray("list").get(0).getAsJsonObject();
                        int aqiIndex = first.getAsJsonObject("main").get("aqi").getAsInt(); // 1..5
                        JsonObject comps = first.getAsJsonObject("components");
                        double pm25 = comps.has("pm2_5") ? comps.get("pm2_5").getAsDouble() : -1;
                        double pm10 = comps.has("pm10") ? comps.get("pm10").getAsDouble() : -1;
                        String aqiDesc;
                        switch (aqiIndex) {
                            case 1: aqiDesc = "Good"; break;
                            case 2: aqiDesc = "Fair"; break;
                            case 3: aqiDesc = "Moderate"; break;
                            case 4: aqiDesc = "Poor"; break;
                            case 5: aqiDesc = "Very Poor"; break;
                            default: aqiDesc = "Unknown"; break;
                        }

                        JPanel cityCard = createCityWeatherCard(ci.name, tempC, cond, aqiDesc, pm25, pm10);
                        SwingUtilities.invokeLater(() -> {
                            forecastPanel.add(cityCard);
                            forecastPanel.revalidate();
                            forecastPanel.repaint();
                        });

                    } catch (Exception inner) {
                        inner.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public void updateData() {
        fetchWeatherData();
    }
}
