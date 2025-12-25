package travelguide;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class NewsPanel extends BasePanel {

    private JPanel newsContainer;
    private JComboBox<String> categoryBox;

    public NewsPanel(SmartTravelCompanion parent) {
        super(parent);
    }

    @Override
    protected void initPanel() {
        removeAll();
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // fetch live news for current city at startup
        fetchLiveNews();
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SmartTravelCompanion.getBackgroundColor());

        add(createHeaderSection("Latest News",
            "Stay updated with news from " + parent.getCurrentCity()), BorderLayout.NORTH);

        JPanel filterPanel = createCard(null);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        filterPanel.add(new JLabel("Category:"));
        String[] categories = {"All News", "Travel Updates", "Events", "Local News", "Safety Alerts"};
        categoryBox = new JComboBox<>(categories);
        categoryBox.addActionListener(e -> fetchLiveNews());
        filterPanel.add(categoryBox);

        JButton refreshBtn = createStyledButton("Refresh", SmartTravelCompanion.getPrimaryColor());
        refreshBtn.addActionListener(e -> fetchLiveNews());
        filterPanel.add(refreshBtn);

        JButton sampleBtn = createStyledButton("Use Sample", SmartTravelCompanion.getAccentColor());
        sampleBtn.addActionListener(e -> showSampleNews());
        filterPanel.add(sampleBtn);

        content.add(filterPanel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        newsContainer = new JPanel();
        newsContainer.setLayout(new BoxLayout(newsContainer, BoxLayout.Y_AXIS));
        newsContainer.setOpaque(false);
        content.add(newsContainer);
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        // Travel Alerts card (same as before)
        JPanel travelAlertsCard = createCard("Travel Alerts & Advisories");
        travelAlertsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel alertsContent = new JPanel();
        alertsContent.setLayout(new BoxLayout(alertsContent, BoxLayout.Y_AXIS));
        alertsContent.setOpaque(false);

        String[][] alerts = getTravelAlerts(parent.getCurrentCity());
        for (String[] alert : alerts) {
            alertsContent.add(createAlertItem(alert[0], alert[1], alert[2]));
        }

        travelAlertsCard.add(alertsContent, BorderLayout.CENTER);
        content.add(travelAlertsCard);

        content.add(Box.createVerticalGlue());

        return content;
    }

    private void showSampleNews() {
        newsContainer.removeAll();
        java.util.List<String[]> news = getNews(parent.getCurrentCity());
        for (String[] article : news) {
            newsContainer.add(createNewsCard(article[0], article[1], article[2], article[3], article[4]));
            newsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        revalidate();
        repaint();
    }

    private JPanel createNewsCard(String title, String summary, String source, String date, String category) {
        JPanel card = createCard(null);
        card.setLayout(new BorderLayout(15, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(200, 200, 200));
        imagePanel.setPreferredSize(new Dimension(180, 0));
        JLabel imageLabel = new JLabel("📰", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(getCategoryColor(category));
        categoryLabel.setBorder(new EmptyBorder(2, 8, 2, 8));

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(Color.BLACK);

        headerRow.add(categoryLabel);
        headerRow.add(dateLabel);

        JLabel titleLabel = new JLabel("<html><p style='width: 450px;'>" + title + "</p></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel summaryLabel = new JLabel("<html><p style='width: 450px;'>" + summary + "</p></html>");
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        summaryLabel.setForeground(Color.BLACK);
        summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sourceLabel = new JLabel("Source: " + source);
        sourceLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        sourceLabel.setForeground(Color.BLACK);
        sourceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(headerRow);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(summaryLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(sourceLabel);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createNewsCard(NewsFetcher.NewsItem ni) {
        JPanel card = createNewsCard(ni.title, ni.description, ni.source, ni.publishedAt, "Latest");
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (ni.url != null && !ni.url.isBlank()) {
                    try { Desktop.getDesktop().browse(new java.net.URI(ni.url)); } catch (Exception ex) { /* ignore */ }
                }
            }
        });
        return card;
    }

    private Color getCategoryColor(String category) {
        switch (category) {
            case "Travel Updates": return SmartTravelCompanion.getPrimaryColor();
            case "Events": return new Color(155, 89, 182);
            case "Local News": return SmartTravelCompanion.getAccentColor();
            case "Safety Alerts": return new Color(231, 76, 60);
            default: return new Color(127, 140, 141);
        }
    }

    private JPanel createAlertItem(String level, String title, String description) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(5, 0, 5, 0));

        Color levelColor = level.equals("LOW") ? SmartTravelCompanion.getAccentColor() :
                          level.equals("MEDIUM") ? new Color(241, 196, 15) : new Color(231, 76, 60);

        JLabel levelLabel = new JLabel(level);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        levelLabel.setForeground(Color.BLACK);
        levelLabel.setOpaque(true);
        levelLabel.setBackground(levelColor);
        levelLabel.setBorder(new EmptyBorder(2, 8, 2, 8));
        levelLabel.setPreferredSize(new Dimension(70, 20));
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title + " - ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.BLACK);

        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        item.add(levelLabel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);

        return item;
    }

    // --- Live news fetch -------------------------------------------------
    private void fetchLiveNews() {
        newsContainer.removeAll();
        newsContainer.add(new JLabel("Loading latest news..."));
        revalidate();
        repaint();

        final String city = parent.getCurrentCity();
        final String category = (String) categoryBox.getSelectedItem();
        final JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Loading...", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.add(new JLabel("Fetching latest news..."), BorderLayout.CENTER);
        dlg.pack();
        dlg.setLocationRelativeTo(this);

        SwingWorker<java.util.List<NewsFetcher.NewsItem>, Void> w = new SwingWorker<java.util.List<NewsFetcher.NewsItem>, Void>() {
            @Override protected java.util.List<NewsFetcher.NewsItem> doInBackground() throws Exception {
                try {
                    return NewsFetcher.fetchNewsForState(city, category);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return java.util.Collections.emptyList();
                }
            }
            @Override protected void done() {
                dlg.dispose();
                newsContainer.removeAll();
                try {
                    java.util.List<NewsFetcher.NewsItem> items = get();
                    if (items == null || items.isEmpty()) {
                        newsContainer.add(new JLabel("No live articles found. Use 'Use Sample' to view built-in news."));
                    } else {
                        for (NewsFetcher.NewsItem ni : items) {
                            newsContainer.add(createNewsCard(ni));
                            newsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
                        }
                    }
                } catch (Exception ex) {
                    newsContainer.add(new JLabel("Failed to load news: " + ex.getMessage()));
                }
                revalidate();
                repaint();
            }
        };
        w.execute();
        dlg.setVisible(true);
    }
    // --------------------------------------------------------------------

    private java.util.List<String[]> getNews(String city) {
        java.util.List<String[]> news = new ArrayList<>();

        switch (city) {
            case "Karnataka":
                news.add(new String[]{
                    "Bengaluru Metro Expansion Enters Next Phase",
                    "New corridors announced to improve connectivity to northern suburbs.",
                    "The Hindu", "2 hours ago", "Local News"});
                news.add(new String[]{
                    "Hampi Tourism Sees Rise in Off-Season Visitors",
                    "Improved homestays and guided tours are attracting year-round travelers.",
                    "Times of India", "1 day ago", "Travel Updates"});
                break;
            case "Tamil Nadu":
                news.add(new String[]{
                    "Chennai to Host Cultural Festival this Winter",
                    "A month-long festival highlighting Carnatic music and classical dance.",
                    "The Hindu", "4 hours ago", "Events"});
                news.add(new String[]{
                    "Mahabalipuram Conservation Initiative Launched",
                    "New measures to protect shore temples from erosion.",
                    "The New Indian Express", "1 day ago", "Local News"});
                break;
            case "Maharashtra":
                news.add(new String[]{
                    "Mumbai Coastal Road Progress Accelerates",
                    "Construction updates and traffic advisories for commuters.",
                    "Indian Express", "3 hours ago", "Travel Updates"});
                news.add(new String[]{
                    "Ajanta & Ellora Visitor Facilities Upgraded",
                    "Improved walkways and interpretation centers open to visitors.",
                    "Hindustan Times", "1 day ago", "Local News"});
                break;
            case "Delhi":
                news.add(new String[]{
                    "New Metro Line Improves Access to South Delhi",
                    "Commuters benefit from reduced travel times on new corridor.",
                    "Hindustan Times", "2 hours ago", "Travel Updates"});
                news.add(new String[]{
                    "Red Fort Restoration Work Sees New Phase",
                    "Efforts to preserve Mughal-era structures continue with minimal closures.",
                    "The Hindu", "1 day ago", "Local News"});
                break;
            case "Kerala":
                news.add(new String[]{
                    "Monsoon-Ready Backwater Routes Launched",
                    "Operators prepare for steady tourist arrivals post-monsoon.",
                    "Mathrubhumi", "6 hours ago", "Travel Updates"});
                news.add(new String[]{
                    "Munnar Tea Festival Dates Announced",
                    "Cultural events and tea estate tours scheduled for visitors.",
                    "The Hindu", "2 days ago", "Events"});
                break;
            case "West Bengal":
                news.add(new String[]{
                    "Kolkata Heritage Walks Gain Popularity",
                    "Local guides report increased interest in colonial and cultural tours.",
                    "Times of India", "8 hours ago", "Local News"});
                news.add(new String[]{
                    "Sundarbans Boat Corridors Restricted During Nesting Season",
                    "Authorities issue advisories for controlled wildlife tours.",
                    "The Telegraph", "1 day ago", "Safety Alerts"});
                break;
            case "Rajasthan":
                news.add(new String[]{
                    "Desert Festival Dates Confirmed in Jaisalmer",
                    "Artisan markets and cultural performances to attract tourists.",
                    "Times of India", "3 days ago", "Events"});
                break;
            case "Uttar Pradesh":
                news.add(new String[]{
                    "Taj Mahal Visitor Timings Adjusted for Maintenance",
                    "Short-term timing changes announced; advance booking recommended.",
                    "Indian Express", "5 hours ago", "Travel Updates"});
                break;
            case "Telangana":
                news.add(new String[]{
                    "Hyderabad Launches Night Food-Walks in Old City",
                    "Guided culinary tours showcase local biryani and street foods.",
                    "The Hindu", "7 hours ago", "Events"});
                break;
            case "Gujarat":
                news.add(new String[]{
                    "Rann Utsav Dates and Logistics Published",
                    "Tourist services and camping facilities detailed for visitors.",
                    "Gujarat Samachar", "1 day ago", "Travel Updates"});
                break;
            default:
                news.add(new String[]{"Regional Update: Tourism and Transport",
                    "Local authorities announce improvements to visitor facilities.",
                    "Local News", "4 hours ago", "Local News"});
        }

        return news;
    }

    private String[][] getTravelAlerts(String city) {
        return new String[][]{
            {"LOW", "Weather Advisory", "Expect occasional rain showers; carry light rain gear."},
            {"LOW", "Peak Season Notice", "Attractions may have long queues during peak hours."},
            {"MEDIUM", "Local Transport Disruption", "Plan for possible delays due to local events or strikes."}
        };
    }

    @Override
    public void updateData() {
        // re-fetch when parent/current city changes
        fetchLiveNews();
    }
}
