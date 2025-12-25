package travelguide;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Single-file "My Trips" implementation that contains:
 * - Trip model
 * - TripItem model
 * - TripManager (singleton)
 * - MyTrips panel (extends BasePanel) to show/create/manage trips
 *
 * Use MyTrips.showAddToTripDialog(component, item) from other panels to add items.
 */
public class MyTrips extends BasePanel {
    // --- Models ---
    public static class Trip {
        public final String id;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private String notes;
        private final List<TripItem> items = new ArrayList<>();

        public Trip(String title, LocalDate startDate, LocalDate endDate, String notes) {
            this.id = UUID.randomUUID().toString();
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
            this.notes = notes;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public void setTitle(String t) { this.title = t; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate d) { this.startDate = d; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate d) { this.endDate = d; }
        public String getNotes() { return notes; }
        public void setNotes(String n) { this.notes = n; }
        public List<TripItem> getItems() { return items; }
        public void addItem(TripItem item) { items.add(item); }
        public void removeItem(TripItem item) { items.remove(item); }

        @Override
        public String toString() {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String s = (startDate != null) ? startDate.format(f) : "—";
            String e = (endDate != null) ? endDate.format(f) : "—";
            return title + " (" + s + " → " + e + ")";
        }
    }

    public static class TripItem {
        private final String type; // Hotel, Attraction, Restaurant, Shopping
        private final String name;
        private final LocalDate date; // optional visit date
        private final String details;

        public TripItem(String type, String name, LocalDate date, String details) {
            this.type = type;
            this.name = name;
            this.date = date;
            this.details = details;
        }

        public String getType() { return type; }
        public String getName() { return name; }
        public LocalDate getDate() { return date; }
        public String getDetails() { return details; }

        @Override
        public String toString() {
            String d = (date != null) ? date.toString() : "No date";
            return String.format("%s — %s (%s)", type, name, d);
        }
    }

    // --- Trip manager (singleton) ---
    public static class TripManager {
        private static final TripManager INSTANCE = new TripManager();
        private final List<Trip> trips = new ArrayList<>();
        private final List<Runnable> listeners = new CopyOnWriteArrayList<>();
        private volatile String lastModifiedTripId = null;

        private TripManager() {}

        public static TripManager getInstance() { return INSTANCE; }

        public synchronized List<Trip> getTrips() { return new ArrayList<>(trips); }

        public synchronized Trip createTrip(String title, LocalDate start, LocalDate end, String notes) {
            Trip t = new Trip(title, start, end, notes);
            trips.add(t);
            lastModifiedTripId = t.getId();
            System.out.println("[TripManager] createTrip -> " + t + "  manager@" + System.identityHashCode(this));
            fireChanged();
            return t;
        }

        public synchronized void removeTrip(String tripId) {
            trips.removeIf(t -> t.getId().equals(tripId));
            if (tripId != null && tripId.equals(lastModifiedTripId)) lastModifiedTripId = null;
            System.out.println("[TripManager] removeTrip -> " + tripId);
            fireChanged();
        }

        public synchronized void addItemToTrip(String tripId, TripItem item) {
            for (Trip t : trips) {
                if (t.getId().equals(tripId)) { t.addItem(item); break; }
            }
            lastModifiedTripId = tripId;
            System.out.println("[TripManager] addItemToTrip -> tripId=" + tripId + " item=" + item + "  manager@" + System.identityHashCode(this));
            fireChanged();
        }

        public synchronized Optional<Trip> getTripById(String id) {
            return trips.stream().filter(t -> t.getId().equals(id)).findFirst();
        }

        public void addChangeListener(Runnable r) { listeners.add(r); System.out.println("[TripManager] addChangeListener -> listeners=" + listeners.size()); }
        public void removeChangeListener(Runnable r) { listeners.remove(r); System.out.println("[TripManager] removeChangeListener -> listeners=" + listeners.size()); }
        public String getLastModifiedTripId() { return lastModifiedTripId; }

        // Ensure listeners run on EDT so UI updates are safe; also log for debugging
        private void fireChanged() {
            final int cnt = listeners.size();
            System.out.println("[TripManager] fireChanged -> listeners=" + cnt);
            javax.swing.SwingUtilities.invokeLater(() -> {
                for (Runnable r : listeners) {
                    try { r.run(); } catch (Exception ex) { ex.printStackTrace(); }
                }
            });
        }

        // Dialog to choose existing trip or create a new one and then add the item.
        public void showAddToTripDialog(Component parent, TripItem item) {
            SwingUtilities.invokeLater(() -> {
                List<Trip> current = getTrips();

                JPanel panel = new JPanel(new BorderLayout(8,8));
                JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
                top.add(new JLabel("Choose trip to add:"));

                // use JComboBox<Trip> so we store Trip objects directly and render readable text
                DefaultComboBoxModel<Trip> model = new DefaultComboBoxModel<>();
                for (Trip t : current) model.addElement(t);
                JComboBox<Trip> cb = new JComboBox<>(model);
                cb.setPreferredSize(new Dimension(380, 26));
                cb.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Trip) setText(((Trip) value).toString());
                        return this;
                    }
                });

                top.add(cb);
                JButton newBtn = new JButton("+ New Trip");
                top.add(newBtn);
                panel.add(top, BorderLayout.NORTH);

                JPanel info = new JPanel(new BorderLayout(6,6));
                info.add(new JLabel("<html><i>Item to add:</i> " + item.toString() + "</html>"), BorderLayout.CENTER);
                panel.add(info, BorderLayout.CENTER);

                JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), "Add to Trip", Dialog.ModalityType.APPLICATION_MODAL);
                dlg.getContentPane().add(panel);
                JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton ok = new JButton("Add");
                JButton cancel = new JButton("Cancel");
                actions.add(cancel);
                actions.add(ok);
                panel.add(actions, BorderLayout.SOUTH);
                dlg.pack();
                dlg.setLocationRelativeTo(parent);

                // create new trip from dialog and refresh combo model with Trip objects
                newBtn.addActionListener(ev -> {
                    JPanel np = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc2 = new GridBagConstraints();
                    gbc2.insets = new Insets(6,6,6,6);
                    gbc2.gridx = 0; gbc2.gridy = 0; gbc2.anchor = GridBagConstraints.WEST;
                    np.add(new JLabel("Title:"), gbc2);
                    JTextField title = new JTextField("New Trip", 18);
                    gbc2.gridx = 1; np.add(title, gbc2);

                    gbc2.gridx=0; gbc2.gridy++;
                    np.add(new JLabel("Start date:"), gbc2);
                    JSpinner startSpinner = createDateSpinner();
                    gbc2.gridx = 1; np.add(startSpinner, gbc2);

                    gbc2.gridx=0; gbc2.gridy++;
                    np.add(new JLabel("End date:"), gbc2);
                    JSpinner endSpinner = createDateSpinner();
                    gbc2.gridx = 1; np.add(endSpinner, gbc2);

                    gbc2.gridx=0; gbc2.gridy++;
                    np.add(new JLabel("Notes:"), gbc2);
                    JTextArea notes = new JTextArea(3, 24);
                    gbc2.gridx=1; np.add(new JScrollPane(notes), gbc2);

                    int res = JOptionPane.showConfirmDialog(dlg, np, "Create New Trip", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (res == JOptionPane.OK_OPTION) {
                        LocalDate startDate = MyTrips.parseDateOrNull(new java.text.SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) startSpinner.getModel()).getDate()));
                        LocalDate endDate = MyTrips.parseDateOrNull(new java.text.SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) endSpinner.getModel()).getDate()));
                        Trip nt = createTrip(title.getText(), startDate, endDate, notes.getText()); // createTrip sets lastModifiedTripId
                        // repopulate combo model and select the new trip
                        DefaultComboBoxModel<Trip> m2 = new DefaultComboBoxModel<>();
                        for (Trip t : getTrips()) m2.addElement(t);
                        cb.setModel(m2);
                        cb.setSelectedItem(nt);
                    }
                });

                ok.addActionListener(ev -> {
                    Trip sel = (Trip) cb.getSelectedItem();
                    if (sel == null) {
                        JOptionPane.showMessageDialog(dlg, "Please select a trip or create one.", "No trip selected", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    addItemToTrip(sel.getId(), item); // addItemToTrip sets lastModifiedTripId and fires change
                    dlg.dispose();
                });

                cancel.addActionListener(ev -> dlg.dispose());
                dlg.setVisible(true);
             });
        }
    }

    // Convenience static accessor used by other panels
    public static TripManager manager() { return TripManager.getInstance(); }
    public static void showAddToTripDialog(Component parent, TripItem item) { manager().showAddToTripDialog(parent, item); }

    // --- Parser helper visible to inner classes / other classes ---
    public static LocalDate parseDateOrNull(String text) {
        if (text == null) return null;
        try { return LocalDate.parse(text.trim()); } catch (Exception e) { return null; }
    }

    // small helper to create a date spinner (acts like a lightweight date picker)
    private static JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(160, spinner.getPreferredSize().height));
        return spinner;
    }

    // --- MyTrips panel (this class) ---
    private DefaultListModel<Trip> listModel;
    private JList<Trip> tripsList;
     private JPanel detailsPanel;
     private JButton newTripBtn;
 
     public MyTrips(SmartTravelCompanion parent) {
         super(parent);
     }
 
     @Override
     protected void initPanel() {
        setLayout(new BorderLayout());
        setBackground(SmartTravelCompanion.getBackgroundColor());

        add(createHeaderSection("My Trips", "Create and manage your trips (added from Hotels/Attractions/Shopping/Restaurants)"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(12,12));
        main.setBorder(new EmptyBorder(10,0,0,0));
        main.setOpaque(false);

        // left - create list model + JList (ensure single instance used everywhere)
        listModel = new DefaultListModel<>();
        tripsList = new JList<>(listModel);
        tripsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tripsList.setFixedCellHeight(48);
        JScrollPane leftScroll = new JScrollPane(tripsList);
        leftScroll.setPreferredSize(new Dimension(320, 0));
        leftScroll.setBorder(null);

        JPanel leftWrap = createCard(null);
        leftWrap.setLayout(new BorderLayout());
        JPanel topLeft = new JPanel(new BorderLayout());
        topLeft.setOpaque(false);
        newTripBtn = new JButton("+ New Trip");
        newTripBtn.addActionListener(e -> showCreateTripDialog());
        topLeft.add(new JLabel("Your Trips"), BorderLayout.WEST);
        topLeft.add(newTripBtn, BorderLayout.EAST);
        leftWrap.add(topLeft, BorderLayout.NORTH);
        leftWrap.add(leftScroll, BorderLayout.CENTER);

        // right - details
        detailsPanel = createCard(null);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setPreferredSize(new Dimension(0, 0));

        main.add(leftWrap, BorderLayout.WEST);
        main.add(detailsPanel, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        // wire selection
        tripsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showTripDetails(tripsList.getSelectedValue());
        });

        // register a dedicated listener method (remove old if present)
        TripManager.getInstance().removeChangeListener(this::onTripsChanged);
        TripManager.getInstance().addChangeListener(this::onTripsChanged);

        // populate initially
        refreshListFromManager();

        // refresh when panel becomes visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshListFromManager();
            }
        });
     }
 
    // called by TripManager on change (keeps it separate and avoids method reference confusion)
    private void onTripsChanged() {
        refreshListFromManager();
    }
 
     @Override
     public void updateData() { refreshListFromManager(); }
 
     private void refreshListFromManager() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (listModel == null) listModel = new DefaultListModel<>();
                if (tripsList == null) tripsList = new JList<>(listModel);

                // preserve selection id if possible
                String prevSelId = null;
                Trip selTrip = tripsList.getSelectedValue();
                if (selTrip != null) prevSelId = selTrip.getId();

                listModel.clear();
                java.util.List<Trip> trips = TripManager.getInstance().getTrips();
                for (Trip t : trips) listModel.addElement(t);

                // select last modified trip if present, otherwise preserve previous or select first
                String last = TripManager.getInstance().getLastModifiedTripId();
                if (last != null) {
                    for (int i = 0; i < listModel.size(); i++) {
                        if (listModel.get(i).getId().equals(last)) {
                            tripsList.setSelectedIndex(i);
                            break;
                        }
                    }
                } else if (prevSelId != null) {
                    for (int i = 0; i < listModel.size(); i++) {
                        if (listModel.get(i).getId().equals(prevSelId)) {
                            tripsList.setSelectedIndex(i);
                            break;
                        }
                    }
                } else if (!listModel.isEmpty()) {
                    tripsList.setSelectedIndex(0);
                }

                // update details panel
                if (detailsPanel != null) showTripDetails(tripsList.getSelectedValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void showCreateTripDialog() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        p.add(new JLabel("Title:"), gbc);
        JTextField title = new JTextField("My Trip", 20);
        gbc.gridx = 1; p.add(title, gbc);

        gbc.gridx = 0; gbc.gridy++;
        p.add(new JLabel("Start date:"), gbc);
        JSpinner startSpinner = createDateSpinner();
        gbc.gridx = 1; p.add(startSpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        p.add(new JLabel("End date:"), gbc);
        JSpinner endSpinner = createDateSpinner();
        gbc.gridx = 1; p.add(endSpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        p.add(new JLabel("Notes:"), gbc);
        JTextArea notes = new JTextArea(4, 20);
        gbc.gridx = 1; p.add(new JScrollPane(notes), gbc);

        int res = JOptionPane.showConfirmDialog(this, p, "Create New Trip", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            LocalDate s = MyTrips.parseDateOrNull(new java.text.SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) startSpinner.getModel()).getDate()));
            LocalDate e = MyTrips.parseDateOrNull(new java.text.SimpleDateFormat("yyyy-MM-dd").format(((SpinnerDateModel) endSpinner.getModel()).getDate()));
            Trip t = TripManager.getInstance().createTrip(title.getText(), s, e, notes.getText());
            refreshListFromManager();
            tripsList.setSelectedValue(t, true);
        }
    }

    private LocalDate parseDate(String s) { try { return LocalDate.parse(s.trim()); } catch (Exception ex) { return null; } }

    private void showTripDetails(Trip trip) {
        detailsPanel.removeAll();
        if (trip == null) {
            detailsPanel.add(new JLabel("Select a trip to see details"));
        } else {
            JLabel title = new JLabel(trip.getTitle());
            title.setFont(new Font("Segoe UI", Font.BOLD, 20));
            detailsPanel.add(title);
            detailsPanel.add(Box.createRigidArea(new Dimension(0,8)));
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dates = (trip.getStartDate()!=null ? trip.getStartDate().format(f) : "—") + "  to  " + (trip.getEndDate()!=null ? trip.getEndDate().format(f) : "—");
            detailsPanel.add(new JLabel("Dates: " + dates));
            detailsPanel.add(Box.createRigidArea(new Dimension(0,8)));
            detailsPanel.add(new JLabel("Notes:"));
            JTextArea notes = new JTextArea(trip.getNotes() == null ? "" : trip.getNotes());
            notes.setLineWrap(true); notes.setWrapStyleWord(true);
            notes.setEditable(true);
            notes.setBackground(SmartTravelCompanion.getCardColor());
            detailsPanel.add(new JScrollPane(notes));
            detailsPanel.add(Box.createRigidArea(new Dimension(0,8)));

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton saveNotes = new JButton("Save Notes");
            JButton delete = new JButton("Delete Trip");
            buttons.add(saveNotes);
            buttons.add(delete);
            detailsPanel.add(buttons);

            saveNotes.addActionListener(e -> {
                trip.setNotes(notes.getText());
                // fire change by temporary create/remove trick
                Trip temp = TripManager.getInstance().createTrip("temp-" + System.currentTimeMillis(), null, null, "");
                TripManager.getInstance().removeTrip(temp.getId());
            });

            delete.addActionListener(e -> {
                int r = JOptionPane.showConfirmDialog(this, "Delete trip '" + trip.getTitle() + "'?", "Delete", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    TripManager.getInstance().removeTrip(trip.getId());
                    refreshListFromManager();
                }
            });

            detailsPanel.add(Box.createRigidArea(new Dimension(0,8)));
            detailsPanel.add(new JLabel("Planned items:"));
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            for (TripItem it : new ArrayList<>(trip.getItems())) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.add(new JLabel(it.toString()), BorderLayout.CENTER);
                JButton remove = new JButton("Remove");
                row.add(remove, BorderLayout.EAST);
                remove.addActionListener(e -> {
                    trip.removeItem(it);
                    // fire change
                    Trip temp2 = TripManager.getInstance().createTrip("temp-" + System.currentTimeMillis(), null, null, "");
                    TripManager.getInstance().removeTrip(temp2.getId());
                    refreshListFromManager();
                });
                itemsPanel.add(row);
                itemsPanel.add(Box.createRigidArea(new Dimension(0,6)));
            }
            detailsPanel.add(new JScrollPane(itemsPanel));
        }
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
}