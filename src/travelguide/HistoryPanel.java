package travelguide;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class HistoryPanel extends BasePanel {
    
    public HistoryPanel(SmartTravelCompanion parent) {
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
        
        add(createHeaderSection("History & Culture", 
            "Discover the rich heritage of " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        CityData.CityInfo city = CityData.getCity(parent.getCurrentCity());
        Map<String, Object> historyInfo = getHistoryInfo(parent.getCurrentCity());
        
        JPanel overviewCard = createCard("Historical Overview");
        overviewCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JLabel historyText = new JLabel("<html><p style='width: 700px; line-height: 1.6;'>" + 
            city.history + "<br><br>" + historyInfo.get("overview") + "</p></html>");
        historyText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyText.setForeground(Color.BLACK);
        
        overviewCard.add(historyText, BorderLayout.CENTER);
        content.add(overviewCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel timelineCard = createCard("Historical Timeline");
        JPanel timelineContent = new JPanel();
        timelineContent.setLayout(new BoxLayout(timelineContent, BoxLayout.Y_AXIS));
        timelineContent.setOpaque(false);
        
        String[][] timeline = (String[][]) historyInfo.get("timeline");
        for (String[] event : timeline) {
            timelineContent.add(createTimelineItem(event[0], event[1]));
        }
        
        timelineCard.add(timelineContent, BorderLayout.CENTER);
        content.add(timelineCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel cultureCard = createCard("Culture & Traditions");
        JPanel cultureContent = new JPanel(new GridLayout(2, 2, 15, 15));
        cultureContent.setOpaque(false);
        
        String[][] culture = (String[][]) historyInfo.get("culture");
        for (String[] item : culture) {
            cultureContent.add(createCultureCard(item[0], item[1], item[2]));
        }
        
        cultureCard.add(cultureContent, BorderLayout.CENTER);
        content.add(cultureCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel factsCard = createCard("Interesting Facts");
        JPanel factsContent = new JPanel();
        factsContent.setLayout(new BoxLayout(factsContent, BoxLayout.Y_AXIS));
        factsContent.setOpaque(false);
        
        String[] facts = (String[]) historyInfo.get("facts");
        for (String fact : facts) {
            JPanel factRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            factRow.setOpaque(false);
            JLabel bullet = new JLabel("📌");
            JLabel factLabel = new JLabel(fact);
            factLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            factRow.add(bullet);
            factRow.add(factLabel);
            factsContent.add(factRow);
        }
        
        factsCard.add(factsContent, BorderLayout.CENTER);
        content.add(factsCard);
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createTimelineItem(String year, String event) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        JLabel yearLabel = new JLabel(year);
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        yearLabel.setForeground(SmartTravelCompanion.getPrimaryColor());
        yearLabel.setPreferredSize(new Dimension(80, 25));
        
        JPanel dot = new JPanel();
        dot.setBackground(SmartTravelCompanion.getPrimaryColor());
        dot.setPreferredSize(new Dimension(12, 12));
        dot.setBorder(new LineBorder(Color.WHITE, 2));
        
        JLabel eventLabel = new JLabel(event);
        eventLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        eventLabel.setForeground(Color.BLACK);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(yearLabel);
        leftPanel.add(dot);
        
        item.add(leftPanel, BorderLayout.WEST);
        item.add(eventLabel, BorderLayout.CENTER);
        
        return item;
    }
    
    private JPanel createCultureCard(String title, String icon, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><p style='width: 200px;'>" + description + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.BLACK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
    
    private Map<String, Object> getHistoryInfo(String city) {
        Map<String, Object> info = new HashMap<>();
        
        switch (city) {
            case "Karnataka":
                info.put("overview", "Karnataka's history spans the Kadamba, Chalukya and Vijayanagara empires to modern Bangalore's technology-led growth.");
                info.put("timeline", new String[][]{
                    {"4th-6th C", "Kadamba and early dynasties establish local kingdoms"},
                    {"6th-12th C", "Badami Chalukyas and Rashtrakutas rule large parts of Deccan"},
                    {"14th-16th C", "Vijayanagara Empire flourishes (Hampi)"},
                    {"18th-19th C", "Mysore under Wodeyars and Tipu Sultan"},
                    {"20th-21st C", "Bengaluru emerges as India's IT hub"}
                });
                info.put("culture", new String[][]{
                    {"Cuisine", "🍛", "Rich Kannada cuisine with dosas, idlis, ragi-based dishes and Mysore pak."},
                    {"Arts", "🎭", "Yakshagana and Carnatic music traditions in south Karnataka."},
                    {"Architecture", "🏛️", "Hoysala temples and Vijayanagara ruins showcase intricate stone work."},
                    {"Language", "🗣️", "Kannada literature and films are integral to cultural identity."}
                });
                info.put("facts", new String[]{
                    "Hampi was the seat of the Vijayanagara Empire and is a UNESCO World Heritage site.",
                    "Bengaluru is often called the Silicon Valley of India.",
                    "Mysore is famous for its Dasara festival and Mysore silk."
                });
                break;
            case "Tamil Nadu":
                info.put("overview", "Tamil Nadu is known for its ancient Dravidian temple architecture, classical arts and a continuous cultural heritage stretching millennia.");
                info.put("timeline", new String[][]{
                    {"3rd C BCE", "Early Sangam age and Tamil kingdoms"},
                    {"9th-13th C", "Chola empire's maritime power and temple building"},
                    {"16th-18th C", "Nayak rulers patronize arts in Madurai and Thanjavur"},
                    {"19th-20th C", "Modernization and Chennai's rise as regional hub"}
                });
                info.put("culture", new String[][]{
                    {"Temples", "🏯", "Grand gopurams and temple festivals define local life."},
                    {"Music", "🎶", "Carnatic music and Bharatanatyam dance are central traditions."},
                    {"Cuisine", "🍽️", "Rice-based meals, Chettinad and coastal seafood specialties."},
                    {"Language", "🗣️", "Tamil is one of the world's oldest classical languages."}
                });
                info.put("facts", new String[]{
                    "Brihadeeswarar Temple in Thanjavur is a UNESCO site built by the Cholas.",
                    "Tamil literature has works dating back over two thousand years."
                });
                break;
            case "Maharashtra":
                info.put("overview", "Maharashtra blends ancient Buddhist heritage, medieval forts and a modern commercial powerhouse centered in Mumbai.");
                info.put("timeline", new String[][]{
                    {"3rd C BCE", "Buddhist sites and Satavahana influence"},
                    {"17th C", "Maratha Empire rises under Shivaji"},
                    {"19th-20th C", "Mumbai develops under colonial trade and industry"},
                    {"21st C", "State as a commercial and cultural leader"}
                });
                info.put("culture", new String[][]{
                    {"Festivals", "🎉", "Ganesh Chaturthi is celebrated with great fervor."},
                    {"Music", "🎵", "Lavani folk and Marathi theatre are key art forms."},
                    {"Cuisine", "🌶️", "Varies from Konkan seafood to spicy Malvani dishes."},
                    {"Film", "🎬", "Mumbai is the center of Bollywood"}
                });
                info.put("facts", new String[]{
                    "Ajanta & Ellora caves contain exceptional ancient rock-cut art.",
                    "Mumbai is India's financial capital and a major port city."
                });
                break;
            case "Delhi":
                info.put("overview", "Delhi's history weaves together ancient kingdoms, Mughal and colonial eras, and its role now as India's political capital.");
                info.put("timeline", new String[][]{
                    {"6th C", "Early settlements and the rise of medieval cities"},
                    {"12th-17th C", "Delhi Sultanate and Mughal rule shape the city's monuments"},
                    {"19th-20th C", "British colonial capital and modern urbanization"},
                    {"1947", "Becomes capital of independent India"}
                });
                info.put("culture", new String[][]{
                    {"Monuments", "🏛️", "Red Fort, Qutub Minar and Humayun's Tomb reflect layered history."},
                    {"Cuisine", "🍢", "Mughlai and street food traditions are prominent."},
                    {"Markets", "🛍️", "Historic bazaars like Chandni Chowk are cultural landmarks."},
                    {"Language", "🗣️", "Hindi and Urdu have deep literary histories here."}
                });
                info.put("facts", new String[]{
                    "Delhi has seen waves of rulers and served as a seat of multiple empires.",
                    "India Gate is a major national monument commemorating soldiers."
                });
                break;
            case "Kerala":
                info.put("overview", "Kerala's maritime trade, spice heritage and unique social traditions make it distinct in India's cultural mosaic.");
                info.put("timeline", new String[][]{
                    {"Early CE", "Trade links with Romans and Arabs begin"},
                    {"14th-18th C", "Local kingdoms and European trading posts emerge"},
                    {"20th C", "Social reforms and modern state formation"},
                    {"Present", "Popular tourist destination known for backwaters and Ayurveda"}
                });
                info.put("culture", new String[][]{
                    {"Performing Arts", "🎭", "Kathakali and Mohiniyattam are iconic dance forms."},
                    {"Cuisine", "🍛", "Coconut and seafood-based dishes are regional staples."},
                    {"Festivals", "🎉", "Onam is a major statewide festival."},
                    {"Ayurveda", "🌿", "Long tradition of Ayurvedic medicine and wellness tourism"}
                });
                info.put("facts", new String[]{
                    "Kerala has one of the highest literacy rates in India.",
                    "Backwater houseboat cruises are a signature tourist experience."
                });
                break;
            case "West Bengal":
                info.put("overview", "West Bengal is a cultural hub with strong traditions in literature, music, festivals and colonial-era architecture centered on Kolkata.");
                info.put("timeline", new String[][]{
                    {"Ancient", "Bengal region part of Maurya and later empires"},
                    {"Medieval", "Bengal Sultanate and Mughal influences"},
                    {"18th-20th C", "Calcutta becomes British India's commercial capital"},
                    {"20th C", "Bengali renaissance and nationalist movements"}
                });
                info.put("culture", new String[][]{
                    {"Literature", "📚", "Home to Rabindranath Tagore and a rich literary tradition."},
                    {"Cuisine", "🍲", "Bengali fish curries, sweets like rosogolla are famous."},
                    {"Festivals", "🎉", "Durga Puja is a major cultural event."},
                    {"Arts", "🎨", "Theatre, music and painting flourish here."}
                });
                info.put("facts", new String[]{
                    "Victoria Memorial is a major Kolkata landmark and museum.",
                    "Sundarbans is the largest mangrove forest in the world."
                });
                break;
            case "Rajasthan":
                info.put("overview", "Rajasthan's history of Rajput kingdoms left a legacy of forts, palaces and vibrant folk traditions across the desert landscape.");
                info.put("timeline", new String[][]{
                    {"7th-12th C", "Rajput principalities emerge across the region"},
                    {"16th-18th C", "Glorious palace and fort architecture flourishes"},
                    {"19th-20th C", "Princely states integrate into modern India"}
                });
                info.put("culture", new String[][]{
                    {"Architecture", "🏰", "Forts and palaces like Amber and Mehrangarh define the skyline."},
                    {"Music & Dance", "💃", "Ghoomar and folk music are integral to local festivals."},
                    {"Crafts", "🧵", "Textiles, block printing and jewelry-making are well-known."},
                    {"Cuisine", "🍖", "Hearty Rajasthani thalis and sweets are specialties."}
                });
                info.put("facts", new String[]{
                    "Rajasthan hosts many large cultural fairs and camel festivals.",
                    "The desert city of Jaisalmer is famed for its living fort."
                });
                break;
            case "Uttar Pradesh":
                info.put("overview", "Uttar Pradesh is a cradle of ancient Indian civilization and religion, home to the Taj Mahal and many pilgrimage sites.");
                info.put("timeline", new String[][]{
                    {"Ancient", "Gangetic plains cradle early Vedic and later civilizations"},
                    {"Medieval", "Majority of Mughal-era historic monuments constructed"},
                    {"Modern", "Important role in India's political and cultural history"}
                });
                info.put("culture", new String[][]{
                    {"Pilgrimage", "🕉️", "Varanasi and Sarnath are key religious centers."},
                    {"Architecture", "🏛️", "Mughal monuments like the Taj Mahal are world-renowned."},
                    {"Cuisine", "🍛", "Awadhi and Mughlai traditions known for rich kebabs and biryani."},
                    {"Festivals", "🎉", "Important Hindu and Islamic festivals observed widely."}
                });
                info.put("facts", new String[]{
                    "The Taj Mahal is one of the New Seven Wonders of the World.",
                    "Varanasi is among the world's oldest continuously inhabited cities."
                });
                break;
            case "Telangana":
                info.put("overview", "Telangana blends Nizam-era heritage with Hyderabad's modern IT economy and famous cuisine.");
                info.put("timeline", new String[][]{
                    {"Medieval", "Kakatiya dynasty and later Qutb Shahi rule shape the region"},
                    {"18th-20th C", "Nizam of Hyderabad rules a princely state", 
                        "Hyderabad integrates into India in 1948"},
                });
                info.put("culture", new String[][]{
                    {"Cuisine", "🍛", "Hyderabadi biryani and rich Nizam culinary traditions."},
                    {"Architecture", "🏯", "Charminar and Golconda fort reflect regional styles."},
                    {"Language", "🗣️", "Telugu and Urdu influence local culture."},
                    {"Film", "🎬", "Telugu cinema is a major cultural industry"}
                });
                info.put("facts", new String[]{
                    "Hyderabad is nicknamed 'City of Pearls' historically for its pearl trade.",
                    "Ramoji Film City is one of the largest film studio complexes in the world."
                });
                break;
            case "Gujarat":
                info.put("overview", "Gujarat's maritime and trade history, Indus Valley links and vibrant handicrafts make it a unique cultural region.");
                info.put("timeline", new String[][]{
                    {"Ancient", "Sites connected to the Indus Valley civilization"},
                    {"Medieval", "Maritime trade with Arabian and African coasts"},
                    {"Modern", "Industrial and cultural growth; birthplace of Mahatma Gandhi's Sabarmati Ashram"}
                });
                info.put("culture", new String[][]{
                    {"Dance", "💃", "Garba and Dandiya are famous folk dances."},
                    {"Crafts", "🧵", "Textiles, bandhani and embroidery are traditional crafts."},
                    {"Cuisine", "🍲", "Distinctive Gujarati thali and snacks."},
                    {"Festivals", "🎉", "Navratri celebrations are among the largest in India."}
                });
                info.put("facts", new String[]{
                    "Rann of Kutch hosts a major cultural festival and seasonal salt marshes.",
                    "Gujarat played a key role in India's independence movement through Gandhi's activism."
                });
                break;
            default:
                info.put("overview", "This region has a rich and varied history. Explore local museums, monuments and cultural programs to learn more.");
                info.put("timeline", new String[][]{
                    {"Ancient", "Early settlements and trade routes"},
                    {"Medieval", "Growth of regional kingdoms"},
                    {"Modern", "Integration into the modern nation-state"}
                });
                info.put("culture", new String[][]{
                    {"Cuisine", "🍽️", "Local culinary specialties"},
                    {"Arts", "🎭", "Regional music and dance"},
                    {"Traditions", "🎉", "Local festivals and fairs"},
                    {"Architecture", "🏛️", "Blend of historic and modern styles"}
                });
                info.put("facts", new String[]{
                    "Local museums hold key insights into the region's past.",
                    "Traditional festivals reflect centuries-old customs."
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
