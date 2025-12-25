package travelguide;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class RestaurantsPanel extends BasePanel {
    // keep a field for menu button references so any external references compile
    private JButton menuBtn;

    public RestaurantsPanel(SmartTravelCompanion parent) {
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

        content.add(createHeaderSection("Restaurants & Dining",
            "Best places to eat in " + parent.getCurrentCity()));

        JPanel filterPanel = createFilterPanel();
        content.add(filterPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        Map<String, String[]> restaurantDetails = getRestaurantDetails(parent.getCurrentCity());

        for (String restaurant : city.restaurants) {
            String[] details = restaurantDetails.getOrDefault(restaurant,
                new String[]{"Popular local restaurant", "Local", "₹500", "4.2", "10:00 - 22:00"});
            content.add(createRestaurantCard(restaurant, details[0], details[1], details[2], details[3], details[4]));
            content.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        content.add(Box.createVerticalGlue());

        return content;
    }

    private JPanel createFilterPanel() {
        JPanel panel = createCard(null);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        panel.add(new JLabel("Cuisine:"));
        String[] cuisines = {"All", "Local", "Indian", "South Indian", "North Indian", "Gujarati", "Hyderabadi"};
        JComboBox<String> cuisineBox = new JComboBox<>(cuisines);
        panel.add(cuisineBox);

        panel.add(new JLabel("Price:"));
        String[] prices = {"Any", "500", "₹100", "₹600", "₹800"};
        JComboBox<String> priceBox = new JComboBox<>(prices);
        panel.add(priceBox);

        panel.add(new JLabel("Rating:"));
        String[] ratings = {"Any", "4+ Stars", "3+ Stars"};
        JComboBox<String> ratingBox = new JComboBox<>(ratings);
        panel.add(ratingBox);

        JButton searchBtn = createStyledButton("Search", SmartTravelCompanion.getPrimaryColor());
        panel.add(searchBtn);

        return panel;
    }

    private JPanel createRestaurantCard(String name, String description, String cuisine, String price, String rating, String hours) {
        JPanel card = createCard(null);
        card.setLayout(new BorderLayout(15, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(230, 126, 34));
        imagePanel.setPreferredSize(new Dimension(120, 0));
        JLabel imageLabel = new JLabel("🍽️", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 45));
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

        JLabel cuisineLabel = new JLabel(cuisine);
        cuisineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cuisineLabel.setForeground(Color.BLACK);
        cuisineLabel.setOpaque(true);
        cuisineLabel.setBackground(new Color(155, 89, 182));
        cuisineLabel.setBorder(new EmptyBorder(2, 8, 2, 8));

        headerRow.add(nameLabel);
        headerRow.add(cuisineLabel);

        JLabel descLabel = new JLabel("<html><p style='width: 400px;'>" + description + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel detailsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        detailsRow.setOpaque(false);
        detailsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price: " + price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLabel.setForeground(SmartTravelCompanion.getAccentColor());

        JLabel ratingLabel = new JLabel("⭐ " + rating);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel hoursLabel = new JLabel("🕐 " + hours);
        hoursLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hoursLabel.setForeground(Color.BLACK);

        detailsRow.add(priceLabel);
        detailsRow.add(ratingLabel);
        detailsRow.add(hoursLabel);

        infoPanel.add(headerRow);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        infoPanel.add(detailsRow);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(130, 0));

        JButton reserveBtn = createStyledButton("Reserve Table", SmartTravelCompanion.getAccentColor());
        reserveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        reserveBtn.addActionListener(ev -> {
            String dateStr = JOptionPane.showInputDialog(this, "Enter reservation date (YYYY-MM-DD) or leave empty:", LocalDate.now().toString());
            String details = cuisine + " • " + price + " • " + hours;
            TourPlannerPanel.addToCurrentPlan("Restaurant", name, dateStr == null ? "13:00" : "13:00", details);
            if (JOptionPane.showConfirmDialog(this, "Also add to My Trips?", "Add", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                LocalDate d = MyTrips.parseDateOrNull(dateStr);
                MyTrips.TripItem item = new MyTrips.TripItem("Restaurant", name, d, details);
                MyTrips.showAddToTripDialog(this, item);
            }
        });

        // assign to the class field in case other code references menuBtn
        menuBtn = createStyledButton("View Menu", SmartTravelCompanion.getPrimaryColor());
        menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // optional: show simple menu dialog (keeps existing behavior)
        menuBtn.addActionListener(ev -> {
            JOptionPane.showMessageDialog(this, "<html><b>" + name + "</b><br/><br/>" + description + "</html>", "Menu / Details", JOptionPane.PLAIN_MESSAGE);
        });

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(reserveBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(menuBtn);
        buttonPanel.add(Box.createVerticalGlue());

        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private Map<String, String[]> getRestaurantDetails(String city) {
        Map<String, String[]> details = new HashMap<>();

        // Karnataka / Bengaluru
        details.put("Karavalli", new String[]{"Coastal and South Indian specialties in a refined setting", "South Indian", "₹500", "4.6", "12:00 - 15:00, 19:00 - 22:30"});
        details.put("MTR", new String[]{"Iconic Bangalore institution for hearty South Indian breakfasts and thalis", "South Indian", "₹", "4.3", "07:00 - 22:00"});
        details.put("Vidyarthi Bhavan", new String[]{"Legendary dosa spot in Basavanagudi", "South Indian", "₹100", "4.4", "07:00 - 16:00"});
        details.put("Koshy's", new String[]{"Classic Bangalore cafe known for decades of literary and cultural patrons", "Cafe", "₹800", "4.1", "08:00 - 22:00"});
        
        // Tamil Nadu / Chennai
        details.put("Murugan Idli Shop", new String[]{"Famous for soft idlis and chutneys across Chennai", "South Indian", "₹700", "4.4", "06:00 - 22:00"});
        details.put("Saravana Bhavan", new String[]{"International South Indian vegetarian chain originating in Chennai", "South Indian", "₹800", "4.2", "07:00 - 23:00"});
        details.put("Dakshin", new String[]{"High-end South Indian tasting menu with regional specialties", "South Indian", "₹850", "4.5", "12:00 - 14:30, 19:00 - 22:30"});
        details.put("Anjappar", new String[]{"Popular Chettinad and spicy South Indian non-vegetarian dishes", "Chettinad", "740", "4.1", "11:00 - 23:00"});
        
        // Maharashtra / Mumbai
        details.put("The Table", new String[]{"Contemporary global cuisine with seasonal ingredients", "Global", "₹500", "4.5", "12:00 - 15:00, 19:00 - 23:00"});
        details.put("Leopold Cafe", new String[]{"Iconic Colaba cafe and casual dining spot", "Cafe / Bar", "₹450", "4.0", "10:00 - 23:30"});
        details.put("Bademiya", new String[]{"Famous late-night kebab stall behind Taj Mahal Hotel", "Street Food", "₹741", "4.2", "20:00 - 03:00"});
        details.put("Britannia & Co.", new String[]{"Historic Parsi cafe known for berry pulav and dhansak", "Parsi", "₹300", "4.3", "08:00 - 22:00"});
        
        // Delhi / NCR
        details.put("Karim's", new String[]{"Legendary Mughlai cuisine near Jama Masjid", "Mughlai", "₹180", "4.4", "11:00 - 23:00"});
        details.put("Bukhara", new String[]{"North-West Frontier style tandoor specialties at ITC Maurya", "North Indian", "₹745", "4.6", "12:00 - 15:00, 19:00 - 23:00"});
        details.put("Indian Accent", new String[]{"Contemporary Indian fine dining with inventive flavors", "Modern Indian", "₹625", "4.7", "12:30 - 14:30, 19:00 - 22:30"});
        details.put("Diggin", new String[]{"Casual all-day eatery with Mediterranean-inspired fare", "Cafe", "₹855", "4.1", "09:00 - 23:00"});
        
        // Kerala
        details.put("The Rice Boat", new String[]{"Traditional Keralite seafood served in a unique setting", "Seafood", "₹985", "4.4", "12:00 - 15:00, 19:00 - 23:00"});
        details.put("Paragon", new String[]{"Famous for Malabar cuisine in Kozhikode and Kochi", "Malabar", "₹652", "4.3", "11:00 - 23:00"});
        details.put("Kayees", new String[]{"Renowned Malabar biryani and seafood", "Malabar", "₹325", "4.2", "11:00 - 22:00"});
        details.put("Fort House (Kovalam)", new String[]{"Seafood specialty in a coastal Kerala setting", "Seafood", "₹852", "4.0", "12:00 - 22:00"});
        
        // West Bengal / Kolkata
        details.put("Oh! Calcutta", new String[]{"Refined Bengali specialties with family recipes", "Bengali", "₹245", "4.3", "12:00 - 15:00, 19:00 - 23:00"});
        details.put("6 Ballygunge Place", new String[]{"Traditional Bengali recipes in a classic setting", "Bengali", "₹635", "4.2", "12:00 - 15:00, 19:00 - 23:00"});
        details.put("Peter Cat", new String[]{"Iconic Kolkata cafe known for Chelo Kebab", "Cafe / Continental", "₹412", "4.1", "08:00 - 23:00"});
        details.put("Bhojohori Manna", new String[]{"Popular chain for homestyle Bengali food", "Bengali", "₹253", "4.0", "12:00 - 22:00"});
        
        // Rajasthan
        details.put("Chokhi Dhani (Jaipur dining)", new String[]{"Rajasthani village-themed dining with cultural performances", "Rajasthani", "₹325", "4.2", "19:00 - 23:00"});
        details.put("Suvarna Mahal", new String[]{"Luxury palace dining with Rajasthani and Mughlai fare", "Royal Indian", "₹852", "4.4", "19:00 - 23:00"});
        details.put("LMB (Jaipur)", new String[]{"Local sweets and Rajasthani thalis", "Rajasthani", "₹256", "4.0", "08:00 - 22:00"});
        
        // Uttar Pradesh
        details.put("Tunday Kababi", new String[]{"World-famous Lucknowi kebabs and galouti kabab", "Mughlai", "₹951", "4.5", "10:00 - 22:00"});
        details.put("Pind Balluchi", new String[]{"Punjabi and North Indian restaurant popular across UP", "North Indian", "₹358", "4.0", "11:00 - 23:00"});
        
        // Telangana / Hyderabad
        details.put("Paradise Biryani", new String[]{"Hyderabadi biryani institution", "Biryani", "₹256", "4.4", "11:00 - 23:00"});
        details.put("Bawarchi", new String[]{"Popular biryani and Hyderabadi specialties", "Biryani", "₹152", "4.1", "11:00 - 23:00"});
        details.put("Ohri's", new String[]{"Casual dining and local favorites in multiple outlets", "Multi-cuisine", "₹365", "4.0", "10:00 - 23:00"});
        details.put("Chutneys", new String[]{"Famous for wide variety of chutneys with dosas and idlis", "South Indian", "₹753", "4.2", "07:00 - 22:00"});
        
        // Gujarat
        details.put("Gordhan Thal", new String[]{"Traditional Gujarati thali restaurant", "Gujarati", "₹253", "4.1", "11:00 - 22:00"});
        details.put("Agashiye", new String[]{"Rooftop fine dining serving authentic Gujarati cuisine", "Gujarati", "₹235", "4.4", "12:00 - 15:00, 19:00 - 22:30"});
        details.put("Manek Chowk street food", new String[]{"Late-night street-food hub offering local specialties", "Street Food", "₹245", "4.0", "20:00 - 02:00"});
        
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
