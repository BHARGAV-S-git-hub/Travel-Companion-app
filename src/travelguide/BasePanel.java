package travelguide;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected SmartTravelCompanion parent;
    protected JPanel contentArea;
    
    public BasePanel(SmartTravelCompanion parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(SmartTravelCompanion.getBackgroundColor());
        initPanel();
    }
    
    protected abstract void initPanel();
    public abstract void updateData();
    
    protected JPanel createHeaderSection(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(SmartTravelCompanion.getSecondaryColor());
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.BLACK);
        
        header.add(titleLabel, BorderLayout.NORTH);
        header.add(subtitleLabel, BorderLayout.SOUTH);
        
        return header;
    }
    
    protected JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SmartTravelCompanion.getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        if (title != null && !title.isEmpty()) {
            JLabel cardTitle = new JLabel(title);
            cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            cardTitle.setForeground(SmartTravelCompanion.getSecondaryColor());
            cardTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
            card.add(cardTitle, BorderLayout.NORTH);
        }
        
        return card;
    }
    
    protected JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(Color.BLACK);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComp.setForeground(SmartTravelCompanion.getSecondaryColor());
        
        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.EAST);
        
        return row;
    }
    
    protected JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.BLACK);
        btn.setBackground(bgColor);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    protected JProgressBar createRatingBar(int value, int max) {
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setString(value + "/" + max);
        bar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        bar.setForeground(SmartTravelCompanion.getAccentColor());
        bar.setBackground(new Color(220, 220, 220));
        bar.setPreferredSize(new Dimension(100, 20));
        return bar;
    }
}
