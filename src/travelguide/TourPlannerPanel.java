package travelguide;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Simplified Tour Planner:
 * - Exposes static addToCurrentPlan(...) so other panels can add items
 * - Uses listOf(...) helper to handle String[] or List<String>
 */
public class TourPlannerPanel extends BasePanel {
    private JComboBox<String> startCombo;
    private JComboBox<String> destCombo;
    private DefaultListModel<PlanEntry> planModel;
    private JList<PlanEntry> planList;
    private JButton exportBtn;

    // current instance (one per application) so other panels can add to the visible plan
    private static volatile TourPlannerPanel currentInstance = null;

    public TourPlannerPanel(SmartTravelCompanion parent) {
        super(parent);
    }

    @Override
    protected void initPanel() {
        currentInstance = this; // register instance
        setLayout(new BorderLayout());
        setBackground(SmartTravelCompanion.getBackgroundColor());
        add(createHeaderSection("Tour Planner", "Pick a suggested itinerary or build a route between two places. Export a plan to My Trips."), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12,12));
        center.setBorder(new EmptyBorder(12, 12, 12, 12));
        center.setOpaque(false);

        center.add(createSelectorsPanel(), BorderLayout.NORTH);
        center.add(createPlanPanel(), BorderLayout.CENTER);
        center.add(createSuggestedPanel(), BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createSelectorsPanel() {
        JPanel p = createCard(null);
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        String city = parent.getCurrentCity();
        CityData.CityInfo info = CityData.getCity(city);

        startCombo = new JComboBox<>();
        destCombo = new JComboBox<>();

        // populate with attractions (use listOf helper)
        List<String> attractions = listOf(info.attractions);
        for (String a : attractions) {
            startCombo.addItem(a);
            destCombo.addItem(a);
        }

        p.add(new JLabel("Start:"));
        p.add(startCombo);
        p.add(new JLabel("Destination:"));
        p.add(destCombo);

        JButton routeBtn = createStyledButton("Show places on the way", SmartTravelCompanion.getPrimaryColor());
        routeBtn.addActionListener(e -> buildRouteFromSelection());
        p.add(routeBtn);

        exportBtn = createStyledButton("Export Plan to My Trips", SmartTravelCompanion.getAccentColor());
        exportBtn.addActionListener(e -> exportPlanToMyTrips());
        p.add(exportBtn);

        return p;
    }

    private JPanel createPlanPanel() {
        JPanel p = createCard(null);
        p.setLayout(new BorderLayout());
        p.setPreferredSize(new Dimension(0, 360));

        planModel = new DefaultListModel<>();
        planList = new JList<>(planModel);
        planList.setCellRenderer(new PlanEntryRenderer());

        JScrollPane sp = new JScrollPane(planList);
        p.add(sp, BorderLayout.CENTER);

        return p;
    }

    private JPanel createSuggestedPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(new JLabel("Suggested Itineraries"), BorderLayout.WEST);
        p.add(header);
        p.add(Box.createRigidArea(new Dimension(0,8)));

        p.add(createSuggestedCard("1-Day Highlights", "See top sights in one day", evt -> useItinerary("one-day")));
        p.add(Box.createRigidArea(new Dimension(0,8)));
        p.add(createSuggestedCard("3-Day Explorer", "Covers major sites in 3 days", evt -> useItinerary("three-day")));
        p.add(Box.createRigidArea(new Dimension(0,8)));
        p.add(createSuggestedCard("Cultural Deep Dive", "Museums, history and local culture", evt -> useItinerary("cultural")));
        return p;
    }

    private JPanel createSuggestedCard(String title, String desc, ActionListener useAction) {
        JPanel card = createCard(null);
        card.setLayout(new BorderLayout(8,8));
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(new JLabel("<html><b>" + title + "</b><br/><small>" + desc + "</small></html>"), BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        JButton use = createStyledButton("Use This", SmartTravelCompanion.getAccentColor());
        use.addActionListener(useAction);
        right.add(use);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    // PlanEntry and renderer
    public static class PlanEntry {
        final String day;
        final String time;
        final String type; // Attraction/Hotel/Restaurant
        final String name;
        final String details;
        final LocalDate date; // optional

        PlanEntry(String day, String time, String type, String name, String details, LocalDate date) {
            this.day = day; this.time = time; this.type = type; this.name = name; this.details = details; this.date = date;
        }

        @Override public String toString() { return day + " • " + time + " • " + type + " • " + name; }
    }

    private static class PlanEntryRenderer extends JLabel implements ListCellRenderer<PlanEntry> {
        PlanEntryRenderer() { setOpaque(true); setBorder(new EmptyBorder(8,8,8,8)); }
        @Override public Component getListCellRendererComponent(JList<? extends PlanEntry> list, PlanEntry value, int index, boolean isSelected, boolean cellHasFocus) {
            setText("<html><b>" + value.name + "</b> <small>(" + value.type + ")</small><br/><small>" + value.day + " • " + value.time + " • " + value.details + "</small></html>");
            setBackground(isSelected ? SmartTravelCompanion.getPrimaryColor().darker() : SmartTravelCompanion.getCardColor());
            setForeground(isSelected ? Color.WHITE : Color.BLACK);
            return this;
        }
    }

    // Use a simple strategy to build itineraries
    private void useItinerary(String id) {
        planModel.clear();
        String city = parent.getCurrentCity();
        CityData.CityInfo info = CityData.getCity(city);

        List<String> attractions = listOf(info.attractions);
        List<String> hotels = listOf(info.hotels);
        List<String> restaurants = listOf(info.restaurants);

        if ("one-day".equals(id)) {
            planModel.addElement(new PlanEntry("Day 1", "09:00", "Attraction", safeGet(attractions,0,"Sight"), "Popular sight", null));
            planModel.addElement(new PlanEntry("Day 1", "12:30", "Restaurant", safeGet(restaurants,0,"Restaurant"), "Lunch", null));
            planModel.addElement(new PlanEntry("Day 1", "15:00", "Attraction", safeGet(attractions,1,"Sight"), "Afternoon visit", null));
            planModel.addElement(new PlanEntry("Day 1", "20:00", "Hotel", safeGet(hotels,0,"Hotel"), "Overnight", null));
        } else if ("three-day".equals(id)) {
            planModel.addElement(new PlanEntry("Day 1", "09:00", "Attraction", safeGet(attractions,0,"Sight"), "Morning", null));
            planModel.addElement(new PlanEntry("Day 1", "13:00", "Restaurant", safeGet(restaurants,0,"Restaurant"), "Lunch", null));
            planModel.addElement(new PlanEntry("Day 1", "18:00", "Hotel", safeGet(hotels,0,"Hotel"), "Stay", null));
            planModel.addElement(new PlanEntry("Day 2", "09:30", "Attraction", safeGet(attractions,1,"Sight"), "Explore", null));
            planModel.addElement(new PlanEntry("Day 2", "14:00", "Attraction", safeGet(attractions,2,"Sight"), "Visit", null));
            planModel.addElement(new PlanEntry("Day 3", "10:00", "Attraction", safeGet(attractions,3,"Sight"), "Final visit", null));
            planModel.addElement(new PlanEntry("Day 3", "19:00", "Hotel", safeGet(hotels,1,"Hotel"), "Stay", null));
        } else {
            planModel.addElement(new PlanEntry("Day 1", "10:00", "Attraction", safeGet(attractions,2,"Museum"), "Museum & history", null));
            planModel.addElement(new PlanEntry("Day 1", "13:00", "Restaurant", safeGet(restaurants,1,"Restaurant"), "Local cuisine", null));
            planModel.addElement(new PlanEntry("Day 2", "11:00", "Attraction", safeGet(attractions,4,"Heritage"), "Heritage site", null));
            planModel.addElement(new PlanEntry("Day 2", "20:00", "Hotel", safeGet(hotels,0,"Hotel"), "Stay", null));
        }
    }

    // Build route based on start/destination: find indices and include attractions between them
    private void buildRouteFromSelection() {
        final String origin = (String) startCombo.getSelectedItem();
        final String dest = (String) destCombo.getSelectedItem();
        if (origin == null || dest == null) {
            JOptionPane.showMessageDialog(this, "Choose start and destination.", "Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final String orsKeyTmp = System.getProperty("ORS_API_KEY");
        final String orsKey = (orsKeyTmp == null || orsKeyTmp.isBlank())
                ? "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6ImIyYzM5YmRjZjQyODQ5ZTBhODA4ODdiN2YzMGFlMWM1IiwiaCI6Im11cm11cjY0In0="
                : orsKeyTmp;

        final JDialog progress = new JDialog(SwingUtilities.getWindowAncestor(this), "Finding places...", Dialog.ModalityType.APPLICATION_MODAL);
        progress.setLayout(new BorderLayout());
        progress.add(new JLabel("Fetching route and nearby attractions..."), BorderLayout.CENTER);
        progress.pack();
        progress.setLocationRelativeTo(this);

        SwingWorker<List<RoutePlacesFetcherORS.Place>, Void> w = new SwingWorker<List<RoutePlacesFetcherORS.Place>, Void>() {
            @Override
            protected List<RoutePlacesFetcherORS.Place> doInBackground() throws Exception {
                // sampleStep=8, bufferMeters=800, maxPlaces=12
                return RoutePlacesFetcherORS.fetchPlacesAlongRoute(origin, dest, orsKey, 8, 800, 12);
            }
            @Override
            protected void done() {
                progress.dispose();
                try {
                    List<RoutePlacesFetcherORS.Place> places = get();
                    planModel.clear();
                    if (places.isEmpty()) {
                        JOptionPane.showMessageDialog(TourPlannerPanel.this, "No places found along the route.", "No results", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int idx = 0;
                        for (RoutePlacesFetcherORS.Place p : places) {
                            idx++;
                            planModel.addElement(new PlanEntry("Day 1", String.format("%02d:00", 9 + (idx % 8)), "Attraction", p.name, p.kind, null));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(TourPlannerPanel.this, "Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        w.execute();
        progress.setVisible(true);
    }

    private String formatTimeForIndex(int i) {
        int hour = 9 + (i % 8);
        return String.format("%02d:00", hour);
    }

    private String safeGet(List<String> list, int idx, String fallback) {
        if (list == null || list.isEmpty()) return fallback;
        if (idx < 0 || idx >= list.size()) return list.get(0);
        return list.get(idx);
    }

    // Export current plan to MyTrips
    private void exportPlanToMyTrips() {
        if (planModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plan is empty. Choose a suggested itinerary or build a route first.", "No Plan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.WEST;
        p.add(new JLabel("Title:"), gbc);
        JTextField titleField = new JTextField("My Exported Trip", 20);
        gbc.gridx=1; p.add(titleField, gbc);

        gbc.gridx=0; gbc.gridy++;
        p.add(new JLabel("Start date:"), gbc);
        JSpinner startSpinner = createDateSpinner();
        gbc.gridx=1; p.add(startSpinner, gbc);

        gbc.gridx=0; gbc.gridy++;
        p.add(new JLabel("End date:"), gbc);
        JSpinner endSpinner = createDateSpinner();
        gbc.gridx=1; p.add(endSpinner, gbc);

        gbc.gridx=0; gbc.gridy++;
        p.add(new JLabel("Notes:"), gbc);
        JTextArea notes = new JTextArea(3, 20);
        gbc.gridx=1; p.add(new JScrollPane(notes), gbc);

        int res = JOptionPane.showConfirmDialog(this, p, "Export Plan to My Trips", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        LocalDate startDate = MyTrips.parseDateOrNull(new SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) startSpinner.getModel()).getDate()));
        LocalDate endDate = MyTrips.parseDateOrNull(new SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) endSpinner.getModel()).getDate()));

        MyTrips.Trip trip = MyTrips.manager().createTrip(titleField.getText(), startDate, endDate, notes.getText());

        for (int i = 0; i < planModel.size(); i++) {
            PlanEntry pe = planModel.get(i);
            int dayNum = 1;
            try { dayNum = Integer.parseInt(pe.day.replaceAll("\\D+","")); } catch (Exception ex) { dayNum = 1; }
            LocalDate itemDate = null;
            if (startDate != null) itemDate = startDate.plusDays(dayNum - 1);
            MyTrips.TripItem ti = new MyTrips.TripItem(pe.type, pe.name, itemDate, pe.details + " • " + pe.time);
            MyTrips.manager().addItemToTrip(trip.getId(), ti);
        }

        JOptionPane.showMessageDialog(this, "Plan exported to My Trips: " + trip.getTitle(), "Exported", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(140, spinner.getPreferredSize().height));
        return spinner;
    }

    // convert possible String[] or List<String> into List<String>
    @SuppressWarnings("unchecked")
    private static List<String> listOf(Object maybe) {
        if (maybe == null) return Collections.emptyList();
        if (maybe instanceof String[]) return Arrays.asList((String[]) maybe);
        if (maybe instanceof List) return (List<String>) maybe;
        return Collections.singletonList(maybe.toString());
    }

    // Public API for other panels to add to the currently visible plan
    public static void addToCurrentPlan(String type, String name, String time, String details) {
        TourPlannerPanel inst = currentInstance;
        if (inst == null) return;
        SwingUtilities.invokeLater(() -> {
            if (inst.planModel == null) inst.planModel = new DefaultListModel<>();
            inst.planModel.addElement(new PlanEntry("Day 1", time == null ? "09:00" : time, type, name, details == null ? "" : details, null));
        });
    }

    @Override
    public void updateData() {
        // refresh UI when parent data (current city) changes — also regenerate a default plan
        SwingUtilities.invokeLater(() -> {
            try {
                CityData.CityInfo info = CityData.getCity(parent.getCurrentCity());
                if (startCombo != null && destCombo != null) {
                    startCombo.removeAllItems();
                    destCombo.removeAllItems();
                    for (String a : listOf(info.attractions)) {
                        startCombo.addItem(a);
                        destCombo.addItem(a);
                    }
                }
                // ensure models exist
                if (planModel == null) planModel = new DefaultListModel<>();
                if (planList == null) planList = new JList<>(planModel);
                // regenerate a sensible default plan for the new city
                useItinerary("one-day");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
