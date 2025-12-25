package travelguide;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class TransportPanel extends BasePanel {
    
    public TransportPanel(SmartTravelCompanion parent) {
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
        
        add(createHeaderSection("Transportation", 
            "Getting around in " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        Map<String, String[][]> transportInfo = getTransportInfo(parent.getCurrentCity());
        
        JPanel airportCard = createCard("Airports");
        JPanel airportContent = new JPanel(new GridLayout(0, 1, 0, 10));
        airportContent.setOpaque(false);
        
        for (String[] airport : transportInfo.get("airports")) {
            airportContent.add(createTransportItem(airport[0], airport[1], airport[2], "✈️"));
        }
        
        airportCard.add(airportContent, BorderLayout.CENTER);
        content.add(airportCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel publicCard = createCard("Public Transportation");
        JPanel publicContent = new JPanel(new GridLayout(0, 2, 15, 10));
        publicContent.setOpaque(false);
        
        for (String[] transport : transportInfo.get("public")) {
            publicContent.add(createTransportCard(transport[0], transport[1], transport[2], transport[3]));
        }
        
        publicCard.add(publicContent, BorderLayout.CENTER);
        content.add(publicCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel taxiCard = createCard("Taxi & Ride Services");
        JPanel taxiContent = new JPanel(new GridLayout(0, 3, 15, 10));
        taxiContent.setOpaque(false);
        
        for (String[] taxi : transportInfo.get("taxi")) {
            taxiContent.add(createServiceCard(taxi[0], taxi[1], taxi[2]));
        }
        
        taxiCard.add(taxiContent, BorderLayout.CENTER);
        content.add(taxiCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel tipsCard = createCard("Transport Tips");
        JPanel tipsContent = new JPanel();
        tipsContent.setLayout(new BoxLayout(tipsContent, BoxLayout.Y_AXIS));
        tipsContent.setOpaque(false);
        
        String[] tips = transportInfo.get("tips")[0];
        for (String tip : tips) {
            JPanel tipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
            tipRow.setOpaque(false);
            JLabel bullet = new JLabel("💡");
            JLabel tipLabel = new JLabel(tip);
            tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tipRow.add(bullet);
            tipRow.add(tipLabel);
            tipsContent.add(tipRow);
        }
        
        tipsCard.add(tipsContent, BorderLayout.CENTER);
        content.add(tipsCard);
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createTransportItem(String name, String code, String distance, String icon) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        JLabel iconLabel = new JLabel(icon + " " + name + " (" + code + ")");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        iconLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        
        JLabel distLabel = new JLabel(distance + " from city center");
        distLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        distLabel.setForeground(Color.BLACK);
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(distLabel, BorderLayout.EAST);
        
        return item;
    }
    
    private JPanel createTransportCard(String type, String icon, String info, String price) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 35));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html><center>" + info + "</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(Color.BLACK);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLabel.setForeground(SmartTravelCompanion.getAccentColor());
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(infoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(priceLabel);
        
        return card;
    }
    
    private JPanel createServiceCard(String name, String type, String availability) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(Color.BLACK);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel availLabel = new JLabel(availability);
        availLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        availLabel.setForeground(SmartTravelCompanion.getAccentColor());
        availLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(availLabel);
        
        return card;
    }
    
    private Map<String, String[][]> getTransportInfo(String city) {
        Map<String, String[][]> info = new HashMap<>();
        
        switch (city) {
            case "Karnataka":
                info.put("airports", new String[][]{
                    {"Kempegowda International Airport (Bengaluru)", "BLR", "35 km"}
                });
                info.put("public", new String[][]{
                    {"Metro (Bengaluru)", "🚇", "Namma Metro - expanding network", "₹30-60"},
                    {"Bus (BMTC)", "🚌", "Extensive city bus network", "₹10-50"},
                    {"Suburban rail", "🚆", "Connects outskirts and satellite towns", "₹10-40"}
                });
                info.put("taxi", new String[][]{
                    {"Namma Taxi / Ola", "Ride-sharing", "App-based, 24/7"},
                    {"Uber", "Ride-sharing", "App-based, 24/7"},
                    {"City Taxis", "Radio taxi", "Metered"}
                });
                info.put("tips", new String[][]{
                    {"Use Namma Metro for predictable travel times", "Book cabs via apps and check surge pricing", "Avoid peak office hours on major roads"}
                });
                break;
            case "Tamil Nadu":
                info.put("airports", new String[][]{
                    {"Chennai International Airport", "MAA", "18 km"},
                    {"Tiruchirappalli International Airport", "TRZ", "6 km (for central TN)"}
                });
                info.put("public", new String[][]{
                    {"Chennai Metro", "🚇", "Rapid transit in Chennai", "₹20-50"},
                    {"State buses (TNSTC)", "🚌", "Intercity and city services", "₹10-100"},
                    {"Suburban EMU", "🚆", "Local trains in metro region", "₹5-30"}
                });
                info.put("taxi", new String[][]{
                    {"Ola / Uber", "Ride-sharing", "Widespread availability"},
                    {"Prepaid taxis", "Airport counters", "Fixed fares"}
                });
                info.put("tips", new String[][]{
                    {"In Chennai T. Nagar and Central are busy — allow extra time", "Use prepaid counters at airports for safety"}
                });
                break;
            case "Maharashtra":
                info.put("airports", new String[][]{
                    {"Chhatrapati Shivaji Maharaj International Airport (Mumbai)", "BOM", "20 km"},
                    {"Pune Airport", "PNQ", "10 km"},
                    {"Aurangabad (for Ajanta/Ellora)", "IXU", "13 km"}
                });
                info.put("public", new String[][]{
                    {"Mumbai Suburban Rail", "🚆", "Fastest way across Mumbai", "₹10-50"},
                    {"BEST buses", "🚌", "Extensive city network", "₹10-50"},
                    {"Metro (Mumbai/Pune)", "🚇", "New metro corridors", "₹20-80"}
                });
                info.put("taxi", new String[][]{
                    {"Black & Yellow Taxis", "Metered", "Available"},
                    {"Ola / Uber", "Ride-sharing", "24/7"},
                    {"Radio cabs", "App/phone booking", "Reliable"}
                });
                info.put("tips", new String[][]{
                    {"Prefer local trains for long distances in Mumbai", "Avoid peak suburban hours if possible"}
                });
                break;
            case "Delhi":
                info.put("airports", new String[][]{
                    {"Indira Gandhi International Airport", "DEL", "16 km"}
                });
                info.put("public", new String[][]{
                    {"Delhi Metro", "🚇", "Extensive and efficient network", "₹10-50"},
                    {"DTC buses", "🚌", "Large city bus network", "₹10-50"},
                    {"Rapid Rail (RRTS)", "🚆", "Regional rapid transit (coming online)", "Varies"}
                });
                info.put("taxi", new String[][]{
                    {"App cabs (Ola/Uber)", "Ride-sharing", "Widespread"},
                    {"Yellow/Black taxis", "Metered", "Book via app or at stands"}
                });
                info.put("tips", new String[][]{
                    {"Use Metro for most inner-city travel", "Check peak hour timings for congestion"}
                });
                break;
            case "Kerala":
                info.put("airports", new String[][]{
                    {"Cochin International Airport (Kochi)", "COK", "25 km"},
                    {"Trivandrum International Airport (Thiruvananthapuram)", "TRV", "7 km"}
                });
                info.put("public", new String[][]{
                    {"KSRTC buses", "🚌", "State-run buses with intercity routes", "₹10-200"},
                    {"Local ferries/boats", "⛴", "Backwater connectivity in Alleppey/Kumarakom", "Varies"},
                    {"Local buses", "🚌", "Connect towns and hill stations", "₹10-100"}
                });
                info.put("taxi", new String[][]{
                    {"App taxis (Ola/Uber)", "Ride-sharing", "Available in cities"},
                    {"Local taxis", "Metered/negotiated", "Useful in rural areas"}
                });
                info.put("tips", new String[][]{
                    {"Use boats for a scenic backwater experience", "Plan travel between hill stations carefully during monsoon"}
                });
                break;
            case "West Bengal":
                info.put("airports", new String[][]{
                    {"Netaji Subhash Chandra Bose International Airport (Kolkata)", "CCU", "17 km"}
                });
                info.put("public", new String[][]{
                    {"Kolkata Metro", "🚇", "India's oldest metro system", "₹10-30"},
                    {"Trams", "🚋", "Heritage tram network", "Low fare"},
                    {"WBTC buses", "🚌", "City and suburban services", "₹10-50"}
                });
                info.put("taxi", new String[][]{
                    {"App taxis (Ola/Uber)", "Ride-sharing", "Available"},
                    {"Yellow taxis", "Metered", "Common in city"}
                });
                info.put("tips", new String[][]{
                    {"Try tram/river ferry for a different perspective of Kolkata", "Expect peak-hour congestion in central areas"}
                });
                break;
            case "Rajasthan":
                info.put("airports", new String[][]{
                    {"Jaipur International Airport", "JAI", "13 km"},
                    {"Udaipur Airport", "UDR", "22 km"},
                    {"Jodhpur Airport", "JDH", "5 km"}
                });
                info.put("public", new String[][]{
                    {"State buses (RSRTC)", "🚌", "Intercity links", "₹50-500"},
                    {"Local buses & autos", "🚌/🛺", "Convenient for short trips", "Varies"}
                });
                info.put("taxi", new String[][]{
                    {"Private cabs", "Tour packages", "Prepaid/negotiated"},
                    {"Ola / Uber (in major cities)", "Ride-sharing", "Available in Jaipur/Udaipur"}
                });
                info.put("tips", new String[][]{
                    {"Desert drives can be long — pack water and sunscreen", "Book intercity cab for early morning/late night travel"}
                });
                break;
            case "Uttar Pradesh":
                info.put("airports", new String[][]{
                    {"Indira Gandhi International (for Delhi access)", "DEL", "200 km (varies)"},
                    {"Agra Airport", "AGR", "12 km"},
                    {"Varanasi Airport", "VNS", "26 km"},
                    {"Lucknow Amausi Airport", "LKO", "12 km"}
                });
                info.put("public", new String[][]{
                    {"Intercity trains", "🚆", "Extensive rail connectivity between historic sites", "Varies"},
                    {"State buses", "🚌", "Connect towns and cities", "₹20-300"}
                });
                info.put("taxi", new String[][]{
                    {"Local taxis & autos", "Metered/negotiated", "Available"},
                    {"App cabs in big cities", "Ola/Uber", "Available in major cities"}
                });
                info.put("tips", new String[][]{
                    {"Visit major sites early morning to avoid crowds", "Prefer car transfers between heritage sites for flexibility"}
                });
                break;
            case "Telangana":
                info.put("airports", new String[][]{
                    {"Rajiv Gandhi International Airport (Hyderabad)", "HYD", "24 km"}
                });
                info.put("public", new String[][]{
                    {"Hyderabad Metro", "🚇", "Covers major corridors", "₹20-60"},
                    {"TSRTC buses", "🚌", "State-run network", "₹10-80"},
                    {"MMTS local trains", "🚆", "Suburban rail", "₹5-30"}
                });
                info.put("taxi", new String[][]{
                    {"Ola / Uber", "Ride-sharing", "Widespread"},
                    {"Meru / FastTrack", "Radio cab", "Reliable"}
                });
                info.put("tips", new String[][]{
                    {"Golconda and old city areas get crowded — allow time", "Use metro where available for consistent travel times"}
                });
                break;
            case "Gujarat":
                info.put("airports", new String[][]{
                    {"Sardar Vallabhbhai Patel International Airport (Ahmedabad)", "AMD", "14 km"},
                    {"Kutch / Bhuj (for Rann)", "BHJ", "8 km"}
                });
                info.put("public", new String[][]{
                    {"GSRTC buses", "🚌", "Good connectivity across state", "₹20-400"},
                    {"Local buses & TRAINS", "🚆", "Intercity rail network", "Varies"}
                });
                info.put("taxi", new String[][]{
                    {"App taxis (Ola/Uber)", "Ride-sharing", "Available in major cities"},
                    {"Local taxis", "Metered/negotiated", "Common outside metros"}
                });
                info.put("tips", new String[][]{
                    {"Rann visits best between Nov-Feb", "Plan long drives in Gujarat with stops due to desert stretches"}
                });
                break;
            default:
                info.put("airports", new String[][]{
                    {"Regional Airport", "REG", "20 km"}
                });
                info.put("public", new String[][]{
                    {"Local buses", "🚌", "City network", "Varies"},
                    {"Trains", "🚆", "Intercity connections", "Varies"}
                });
                info.put("taxi", new String[][]{
                    {"Local Taxi", "Official taxi", "24/7"},
                    {"App cabs", "Ride-sharing", "Available in urban areas"}
                });
                info.put("tips", new String[][]{
                    {"Check local transport schedules", "Use apps for reliable taxi estimates"}
                });
        }
        
        return info;
    }
    
    @Override
    public void updateData() {
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
