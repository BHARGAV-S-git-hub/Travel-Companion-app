package travelguide;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class HotelsPanel extends BasePanel {
    
    public HotelsPanel(SmartTravelCompanion parent) {
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
        
        add(createHeaderSection("Hotels & Accommodation", 
            "Top hotels and places to stay in " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        JPanel filterPanel = createFilterPanel();
        content.add(filterPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        Map<String, String[]> hotelDetails = getHotelDetails(parent.getCurrentCity());
        
        for (String hotel : city.hotels) {
            String[] details = hotelDetails.getOrDefault(hotel, 
                new String[]{"Luxury accommodation with excellent amenities", "5", "₹3500", "4.5", "WiFi, Pool, Spa"});
            content.add(createHotelCard(hotel, details[0], details[1], details[2], details[3], details[4]));
            content.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = createCard(null);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        panel.add(new JLabel("Filter by:"));
        
        String[] categories = {"All", "5 Star", "4 Star", "3 Star", "Budget"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        panel.add(categoryBox);
        
        panel.add(new JLabel("Price:"));
        String[] prices = {"Any", "₹0-100", "₹100-250", "₹250-500", "₹500+"};
        JComboBox<String> priceBox = new JComboBox<>(prices);
        panel.add(priceBox);
        
        panel.add(new JLabel("Amenities:"));
        JCheckBox poolCheck = new JCheckBox("Pool");
        JCheckBox wifiCheck = new JCheckBox("WiFi");
        JCheckBox spaCheck = new JCheckBox("Spa");
        wifiCheck.setSelected(true);
        panel.add(poolCheck);
        panel.add(wifiCheck);
        panel.add(spaCheck);
        
        JButton searchBtn = createStyledButton("Search", SmartTravelCompanion.getPrimaryColor());
        panel.add(searchBtn);
        
        return panel;
    }
    
    private JPanel createHotelCard(String name, String description, String stars, String price, String rating, String amenities) {
        JPanel card = createCard(null);
        card.setLayout(new BorderLayout(15, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(200, 200, 200));
        imagePanel.setPreferredSize(new Dimension(150, 0));
        JLabel imageLabel = new JLabel("🏨", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        
        JLabel starsLabel = new JLabel(getStars(Integer.parseInt(stars)));
        starsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        starsLabel.setForeground(Color.BLACK);
        
        headerRow.add(nameLabel);
        headerRow.add(starsLabel);
        
        JLabel descLabel = new JLabel("<html><p style='width: 350px;'>" + description + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel amenitiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        amenitiesPanel.setOpaque(false);
        amenitiesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (String amenity : amenities.split(", ")) {
            JLabel chip = new JLabel(amenity);
            chip.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            chip.setForeground(SmartTravelCompanion.getPrimaryColor());
            chip.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(SmartTravelCompanion.getPrimaryColor(), 1),
                new EmptyBorder(2, 6, 2, 6)
            ));
            amenitiesPanel.add(chip);
        }
        
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ratingRow.setOpaque(false);
        ratingRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("⭐ " + rating + "/5");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ratingLabel.setForeground(SmartTravelCompanion.getAccentColor());
        
        JLabel reviewsLabel = new JLabel("(1,234 reviews)");
        reviewsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reviewsLabel.setForeground(Color.BLACK);
        
        ratingRow.add(ratingLabel);
        ratingRow.add(reviewsLabel);
        
        infoPanel.add(headerRow);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(amenitiesPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(ratingRow);
        
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);
        pricePanel.setPreferredSize(new Dimension(120, 0));
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        priceLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel perNightLabel = new JLabel("per night");
        perNightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        perNightLabel.setForeground(Color.BLACK);
        perNightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton bookBtn = createStyledButton("Book Now", SmartTravelCompanion.getAccentColor());
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add to Tour Planner (adds current hotel to Tour Planner plan)
        bookBtn.addActionListener(ev -> {
            String dateStr = JOptionPane.showInputDialog(this, "Enter date to book (YYYY-MM-DD) or leave empty:", LocalDate.now().toString());
            String detailsText = price + " • " + amenities; // assume price/amenities variables exist in scope
            TourPlannerPanel.addToCurrentPlan("Hotel", name, dateStr == null ? "20:00" : "20:00", detailsText);
            // also offer to add directly to My Trips if desired
            if (JOptionPane.showConfirmDialog(this, "Also add to My Trips?", "Add", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                LocalDate d = MyTrips.parseDateOrNull(dateStr);
                MyTrips.TripItem ti = new MyTrips.TripItem("Hotel", name, d, detailsText);
                MyTrips.showAddToTripDialog(this, ti);
            }
        });
        
        pricePanel.add(Box.createVerticalGlue());
        pricePanel.add(priceLabel);
        pricePanel.add(perNightLabel);
        pricePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        pricePanel.add(bookBtn);
        pricePanel.add(Box.createVerticalGlue());
        
        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(pricePanel, BorderLayout.EAST);
        
        return card;
    }
    
    private String getStars(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append("★");
        for (int i = count; i < 5; i++) sb.append("☆");
        return sb.toString();
    }
    
    private Map<String, String[]> getHotelDetails(String city) {
        Map<String, String[]> details = new HashMap<>();
        
        // Karnataka
        details.put("The Leela Palace Bengaluru", new String[]{
            "Luxury hotel with lakeside views and fine dining in central Bengaluru.",
            "5", "₹7000", "4.6", "WiFi, Pool, Spa, Gym"});
        details.put("Taj West End", new String[]{
            "Heritage hotel with lush gardens and colonial charm.",
            "5", "₹5000", "4.5", "WiFi, Pool, Spa, Restaurant"});
        details.put("ITC Windsor", new String[]{
            "Elegant heritage luxury hotel near MG Road.",
            "5", "₹6800", "4.4", "WiFi, Spa, Dining"});
        details.put("Eagleton Resort", new String[]{
            "Luxury resort near Bengaluru ideal for golf & nature retreats.",
            "5", "₹2000", "4.3", "Golf, Pool, Spa, Dining"});
        
        // Tamil Nadu
        details.put("Taj Coromandel", new String[]{
            "Luxury heritage hotel in Chennai with celebrated dining.",
            "5", "₹10500", "4.6", "WiFi, Pool, Spa, Restaurant"});
        details.put("ITC Grand Chola", new String[]{
            "Opulent Chennai hotel with royal Tamil architecture and extensive amenities.",
            "5", "₹2600", "4.7", "WiFi, Pool, Spa, Restaurants"});
        details.put("The Leela Palace Chennai", new String[]{
            "Seafront luxury with coastal views and refined service.",
            "5", "₹5270", "4.6", "WiFi, Pool, Spa"});
        details.put("Radisson Blu Temple Bay", new String[]{
            "Popular seaside business hotel near Mahabalipuram.",
            "4", "₹8120", "4.2", "WiFi, Pool, Dining"});
        
        // Maharashtra
        details.put("The Taj Mahal Palace", new String[]{
            "Historic landmark hotel overlooking the Gateway of India in Mumbai.",
            "5", "₹350", "4.8", "WiFi, Pool, Spa, Dining"});
        details.put("The Oberoi Mumbai", new String[]{
            "Luxury business hotel with bay views and refined service.",
            "5", "₹6320", "4.7", "WiFi, Spa, Dining"});
        details.put("ITC Maratha", new String[]{
            "Luxury hotel near the airport with grand interiors.",
            "5", "₹5200", "4.5", "WiFi, Spa, Dining"});
        details.put("Four Seasons Mumbai", new String[]{
            "Upscale hotel with contemporary design and city views.",
            "5", "₹6400", "4.6", "WiFi, Pool, Spa"});
        
        // Delhi
        details.put("The Leela Palace New Delhi", new String[]{
            "Palatial luxury with large rooms and fine dining near diplomatic area.",
            "5", "₹5330", "4.7", "WiFi, Pool, Spa"});
        details.put("Taj Mahal Hotel", new String[]{
            "Iconic luxury hotel in central Delhi with colonial charm.",
            "5", "₹3300", "4.7", "WiFi, Spa, Dining"});
        details.put("ITC Maurya", new String[]{
            "Renowned luxury hotel with award-winning restaurants.",
            "5", "₹7280", "4.6", "WiFi, Spa, Dining"});
        details.put("Oberoi New Delhi", new String[]{
            "Luxury hotel with modern amenities and excellent service.",
            "5", "₹5310", "4.7", "WiFi, Spa, Dining"});
        
        // Kerala
        details.put("Kumarakom Lake Resort", new String[]{
            "Top backwater resort with traditional Kerala architecture and houseboat experiences.",
            "5", "₹6220", "4.8", "WiFi, Pool, Ayurveda, Houseboats"});
        details.put("Taj Malabar, Kochi", new String[]{
            "Heritage property with sea views and premium dining.",
            "5", "₹5200", "4.5", "WiFi, Pool, Dining"});
        details.put("Carnoustie Ayurveda & Wellness Resort", new String[]{
            "Wellness resort focused on Ayurveda near Kovalam.",
            "5", "₹4240", "4.6", "Ayurveda, Spa, Pool"});
        details.put("Leela Kovalam", new String[]{
            "Clifftop resort with panoramic ocean views and luxe amenities.",
            "5", "₹2360", "4.6", "WiFi, Pool, Spa"});
        
        // West Bengal
        details.put("The Oberoi Grand", new String[]{
            "Heritage luxury hotel in central Kolkata with colonial elegance.",
            "5", "₹5180", "4.6", "WiFi, Dining, Concierge"});
        details.put("ITC Sonar", new String[]{
            "Upscale hotel with premium services and dining options.",
            "5", "₹5150", "4.5", "WiFi, Pool, Dining"});
        details.put("Taj Bengal", new String[]{
            "Luxury property with landscaped gardens and fine dining.",
            "5", "₹1270", "4.6", "WiFi, Pool, Spa"});
        details.put("The Park Kolkata", new String[]{
            "Boutique-style luxury hotel in central Kolkata.",
            "5", "₹4120", "4.3", "WiFi, Dining"});
        
        // Rajasthan
        details.put("Rambagh Palace", new String[]{
            "Former royal palace in Jaipur offering a regal stay.",
            "5", "₹4320", "4.8", "WiFi, Pool, Spa"});
        details.put("City Palace Hotel Udaipur", new String[]{
            "Lakeside palace hotel with historic character and views.",
            "5", "₹5250", "4.6", "WiFi, Dining"});
        details.put("Taj Hari Mahal", new String[]{
            "Luxury heritage hotel with Rajasthani architecture in Jodhpur.",
            "5", "₹9200", "4.5", "WiFi, Pool, Dining"});
        details.put("Rajvilas", new String[]{
            "Boutique palace resort outside Jaipur with secluded grounds.",
            "5", "₹7280", "4.7", "WiFi, Pool, Spa"});
        
        // Uttar Pradesh
        details.put("Tajview Agra", new String[]{
            "Hotel with direct views of the Taj Mahal and elegant rooms.",
            "4", "₹5120", "4.4", "WiFi, Dining"});
        details.put("Ramada Plaza Agra", new String[]{
            "Comfortable hotel with close access to Agra's attractions.",
            "4", "₹4090", "4.1", "WiFi, Pool, Dining"});
        details.put("Vivanta by Taj (Varanasi)", new String[]{
            "Luxury property by the Ganges with curated experiences.",
            "5", "₹5200", "4.5", "WiFi, Dining, Concierge"});
        details.put("ITC Mughal", new String[]{
            "Luxury hotel with Mughal-inspired design in Agra.",
            "5", "₹5230", "4.6", "WiFi, Pool, Dining"});
        
        // Telangana
        details.put("Taj Falaknuma Palace", new String[]{
            "Palace-turned-hotel with opulent heritage rooms and panoramic city views.",
            "5", "₹5350", "4.8", "WiFi, Pool, Spa, Heritage"});
        details.put("ITC Kakatiya", new String[]{
            "Luxury hotel with regional hospitality in Hyderabad.",
            "5", "₹6140", "4.5", "WiFi, Pool, Dining"});
        details.put("Novotel Hyderabad", new String[]{
            "Modern business hotel with comfortable amenities.",
            "4", "₹6690", "4.2", "WiFi, Pool, Dining"});
        details.put("Trident Hyderabad", new String[]{
            "Upscale hotel with serene grounds and river views.",
            "5", "₹78180", "4.6", "WiFi, Pool, Spa"});
        
        // Gujarat
        details.put("Taj Mahal Palace (Bhavnagar)", new String[]{
            "Comfortable heritage-style hotel in Bhavnagar region.",
            "4", "₹90", "4.2", "WiFi, Dining"});
        details.put("Ginger Hotels", new String[]{
            "Budget-friendly reliable chain across Gujarat and India.",
            "3", "₹40", "4.0", "WiFi, Breakfast"});
        details.put("The Fern Residency", new String[]{
            "Mid-range hotel with contemporary amenities in Ahmedabad.",
            "3", "₹60", "4.1", "WiFi, Dining"});
        details.put("Hyatt Regency Ahmedabad", new String[]{
            "Upscale business hotel with modern facilities.",
            "5", "₹160", "4.5", "WiFi, Pool, Dining"});
        
        return details;
    }
    
    @Override
    public void updateData() {
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
