package travelguide;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class ReviewsPanel extends BasePanel {
    private JPanel reviewsContainer;
    private java.util.List<String[]> reviews;
    
    public ReviewsPanel(SmartTravelCompanion parent) {
        super(parent);
    }
    
    @Override
    protected void initPanel() {
        reviews = new ArrayList<>();
        loadSampleReviews();
        
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SmartTravelCompanion.getBackgroundColor());
        
        add(createHeaderSection("Reviews & Ratings", 
            "See what travelers say about " + parent.getCurrentCity()), BorderLayout.NORTH);
        
        JPanel overviewCard = createCard("Rating Overview");
        overviewCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JPanel overviewContent = new JPanel(new GridLayout(1, 4, 20, 0));
        overviewContent.setOpaque(false);
        
        overviewContent.add(createRatingOverview("Overall", "4.5", 156));
        overviewContent.add(createRatingOverview("Attractions", "4.7", 89));
        overviewContent.add(createRatingOverview("Hotels", "4.3", 67));
        overviewContent.add(createRatingOverview("Restaurants", "4.6", 112));
        
        overviewCard.add(overviewContent, BorderLayout.CENTER);
        content.add(overviewCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel writeReviewCard = createCard("Write a Review");
        writeReviewCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JPanel writeForm = new JPanel();
        writeForm.setLayout(new BoxLayout(writeForm, BoxLayout.Y_AXIS));
        writeForm.setOpaque(false);
        
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        topRow.add(new JLabel("Your Name:"));
        JTextField nameField = new JTextField(15);
        topRow.add(nameField);
        
        topRow.add(new JLabel("Rating:"));
        String[] ratings = {"5 - Excellent", "4 - Very Good", "3 - Average", "2 - Poor", "1 - Terrible"};
        JComboBox<String> ratingCombo = new JComboBox<>(ratings);
        topRow.add(ratingCombo);
        
        topRow.add(new JLabel("Category:"));
        String[] categories = {"General", "Attractions", "Hotels", "Restaurants", "Transportation"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        topRow.add(categoryCombo);
        
        JPanel reviewRow = new JPanel(new BorderLayout(10, 0));
        reviewRow.setOpaque(false);
        reviewRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewRow.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JTextArea reviewArea = new JTextArea(3, 50);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        reviewArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        
        JButton submitBtn = createStyledButton("Submit Review", SmartTravelCompanion.getAccentColor());
        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String review = reviewArea.getText().trim();
            String rating = ((String) ratingCombo.getSelectedItem()).substring(0, 1);
            String category = (String) categoryCombo.getSelectedItem();
            
            if (!name.isEmpty() && !review.isEmpty()) {
                addReview(name, rating, review, category);
                nameField.setText("");
                reviewArea.setText("");
                JOptionPane.showMessageDialog(this, "Thank you for your review!", "Review Submitted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Missing Information", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        reviewRow.add(new JScrollPane(reviewArea), BorderLayout.CENTER);
        reviewRow.add(submitBtn, BorderLayout.EAST);
        
        writeForm.add(topRow);
        writeForm.add(reviewRow);
        
        writeReviewCard.add(writeForm, BorderLayout.CENTER);
        content.add(writeReviewCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel reviewsCard = createCard("Recent Reviews");
        reviewsContainer = new JPanel();
        reviewsContainer.setLayout(new BoxLayout(reviewsContainer, BoxLayout.Y_AXIS));
        reviewsContainer.setOpaque(false);
        
        refreshReviews();
        
        reviewsCard.add(reviewsContainer, BorderLayout.CENTER);
        content.add(reviewsCard);
        
        content.add(Box.createVerticalGlue());
        
        return content;
    }
    
    private JPanel createRatingOverview(String category, String rating, int count) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel(rating + " ⭐");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        ratingLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel countLabel = new JLabel(count + " reviews");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        countLabel.setForeground(Color.BLACK);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(categoryLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(ratingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(countLabel);
        
        return panel;
    }
    
    private void loadSampleReviews() {
        reviews.add(new String[]{"Sarah M.", "5", "Absolutely loved my trip! The attractions were incredible and the food was amazing.", "General", "2 days ago"});
        reviews.add(new String[]{"John D.", "4", "Great experience overall. Hotels were comfortable but a bit pricey.", "Hotels", "1 week ago"});
        reviews.add(new String[]{"Emma L.", "5", "The local cuisine is fantastic. Every restaurant we tried was delicious!", "Restaurants", "2 weeks ago"});
        reviews.add(new String[]{"Michael R.", "4", "Beautiful city with so much history. Would definitely recommend!", "Attractions", "3 weeks ago"});
    }
    
    private void addReview(String name, String rating, String text, String category) {
        reviews.add(0, new String[]{name, rating, text, category, "Just now"});
        refreshReviews();
    }
    
    private void refreshReviews() {
        reviewsContainer.removeAll();
        
        for (String[] review : reviews) {
            reviewsContainer.add(createReviewCard(review[0], review[1], review[2], review[3], review[4]));
            reviewsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        reviewsContainer.revalidate();
        reviewsContainer.repaint();
    }
    
    private JPanel createReviewCard(String name, String rating, String text, String category, String date) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        
        JLabel avatar = new JLabel(name.substring(0, 1).toUpperCase(), SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        avatar.setForeground(Color.BLACK);
        avatar.setOpaque(true);
        avatar.setBackground(SmartTravelCompanion.getPrimaryColor());
        avatar.setPreferredSize(new Dimension(45, 45));
        
        avatarPanel.add(avatar, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel ratingLabel = new JLabel(getStars(Integer.parseInt(rating)));
        ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ratingLabel.setForeground(Color.BLACK);
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(155, 89, 182));
        categoryLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
        
        headerRow.add(nameLabel);
        headerRow.add(ratingLabel);
        headerRow.add(categoryLabel);
        
        JLabel textLabel = new JLabel("<html><p style='width: 500px;'>" + text + "</p></html>");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(Color.BLACK);
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(Color.BLACK);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(headerRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(textLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(dateLabel);
        
        card.add(avatarPanel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private String getStars(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append("★");
        for (int i = count; i < 5; i++) sb.append("☆");
        return sb.toString();
    }
    
    @Override
    public void updateData() {
        removeAll();
        initPanel();
        revalidate();
        repaint();
    }
}
