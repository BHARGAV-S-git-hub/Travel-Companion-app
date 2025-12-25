package travelguide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*; 

public class SmartTravelCompanion extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JComboBox<String> citySelector;
    private JLabel statusLabel;
    private String currentCity = "Karnataka"; // default state
    private JComboBox<String> cityCombo; // top-right selector
    
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    
    public SmartTravelCompanion() {
        setTitle("Smart Travel Companion - Your Complete Travel Guide");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        
        initComponents();
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createSidebarPanel(), BorderLayout.WEST);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);
        
        add(mainPanel);
        
        showPanel("home");
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Smart Travel Companion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setIcon(createIcon("plane"));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel cityLabel = new JLabel("Select City: ");
        cityLabel.setForeground(Color.BLACK);
        cityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] states = {
            "Karnataka", "Tamil Nadu", "Maharashtra", "Delhi", "Kerala",
            "West Bengal", "Rajasthan", "Uttar Pradesh", "Telangana", "Gujarat"
        };
        citySelector = new JComboBox<>(states);
        citySelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        citySelector.setPreferredSize(new Dimension(150, 35));
        citySelector.addActionListener(e -> {
            String selected = (String) citySelector.getSelectedItem();
            setCurrentCity(selected);
            updateAllPanels();
            statusLabel.setText("Showing information for: " + currentCity);
        });
        
        rightPanel.add(cityLabel);
        rightPanel.add(citySelector);
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SECONDARY_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        String[][] menuItems = {
            {"home", "Home", "home"},
            {"mytrips", "My Trips", "mytrips"},
            {"weather", "Weather & Climate", "weather"},
            {"safety", "Safety & Crime", "safety"},
            {"attractions", "Tourist Attractions", "attractions"},
            {"hotels", "Hotels & Stay", "hotels"},
            {"restaurants", "Restaurants & Dining", "restaurants"},
            {"shopping", "Shopping Centers", "shopping"},
            {"transport", "Transportation", "transport"},
            {"history", "History & Culture", "history"},
            {"planner", "Tour Planner", "planner"},
            {"reviews", "Reviews & Ratings", "reviews"},
            {"news", "Latest News", "news"}
        };
        
        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuLabel.setForeground(Color.BLACK);
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLabel.setBorder(new EmptyBorder(0, 10, 10, 0));
        sidebar.add(menuLabel);
        
        for (String[] item : menuItems) {
            JButton btn = createMenuButton(item[1], item[0]);
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JPanel userTypePanel = new JPanel();
        userTypePanel.setLayout(new BoxLayout(userTypePanel, BoxLayout.Y_AXIS));
        userTypePanel.setOpaque(false);
        userTypePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel userLabel = new JLabel("USER TYPE");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(Color.BLACK);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userLabel.setBorder(new EmptyBorder(0, 10, 10, 0));
        userTypePanel.add(userLabel);
        
        ButtonGroup userGroup = new ButtonGroup();
        String[] userTypes = {"Tourist", "Business", "Student"};
        for (String type : userTypes) {
            JRadioButton rb = new JRadioButton(type);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            rb.setForeground(Color.BLACK);
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            rb.setBorder(new EmptyBorder(5, 10, 5, 0));
            if (type.equals("Tourist")) rb.setSelected(true);
            userGroup.add(rb);
            userTypePanel.add(rb);
        }
        
        sidebar.add(userTypePanel);
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String command) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.BLACK);
        btn.setBackground(SECONDARY_COLOR);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SECONDARY_COLOR);
            }
        });
        
        btn.addActionListener(e -> showPanel(command));
        
        return btn;
    }
    
    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        contentPanel.add(new HomePanel(this), "home");
        contentPanel.add(new MyTrips(this), "mytrips");
        contentPanel.add(new WeatherPanel(this), "weather");
        contentPanel.add(new SafetyPanel(this), "safety");
        contentPanel.add(new AttractionsPanel(this), "attractions");
        contentPanel.add(new HotelsPanel(this), "hotels");
        contentPanel.add(new RestaurantsPanel(this), "restaurants");
        contentPanel.add(new ShoppingPanel(this), "shopping");
        contentPanel.add(new TransportPanel(this), "transport");
        contentPanel.add(new HistoryPanel(this), "history");
        contentPanel.add(new TourPlannerPanel(this), "planner");
        contentPanel.add(new ReviewsPanel(this), "reviews");
        contentPanel.add(new NewsPanel(this), "news");
        
        return contentPanel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(44, 62, 80));
        statusBar.setPreferredSize(new Dimension(0, 30));
        statusBar.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        statusLabel = new JLabel("Welcome to Smart Travel Companion | Showing information for: " + currentCity);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setForeground(Color.BLACK);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private ImageIcon createIcon(String name) {
        return null;
    }
    
    public void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }
    
    // Safe getter for current selected state/city
    public String getCurrentCity() {
        return currentCity;
    }

    // Safe setter used by panels to change the selected state/city
    public void setCurrentCity(String city) {
        if (city == null) return;
        this.currentCity = city;
        // keep UI selector in sync if present
        try {
            if (cityCombo != null) cityCombo.setSelectedItem(city);
        } catch (Exception ignored) {}
        // Attempt to refresh panels if the app exposes such method(s)
        try {
            // common pattern: updateData on child panels
            // if you have a method to refresh all panels, call it here
            java.lang.reflect.Method m = this.getClass().getDeclaredMethod("refreshAllPanels");
            m.setAccessible(true);
            m.invoke(this);
        } catch (Exception ignored) {}
    }

    // Example: create or update top-right selector UI — integrate into your existing UI init
    private void initTopRightCitySelector(Container container) {
        // remove any previous combo if present
        if (cityCombo != null) {
            container.remove(cityCombo);
            cityCombo = null;
        }

        String[] states = {
            "Karnataka", "Tamil Nadu", "Maharashtra", "Delhi", "Kerala",
            "West Bengal", "Rajasthan", "Uttar Pradesh", "Telangana", "Gujarat"
        };
        cityCombo = new JComboBox<>(states);
        cityCombo.setSelectedItem(currentCity);
        cityCombo.setPreferredSize(new Dimension(180, 26));
        cityCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) cityCombo.getSelectedItem();
                // setCurrentCity will sync UI and attempt panel refresh
                setCurrentCity(selected);
            }
        });
        // Add to your top-right area — adjust location code below to match your layout
        container.add(cityCombo, BorderLayout.EAST);
    }

    private void updateAllPanels() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof BasePanel) {
                ((BasePanel) comp).updateData();
            }
        }
    }
    
    public static Color getPrimaryColor() { return PRIMARY_COLOR; }
    public static Color getSecondaryColor() { return SECONDARY_COLOR; }
    public static Color getAccentColor() { return ACCENT_COLOR; }
    public static Color getBackgroundColor() { return BACKGROUND_COLOR; }
    public static Color getCardColor() { return CARD_COLOR; }
}
