package travelguide;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;

public class SafetyPanel extends BasePanel {
    private JPanel citiesCrimePanel;

    public SafetyPanel(SmartTravelCompanion parent) { super(parent); }

    @Override
    protected void initPanel() {
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        fetchSafetyData();
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SmartTravelCompanion.getBackgroundColor());

        content.add(createHeaderSection("Safety & Crime Statistics",
            "Safety information for " + parent.getCurrentCity()));

        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());

        JPanel overviewPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        overviewPanel.setOpaque(false);
        overviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        overviewPanel.add(createIndexCard("Safety Index", city.safetyIndex, SmartTravelCompanion.getAccentColor()));
        overviewPanel.add(createIndexCard("Crime Index", city.crimeIndex, new Color(241, 196, 15)));
        overviewPanel.add(createIndexCard("Quality of Life", city.qualityOfLife, SmartTravelCompanion.getPrimaryColor()));

        content.add(overviewPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // denser grid so the 5 city cards make better use of space
        citiesCrimePanel = new JPanel(new GridLayout(0, 2, 12, 12));
        citiesCrimePanel.setOpaque(false);
        citiesCrimePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 900));
        content.add(citiesCrimePanel);

        content.add(Box.createRigidArea(new Dimension(0, 12)));
        return content;
    }

    private JPanel createCityCrimeCard(String cityName, int crimeIndex, int safetyIndex) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(340, 140));
        card.setBackground(SmartTravelCompanion.getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(220,220,220),1),
            new javax.swing.border.EmptyBorder(10,10,10,10)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(cityName);
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel crime = new JLabel("Crime Index: " + crimeIndex);
        crime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel safe = new JLabel("Safety Index: " + safetyIndex);
        safe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        crime.setAlignmentX(Component.LEFT_ALIGNMENT);
        safe.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(name);
        card.add(Box.createRigidArea(new Dimension(0,6)));
        card.add(crime);
        card.add(Box.createRigidArea(new Dimension(0,6)));
        card.add(safe);
        return card;
    }

    private void fetchSafetyData() {
        // If no Crimeometer key, use CityData per-city defaults
        if (ApiKeys.CRIMEOMETER_KEY == null || ApiKeys.CRIMEOMETER_KEY.trim().isEmpty()) {
            System.out.println("No crime API key provided — using CityData per-city fallbacks.");
            // populate using CityData
            SwingUtilities.invokeLater(() -> populateCitiesFromCityData());
            return;
        }

        new Thread(() -> {
            try {
                //List<CityData.CityInfo> topCities = CityData.getTopCitiesForState(parent.getCurrentCity());
                java.util.List<CityData.CityInfo> topCities = CityData.getTopCitiesForState(parent.getCurrentCity());
                SwingUtilities.invokeLater(() -> {
                    citiesCrimePanel.removeAll();
                    citiesCrimePanel.revalidate();
                    citiesCrimePanel.repaint();
                });

                for (CityData.CityInfo ci : topCities) {
                    try {
                        // Crimeometer example: incidents count in last 30 days — adjust endpoint per docs
                        String now = URLEncoder.encode(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString(), "UTF-8");
                        String past = URLEncoder.encode(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).minusDays(30).toString(), "UTF-8");
                        String url = String.format("https://api.crimeometer.us/v1/incidents/raw-data?lat=%s&lon=%s&distance=10mi&datetime_ini=%s&datetime_end=%s",
                                ci.latitude, ci.longitude, past, now);
                        Map<String,String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("x-api-key", ApiKeys.CRIMEOMETER_KEY);

                        String resp = ApiClient.get(url, headers);
                        System.out.println("Crime API raw response for " + ci.name + ": " + resp);

                        JsonObject obj = JsonParser.parseString(resp).getAsJsonObject();
                        int incidentsCount = 0;
                        if (obj.has("incidents")) incidentsCount = obj.getAsJsonArray("incidents").size();
                        else if (obj.has("results")) incidentsCount = obj.getAsJsonArray("results").size();

                        int crimeIndex = Math.min(100, incidentsCount * 2); // simple scaling
                        int safetyIndex = Math.max(0, 100 - crimeIndex);

                        JPanel card = createCityCrimeCard(ci.name, crimeIndex, safetyIndex);
                        SwingUtilities.invokeLater(() -> {
                            citiesCrimePanel.add(card);
                            citiesCrimePanel.revalidate();
                            citiesCrimePanel.repaint();
                        });

                    } catch (Exception inner) {
                        inner.printStackTrace();
                        // fallback to CityData values if API fails for that city
                        SwingUtilities.invokeLater(() -> {
                            citiesCrimePanel.add(createCityCrimeCard(ci.name, ci.crimeIndex, ci.safetyIndex));
                            citiesCrimePanel.revalidate();
                            citiesCrimePanel.repaint();
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void populateCitiesFromCityData() {
        citiesCrimePanel.removeAll();
        java.util.List<CityData.CityInfo> topCities = CityData.getTopCitiesForState(parent.getCurrentCity());
        for (CityData.CityInfo ci : topCities) {
            citiesCrimePanel.add(createCityCrimeCard(ci.name, ci.crimeIndex, ci.safetyIndex));
        }
        citiesCrimePanel.revalidate();
        citiesCrimePanel.repaint();
    }

    private JPanel createIndexCard(String title, int value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(220,220,220),1),
            new javax.swing.border.EmptyBorder(15,15,15,15)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0,10)));
        card.add(valueLabel);
        return card;
    }

    @Override
    public void updateData() {
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
