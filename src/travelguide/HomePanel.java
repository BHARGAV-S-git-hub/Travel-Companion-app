package travelguide;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class HomePanel extends BasePanel {
    private JLabel cityNameLabel;
    private JLabel countryLabel;
    private JLabel descriptionLabel;
    private JPanel quickStatsPanel;
    private JComboBox<String> stateCombo;
    
    public HomePanel(SmartTravelCompanion parent) {
        super(parent);
    }
    
    @Override
    protected void initPanel() {
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SmartTravelCompanion.getBackgroundColor());
        
        JPanel welcomeCard = createCard(null);
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setBackground(SmartTravelCompanion.getPrimaryColor());
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JPanel welcomeContent = new JPanel();
        welcomeContent.setLayout(new BoxLayout(welcomeContent, BoxLayout.Y_AXIS));
        welcomeContent.setOpaque(false);
        welcomeContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cityNameLabel = new JLabel(parent.getCurrentCity());
        cityNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        cityNameLabel.setForeground(Color.BLACK);
        cityNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        countryLabel = new JLabel(city.country + " | " + city.region);
        countryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        countryLabel.setForeground(Color.BLACK);
        countryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        welcomeContent.add(welcomeLabel);
        welcomeContent.add(Box.createRigidArea(new Dimension(0, 5)));
        welcomeContent.add(cityNameLabel);
        welcomeContent.add(Box.createRigidArea(new Dimension(0, 5)));
        welcomeContent.add(countryLabel);
        
        welcomeCard.add(welcomeContent, BorderLayout.CENTER);
        
        content.add(welcomeCard);
        content.add(Box.createRigidArea(new Dimension(0,10)));
        // City / State selection panel - replaced international list with Indian states
        //content.add(createCitySelectionPanel());
        content.add(Box.createRigidArea(new Dimension(0,10)));
        
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel descCard = createCard("About This State");
        descriptionLabel = new JLabel("<html><p style='width: 600px;'>" + city.description + "</p></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descCard.add(descriptionLabel, BorderLayout.CENTER);
        content.add(descCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        quickStatsPanel = createQuickStatsPanel();
        content.add(quickStatsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel featuresPanel = createFeaturesPanel();
        content.add(featuresPanel);
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    /*private JPanel createCitySelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        panel.add(new JLabel("Select State:"));

        String[] states = {
            "Karnataka", "Tamil Nadu", "Maharashtra", "Delhi", "Kerala",
            "West Bengal", "Rajasthan", "Uttar Pradesh", "Telangana", "Gujarat"
        };
        stateCombo = new JComboBox<>(states);
        stateCombo.setPreferredSize(new Dimension(220, 28));
        stateCombo.setSelectedItem(parent.getCurrentCity());
        stateCombo.addActionListener(e -> {
            String selected = (String) stateCombo.getSelectedItem();
            // update parent current city and refresh this panel
            try {
                parent.setCurrentCity(selected);
            } catch (Exception ex) {
                // if setCurrentCity isn't available, ignore (project likely has this method)
            }
            updateData();
        });

        panel.add(stateCombo);
        JButton goBtn = createStyledButton("Go", SmartTravelCompanion.getPrimaryColor());
        goBtn.addActionListener(e -> {
            String selected = (String) stateCombo.getSelectedItem();
            try {
                parent.setCurrentCity(selected);
            } catch (Exception ex) {}
            updateData();
        });
        panel.add(goBtn);

        return panel;
    }*/
    
    private JPanel createQuickStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        
        panel.add(createStatCard("Safety Index", city.safetyIndex + "%", SmartTravelCompanion.getAccentColor()));
        panel.add(createStatCard("Quality of Life", city.qualityOfLife + "%", SmartTravelCompanion.getPrimaryColor()));
        panel.add(createStatCard("Best Time", city.bestTimeToVisit, new Color(155, 89, 182)));
        panel.add(createStatCard("Climate", city.climate, new Color(230, 126, 34)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        panel.add(createFeatureCard("Weather & Climate", "Real-time weather updates and forecasts", "weather"));
        panel.add(createFeatureCard("Safety Information", "Crime statistics and safety index", "safety"));
        panel.add(createFeatureCard("Tourist Attractions", "Must-visit places and landmarks", "attractions"));
        panel.add(createFeatureCard("Hotels & Stay", "Best accommodation options", "hotels"));
        panel.add(createFeatureCard("Restaurants", "Top dining experiences", "restaurants"));
        panel.add(createFeatureCard("Tour Planner", "Plan your perfect itinerary", "planner"));
        
        return panel;
    }
    
    private JPanel createFeatureCard(String title, String desc, String panelName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><p style='width: 150px;'>" + desc + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        textPanel.add(descLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                parent.showPanel(panelName);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    @Override
    public void updateData() {
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        cityNameLabel.setText(city.name);
        countryLabel.setText(city.country + " | " + city.region);
        descriptionLabel.setText("<html><p style='width: 600px;'>" + city.description + "</p></html>");
        if (stateCombo != null) stateCombo.setSelectedItem(parent.getCurrentCity());
        
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
