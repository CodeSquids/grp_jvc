package com.projet.Pages;

import com.projet.Main;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    public enum ActivePage {
        VISITORS,
        SITES,
        VISITS,
        COMPLEX
    }

    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);
    private static final Color COLOR_BORDER = new Color(210, 200, 190);
    private static final Color COLOR_BUTTON_REFRESH = new Color(200, 210, 220);
    private static final Color COLOR_ACTIVE_BUTTON = new Color(230, 210, 180);

    private final JFrame owner;
    private final ActivePage activePage;

    public Header(JFrame owner, String title, String subtitle, ActivePage activePage) {
        super();
        this.owner = owner;
        this.activePage = activePage;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_HEADER_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        setBackground(COLOR_HEADER_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("*");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(title);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.WEST);
        textPanel.add(subtitleLabel, BorderLayout.EAST);

        JPanel navPanel = new JPanel(new GridLayout(4, 1, 8, 10));
        navPanel.setOpaque(false);
        navPanel.add(createNavButton("Visiteurs", ActivePage.VISITORS));
        navPanel.add(createNavButton("Sites", ActivePage.SITES));
        navPanel.add(createNavButton("Visites ", ActivePage.VISITS));
        navPanel.add(createNavButton("Complexes", ActivePage.COMPLEX));
        navPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(textPanel);
        add(Box.createVerticalStrut(20));
        add(navPanel);
        add(Box.createVerticalStrut(200));
    }

    private JButton createNavButton(String text, ActivePage targetPage) {
        JButton button = createElegantButton(text, targetPage == activePage ? COLOR_ACTIVE_BUTTON : COLOR_BUTTON_REFRESH);
        button.setEnabled(targetPage != activePage);
        button.addActionListener(e -> openPage(targetPage));
        return button;
    }

    private JButton createElegantButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
//        button.setMaximumSize(new Dimension(180, 40));
        button.setSize(new Dimension(80, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(backgroundColor.darker());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void openPage(ActivePage targetPage) {
        if (targetPage == activePage) {
            return;
        }

        JFrame nextFrame = switch (targetPage) {
            case VISITORS -> new Main();
            case SITES -> new SitePage();
            case VISITS -> new VisiterPage();
            case COMPLEX -> new ComplexQueriesFrame();
        };

        nextFrame.setVisible(true);
        owner.dispose();
    }
}
