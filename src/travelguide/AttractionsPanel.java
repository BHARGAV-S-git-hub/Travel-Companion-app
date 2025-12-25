package travelguide;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class AttractionsPanel extends BasePanel {
    
    public AttractionsPanel(SmartTravelCompanion parent) {
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
        
        add(createHeaderSection("Tourist Attractions", 
            "Top places to visit in " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        
        Map<String, String[]> attractionDetails = getAttractionDetails(parent.getCurrentCity());
        
        for (String attraction : city.attractions) {
            String[] details = attractionDetails.getOrDefault(attraction, 
                new String[]{"A must-visit destination", "09:00 - 18:00", "₹1000", "4.5"});
            content.add(createAttractionCard(attraction, details[0], details[1], details[2], details[3]));
            content.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createAttractionCard(String name, String description, String hours, String price, String rating) {
        JPanel card = createCard(null);
        card.setLayout(new BorderLayout(15, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(SmartTravelCompanion.getPrimaryColor());
        iconPanel.setPreferredSize(new Dimension(100, 0));
        JLabel iconLabel = new JLabel("📍", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><p style='width: 400px;'>" + description + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel detailsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        detailsRow.setOpaque(false);
        detailsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsRow.add(createDetailChip("🕐 " + hours));
        detailsRow.add(createDetailChip("💰 " + price));
        detailsRow.add(createDetailChip("⭐ " + rating));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        infoPanel.add(detailsRow);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton viewBtn = createStyledButton("View Details", SmartTravelCompanion.getPrimaryColor());
        JButton addToPlanBtn = createStyledButton("Add to Plan", SmartTravelCompanion.getAccentColor());
        
        addToPlanBtn.addActionListener(ev -> {
            String dateStr = JOptionPane.showInputDialog(this, "Enter visit date (YYYY-MM-DD) or leave empty:", LocalDate.now().toString());
            TourPlannerPanel.addToCurrentPlan("Attraction", name, dateStr == null ? "10:00" : "10:00", description);
            // optional: also offer to add into My Trips
            if (JOptionPane.showConfirmDialog(this, "Also add to My Trips?", "Add", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                LocalDate d = MyTrips.parseDateOrNull(dateStr);
                MyTrips.TripItem ti = new MyTrips.TripItem("Attraction", name, d, description);
                MyTrips.showAddToTripDialog(this, ti);
            }
        });
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(addToPlanBtn);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private JLabel createDetailChip(String text) {
        JLabel chip = new JLabel(text);
        chip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chip.setForeground(Color.BLACK);
        chip.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        return chip;
    }
    
    private Map<String, String[]> getAttractionDetails(String city) {
        Map<String, String[]> details = new HashMap<>();
        
        // Karnataka
        details.put("Bangalore Palace", new String[]{
            "19th-century Tudor-style palace with historic interiors and gardens.",
            "10:00 - 17:30", "₹900", "4.2"});
        details.put("Hampi (Warangal ruins)", new String[]{
            "UNESCO-listed ruins of the Vijayanagara Empire with dramatic boulder-strewn landscapes.",
            "Sunrise-Sunset", "₹950", "4.8"});
        details.put("Mysore Palace", new String[]{
            "Opulent royal palace in Mysore famed for Dussehra illuminations.",
            "10:00 - 17:30", "₹690", "4.6"});
        details.put("Coorg", new String[]{
            "Lush hill station known for coffee plantations and waterfalls.",
            "All day", "₹1100", "4.5"});
        details.put("Nagarhole National Park", new String[]{
            "Protected wildlife reserve with tiger, elephant and rich birdlife.",
            "06:00 - 18:00", "₹2500", "4.4"});
        
        // Tamil Nadu
        details.put("Meenakshi Temple (Madurai)", new String[]{
            "Dravidian temple complex noted for its towering gopurams and sculptures.",
            "05:00 - 21:00", "Free", "4.7"});
        details.put("Brihadeeswarar Temple (Thanjavur)", new String[]{
            "UNESCO site and architectural marvel from the Chola dynasty.",
            "06:00 - 18:00", "Free", "4.8"});
        details.put("Marina Beach", new String[]{
            "Long urban beach in Chennai popular for evening walks and street food.",
            "05:00 - 20:00", "Free", "4.0"});
        details.put("Mahabalipuram", new String[]{
            "Group of monuments and rock-cut temples on the Coromandel coast.",
            "Sunrise-Sunset", "₹590", "4.5"});
        
        // Maharashtra
        details.put("Gateway of India", new String[]{
            "Iconic basalt arch built during the British era overlooking the Arabian Sea.",
            "24 hours", "Free", "4.4"});
        details.put("Ajanta & Ellora Caves", new String[]{
            "Ancient rock-cut Buddhist, Jain and Hindu cave complexes with exquisite carvings.",
            "09:00 - 17:00", "₹150", "4.9"});
        details.put("Shirdi", new String[]{
            "Pilgrimage town centered on Sai Baba temple attracting millions yearly.",
            "05:00 - 22:00", "Free", "4.3"});
        details.put("Sanjay Gandhi National Park", new String[]{
            "Large protected park within Mumbai with safari and Kanheri caves.",
            "07:00 - 18:00", "₹100", "4.1"});
        
        // Delhi / NCR
        details.put("Red Fort", new String[]{
            "Mughal-era fort and UNESCO site used for India's Republic Day celebrations.",
            "09:30 - 16:30", "₹50", "4.5"});
        details.put("Qutub Minar", new String[]{
            "Tall minaret from the Delhi Sultanate era surrounded by historical ruins.",
            "07:00 - 17:00", "₹200", "4.4"});
        details.put("India Gate", new String[]{
            "War memorial and popular lawn space with evening crowds.",
            "24 hours", "Free", "4.3"});
        details.put("Humayun's Tomb", new String[]{
            "Precursor to the Taj Mahal — splendid Mughal garden tomb.",
            "06:00 - 18:00", "₹120", "4.5"});
        details.put("Connaught Place", new String[]{
            "Colonial-era commercial hub with shops, cafes and arcades.",
            "10:00 - 22:00", "₹300", "4.2"});
        
        // Kerala
        details.put("Alleppey Backwaters", new String[]{
            "Serene canals and houseboat cruises through traditional villages.",
            "Sunrise-Sunset", "₹210", "4.8"});
        details.put("Munnar Tea Plantations", new String[]{
            "Rolling hills and tea estates with cool climate and viewpoints.",
            "06:00 - 18:00", "₹300", "4.6"});
        details.put("Kovalam Beach", new String[]{
            "Popular coastal destination known for lighthouse and crescent beaches.",
            "06:00 - 19:00", "Free", "4.1"});
        details.put("Periyar Wildlife Sanctuary", new String[]{
            "Wildlife reserve with boat safaris and elephant sightings.",
            "06:00 - 18:00", "₹120", "4.5"});
        
        // West Bengal
        details.put("Victoria Memorial", new String[]{
            "White-marble museum and Kolkata landmark surrounded by gardens.",
            "10:00 - 17:00", "₹230", "4.4"});
        details.put("Sundarbans National Park", new String[]{
            "Largest mangrove forest, famous for Royal Bengal tigers and waterways.",
            "Boat tours (seasonal)", "₹300", "4.6"});
        details.put("Howrah Bridge", new String[]{
            "Iconic cantilever bridge over the Hooghly River, symbol of Kolkata.",
            "24 hours", "Free", "4.2"});
        details.put("Dakshineswar Kali Temple", new String[]{
            "Popular 19th-century Hindu temple on the Hooghly's eastern bank.",
            "05:00 - 22:00", "Free", "4.3"});
        
        // Rajasthan
        details.put("Amber Fort", new String[]{
            "Hilltop fort near Jaipur with palatial halls and elephant rides.",
            "08:00 - 17:30", "₹410", "4.5"});
        details.put("City Palace (Udaipur)", new String[]{
            "Lavish lakeside palace complex with museums and courtyards.",
            "09:30 - 17:30", "₹200", "4.6"});
        details.put("Jaisalmer Fort", new String[]{
            "Living fort of yellow sandstone in the Thar Desert with markets inside.",
            "08:00 - 18:00", "₹120", "4.4"});
        details.put("Mehrangarh Fort", new String[]{
            "Massive hill fort in Jodhpur with sweeping views and galleries.",
            "09:00 - 17:30", "₹150", "4.7"});
        
        // Uttar Pradesh
        details.put("Taj Mahal (Agra)", new String[]{
            "World-famous white-marble mausoleum, UNESCO World Heritage site.",
            "Sunrise-Sunset (closed Fri)", "₹160", "4.9"});
        details.put("Varanasi Ghats", new String[]{
            "Ancient riverfront steps on the Ganges with ritual bathing and aarti.",
            "All day", "Free", "4.6"});
        details.put("Sarnath", new String[]{
            "Important Buddhist pilgrimage site near Varanasi where Buddha taught first sermon.",
            "07:00 - 17:00", "₹100", "4.4"});
        details.put("Fatehpur Sikri", new String[]{
            "Well-preserved Mughal city and UNESCO site built by Akbar.",
            "09:00 - 17:00", "₹135", "4.5"});
        
        // Telangana
        details.put("Charminar", new String[]{
            "16th-century monument and busy market landmark of Hyderabad.",
            "09:30 - 17:30", "Free/Market", "4.3"});
        details.put("Golconda Fort", new String[]{
            "Historic fort with acoustic features and royal halls.",
            "09:00 - 17:00", "₹150", "4.5"});
        details.put("Ramoji Film City", new String[]{
            "Large film studio and theme park offering guided tours.",
            "09:00 - 17:30", "₹120", "4.2"});
        details.put("Hussain Sagar Lake", new String[]{
            "Large heart-shaped lake with Buddha statue and lakeside attractions.",
            "All day", "Free", "4.0"});
        
        // Gujarat
        details.put("Sabarmati Ashram", new String[]{
            "Historic residence of Mahatma Gandhi and museum of the independence movement.",
            "08:00 - 18:00", "Free", "4.3"});
        details.put("Rann of Kutch", new String[]{
            "Vast white salt marsh famed for seasonal festivals and surreal landscapes.",
            "Best visited Nov-Feb", "₹130", "4.7"});
        details.put("Gir National Park", new String[]{
            "Only place to see Asiatic lions in the wild with guided safaris.",
            "06:00 - 18:00 (safaris)", "₹200", "4.6"});
        details.put("Somnath Temple", new String[]{
            "Ancient and revered coastal shrine with historic reconstructions.",
            "05:00 - 22:00", "Free", "4.4"});
        
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
