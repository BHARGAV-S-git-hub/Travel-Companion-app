package travelguide;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class ShoppingPanel extends BasePanel {
    
    public ShoppingPanel(SmartTravelCompanion parent) {
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
        
        add(createHeaderSection("Shopping Centers & Markets", 
            "Best shopping destinations in " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        Map<String, java.util.List<String[]>> shoppingPlaces = getShoppingPlaces(parent.getCurrentCity());
        
        JPanel mallsCard = createCard("Shopping Malls & Centers");
        JPanel mallsContent = new JPanel();
        mallsContent.setLayout(new BoxLayout(mallsContent, BoxLayout.Y_AXIS));
        mallsContent.setOpaque(false);
        
        for (String[] mall : shoppingPlaces.get("malls")) {
            mallsContent.add(createShoppingItem(mall[0], mall[1], mall[2], "🏬"));
            mallsContent.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        mallsCard.add(mallsContent, BorderLayout.CENTER);
        content.add(mallsCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel marketsCard = createCard("Local Markets & Bazaars");
        JPanel marketsContent = new JPanel();
        marketsContent.setLayout(new BoxLayout(marketsContent, BoxLayout.Y_AXIS));
        marketsContent.setOpaque(false);
        
        for (String[] market : shoppingPlaces.get("markets")) {
            marketsContent.add(createShoppingItem(market[0], market[1], market[2], "🛒"));
            marketsContent.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        marketsCard.add(marketsContent, BorderLayout.CENTER);
        content.add(marketsCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel luxuryCard = createCard("Luxury Shopping Districts");
        JPanel luxuryContent = new JPanel();
        luxuryContent.setLayout(new BoxLayout(luxuryContent, BoxLayout.Y_AXIS));
        luxuryContent.setOpaque(false);
        
        for (String[] luxury : shoppingPlaces.get("luxury")) {
            luxuryContent.add(createShoppingItem(luxury[0], luxury[1], luxury[2], "💎"));
            luxuryContent.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        luxuryCard.add(luxuryContent, BorderLayout.CENTER);
        content.add(luxuryCard);
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createShoppingItem(String name, String description, String hours, String icon) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        iconLabel.setPreferredSize(new Dimension(50, 50));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel hoursLabel = new JLabel("🕐 " + hours);
        hoursLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hoursLabel.setForeground(Color.BLACK);
        hoursLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(hoursLabel);
        
       // JButton viewBtn = createStyledButton("View", SmartTravelCompanion.getPrimaryColor());
        JButton addToTripBtn = createStyledButton("Add to Trip", SmartTravelCompanion.getAccentColor());
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        JPanel rightBtns = new JPanel(new GridLayout(0,1,0,6));
        rightBtns.setOpaque(false);
        //rightBtns.add(viewBtn);
        rightBtns.add(addToTripBtn);
        item.add(rightBtns, BorderLayout.EAST);
        
        addToTripBtn.addActionListener(ev -> {
            String dateStr = JOptionPane.showInputDialog(this, "Enter date to visit/shop (YYYY-MM-DD):", LocalDate.now().toString());
            LocalDate date = null;
            try { if (dateStr != null && !dateStr.trim().isEmpty()) date = LocalDate.parse(dateStr.trim()); } catch (Exception ex) { date = null; }
            MyTrips.TripItem itemObj = new MyTrips.TripItem("Shopping", name, date, description);
            MyTrips.showAddToTripDialog(this, itemObj);
        });
        
        return item;
    }
    
    private Map<String, java.util.List<String[]>> getShoppingPlaces(String city) {
        Map<String, java.util.List<String[]>> places = new HashMap<>();
        
        switch (city) {
            case "Karnataka":
                places.put("malls", Arrays.asList(
                    new String[]{"Phoenix Marketcity (Bengaluru)", "Large mall with international brands", "10:00 - 22:00"},
                    new String[]{"UB City", "Luxury shopping and dining complex", "10:00 - 21:00"},
                    new String[]{"Brigade Road / Commercial Street", "Popular retail and fashion streets", "10:00 - 21:00"}
                ));
                places.put("markets", Arrays.asList(
                    new String[]{"Commercial Street", "Bargain shopping for textiles and trinkets", "10:00 - 21:00"},
                    new String[]{"Russell Market", "Historic market for fresh produce and spices", "07:00 - 19:00"}
                ));
                places.put("luxury", Arrays.asList(
                    new String[]{"UB City High-end Stores", "Designer boutiques and fine jewelry", "Varies by store"},
                    new String[]{"MG Road Luxury Boutiques", "Upscale shopping options", "10:00 - 20:00"}
                ));
                break;
            case "Tamil Nadu":
                places.put("malls", Arrays.asList(
                    new String[]{"Phoenix Marketcity Chennai", "Large mall with entertainment and dining", "10:00 - 22:00"},
                    new String[]{"Express Avenue", "Major shopping and cinema complex", "10:00 - 21:30"},
                    new String[]{"Forum Vijaya Mall", "Popular family mall", "10:00 - 21:30"}
                ));
                places.put("markets", Arrays.asList(
                    new String[]{"T. Nagar", "Chennai's premier shopping district for textiles and jewelry", "10:00 - 21:00"},
                    new String[]{"Pondy Bazaar", "Street shopping and local stalls", "10:00 - 21:00"}
                ));
                places.put("luxury", Arrays.asList(
                    new String[]{"Palladium (Chennai outlets)", "Luxury retail (select stores)", "Varies by store"},
                    new String[]{"Alwarpet boutiques", "Designer stores and ateliers", "10:00 - 19:00"}
                ));
                break;
            case "Maharashtra":
                places.put("malls", Arrays.asList(
                    new String[]{"High Street Phoenix", "Large luxury mall in Mumbai", "11:00 - 21:00"},
                    new String[]{"Palladium", "High-end brands and fine dining", "11:00 - 21:00"},
                    new String[]{"Phoenix Marketcity (Pune)", "Major retail destination in Pune", "10:00 - 22:00"}
                ));
                places.put("markets", Arrays.asList(
                    new String[]{"Colaba Causeway", "Street shopping for souvenirs and fashion", "10:00 - 20:00"},
                    new String[]{"Linking Road", "Bargain clothes and footwear", "10:00 - 21:00"}
                ));
                places.put("luxury", Arrays.asList(
                    new String[]{"Baroda or Mumbai designer stores", "Flagship boutiques and jewelry", "Varies by store"},
                    new String[]{"Kala Ghoda boutiques", "Artisanal and designer stores", "10:00 - 19:00"}
                ));
                break;
            case "Delhi":
                places.put("malls", Arrays.asList(
                    new String[]{"DLF Promenade / Emporio", "Premium malls in Saket", "10:30 - 21:30"},
                    new String[]{"Select Citywalk", "Large open-air shopping and dining hub", "11:00 - 21:30"},
                    new String[]{"Pacific Mall, Subhash Nagar", "Popular neighborhood mall", "10:30 - 21:00"}
                ));
                places.put("markets", Arrays.asList(
                    new String[]{"Chandni Chowk", "Historic bazaar for jewelry, spices and street food", "10:00 - 20:00"},
                    new String[]{"Dilli Haat", "Handicrafts and food stalls from across India", "10:30 - 22:00"}
                ));
                places.put("luxury", Arrays.asList(
                    new String[]{"Designer boutiques in Khan Market", "Upscale stores and bookshops", "10:00 - 20:00"},
                    new String[]{"Connaught Place arcades", "Premium shops and stores", "10:00 - 20:00"}
                ));
                break;
            default:
                places.put("malls", Arrays.asList(
                    new String[]{"Central Mall", "Major shopping destination", "10:00 - 21:00"},
                    new String[]{"City Shopping Center", "Popular retail complex", "10:00 - 20:00"}
                ));
                places.put("markets", Arrays.asList(
                    new String[]{"Local Market", "Traditional market experience", "08:00 - 18:00"},
                    new String[]{"Night Market", "Evening market with street food", "18:00 - 23:00"}
                ));
                places.put("luxury", Arrays.asList(
                    new String[]{"Luxury District", "High-end shopping area", "10:00 - 19:00"},
                    new String[]{"Designer Boutiques", "Premium fashion and accessories", "10:00 - 20:00"}
                ));
        }
        
        return places;
    }
    
    @Override
    public void updateData() {
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
