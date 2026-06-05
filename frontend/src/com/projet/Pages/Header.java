package com.projet.Pages;

import com.projet.Main;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class Header extends JPanel {
    public enum ActivePage {
        VISITORS,
        SITES,
        VISITS,
        COMPLEX1,
        COMPLEX2
    }

    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);
    private static final Color COLOR_BORDER = new Color(210, 200, 190);
    private static final Color COLOR_BUTTON_REFRESH = new Color(200, 210, 220);
    private static final Color COLOR_ACTIVE_BUTTON = new Color(230, 210, 180);
    private static final String ICON_VISITORS = "/com/projet/images/user-circle.png";
    private static final String ICON_SITES = "/com/projet/images/location-map.png";
    private static final String ICON_VISITS = "/com/projet/images/chat-square.png";
    private static final String ICON_COMPLEX_1 = "/com/projet/images/stone.png";
    private static final String ICON_COMPLEX_2 = "/com/projet/images/briefcase.png";

    private final JFrame owner;
    private final ActivePage activePage;

    public Header(JFrame owner, ActivePage activePage) {
        super();
        this.owner = owner;
        this.activePage = activePage;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_HEADER_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("*");
        titleLabel.setPreferredSize(new Dimension(20, 20));

        ImageIcon headerIcon = loadHeaderIcon();
        if (headerIcon != null) { titleLabel = new JLabel("HereVisit", headerIcon, JLabel.LEFT); }
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titleLabel.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 8, 10));
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, textPanel.getPreferredSize().height));

        JPanel navMainPanel = new JPanel(new GridLayout(5, 1, 8, 10));
        navMainPanel.setOpaque(false);

        JLabel spacesLabel = new JLabel("Espaces ");
        spacesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        spacesLabel.setForeground(Color.WHITE);
        navMainPanel.add(spacesLabel);
        navMainPanel.add(createNavButton("Visiteurs", ActivePage.VISITORS, ICON_VISITORS));
        navMainPanel.add(createNavButton("Sites", ActivePage.SITES, ICON_SITES));
        navMainPanel.add(createNavButton("Visites ", ActivePage.VISITS, ICON_VISITS));
//        navMainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navMainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, navMainPanel.getPreferredSize().height));


        JPanel navCompPanel = new JPanel(new GridLayout(4, 1, 8, 10));
        navCompPanel.setOpaque(false);
        JLabel complexLabel = new JLabel("Complexites ");
        complexLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        complexLabel.setForeground(Color.WHITE);
        navCompPanel.add(complexLabel);
        navCompPanel.add(createNavButton("Complexe1", ActivePage.COMPLEX1, ICON_COMPLEX_1));
        navCompPanel.add(createNavButton("Complexe2", ActivePage.COMPLEX2, ICON_COMPLEX_2));
        navCompPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navCompPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, navCompPanel.getPreferredSize().height));

        add(textPanel);
        add(navMainPanel);
        add(navCompPanel);
    }

    private JButton createNavButton(String text, ActivePage targetPage, String iconPath) {
        JButton button = createElegantButton(text, loadButtonIcon(iconPath), targetPage == activePage ? COLOR_ACTIVE_BUTTON : COLOR_BUTTON_REFRESH);
        button.setForeground(targetPage == activePage ? Color.BLACK : Color.WHITE );
        button.setEnabled(targetPage != activePage);
        button.setContentAreaFilled(targetPage == activePage);
        button.addActionListener(e -> openPage(targetPage));
        return button;
    }

    private JButton createElegantButton(String text, ImageIcon icon, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(10);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 2, true),
                BorderFactory.createEmptyBorder(9, 21, 9, 21)
        ));
        button.setOpaque(true);
        button.setContentAreaFilled(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(backgroundColor);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private ImageIcon loadButtonIcon(String iconPath) {
        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl != null) {
            return scaleIcon(new ImageIcon(iconUrl));
        }
        return null;
    }

    private ImageIcon loadHeaderIcon() {
        URL iconUrl = getClass().getResource("/com/projet/images/icon-192.png");
        if (iconUrl != null) {
            return scaleIcon(new ImageIcon(iconUrl));
        }
        return null;
    }

    private ImageIcon scaleIcon(ImageIcon icon) {
        Image scaledImage = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void openPage(ActivePage targetPage) {
        if (targetPage == activePage) {
            return;
        }

        JFrame nextFrame = switch (targetPage) {
            case VISITORS -> new Main();
            case SITES -> new SitePage();
            case VISITS -> new VisiterPage();
            case COMPLEX1 -> new ComplexQueriesFrame();
            case COMPLEX2 -> new Complex2();
        };

        nextFrame.setVisible(true);
        owner.dispose();
    }
}
