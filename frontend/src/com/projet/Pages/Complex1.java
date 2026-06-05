package com.projet.Pages;

import com.projet.Services.VisiterService;
import com.projet.Tables.Complex1Model;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

public class Complex1 extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JPanel complex1Panel;
    private JTable complex1Table;
    private Complex1Model complex1Model;

    private JComboBox<String> cmbFiltreSite1;
    private JComboBox<String> cmbPeriode1;
    private JTextField txtAnnee1;
    private JTextField txtMois1;
    private JTextField txtDateStart1;
    private JTextField txtDateEnd1;
    private JPanel dynamicPanel1;
    private JButton btnAppliquerFiltres1;
    private JButton btnReinitialiser1;

    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JLabel lblRecordCount;
    private JLabel lblTotal1;

    private static final Color COLOR_BACKGROUND = new Color(250, 248, 245);
    private static final Color COLOR_PANEL = new Color(255, 255, 255);
    private static final Color COLOR_BORDER = new Color(210, 200, 190);
    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);
    private static final Color COLOR_ACCENT = new Color(160, 130, 110);
    private static final Color COLOR_BUTTON_APPLY = new Color(200, 230, 210);
    private static final Color COLOR_BUTTON_REFRESH = new Color(200, 210, 220);
    private static final Color COLOR_TABLE_HEADER = new Color(55, 50, 65);
    private static final Color COLOR_ROW_ODD = new Color(255, 255, 255);
    private static final Color COLOR_ROW_EVEN = new Color(248, 245, 242);

    private String[] sites = {"Tous les sites"};

    public Complex1() {
        setTitle("HereVisit : Gestion des Visites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 950);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JPanel headerPanel = new Header(this, Header.ActivePage.COMPLEX1);
        headerPanel.setPreferredSize(new Dimension(200, getHeight()));
        mainPanel.add(headerPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(1000, 0));

        complex1Model = new Complex1Model();
        chargerListeSites();
        createComplex1Panel();
        centerPanel.add(complex1Panel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        chargerToutesVisites();
        setVisible(true);
    }

    private void chargerListeSites() {
        try {
            JSONArray stats = VisiterService.complex4();
            if (stats != null && stats.length() > 0) {
                java.util.List<String> siteList = new java.util.ArrayList<>();
                siteList.add("Tous les sites");
                for (int i = 0; i < stats.length(); i++) {
                    String nomSite = stats.getJSONObject(i).getString("nom_site");
                    if (!siteList.contains(nomSite)) {
                        siteList.add(nomSite);
                    }
                }
                sites = siteList.toArray(new String[0]);
            }
        } catch (Exception e) {
            System.out.println("Impossible de charger les sites: " + e.getMessage());
        }
    }

    private void createComplex1Panel() {
        complex1Panel = new JPanel(new BorderLayout(10, 10));
        complex1Panel.setOpaque(false);
        complex1Panel.add(createFilterPanel(), BorderLayout.NORTH);
        complex1Panel.add(createTableContainer(), BorderLayout.CENTER);
        updateDynamicFields1();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Filtres de la requete 1");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);

        JLabel siteLabel = new JLabel("Filtrer par site :");
        siteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        siteLabel.setForeground(Color.BLACK);

        cmbFiltreSite1 = new JComboBox<>(sites);
        cmbFiltreSite1.setPreferredSize(new Dimension(180, 30));

        JLabel periodeLabel = new JLabel("Periode :");
        periodeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        periodeLabel.setForeground(Color.BLACK);

        cmbPeriode1 = new JComboBox<>(new String[]{
                "Toute l'annee en cours",
                "Annee specifique",
                "Mois specifique",
                "Entre 2 dates"
        });
        cmbPeriode1.setPreferredSize(new Dimension(180, 30));
        cmbPeriode1.addActionListener(e -> updateDynamicFields1());

        topPanel.add(siteLabel);
        topPanel.add(cmbFiltreSite1);
        topPanel.add(periodeLabel);
        topPanel.add(cmbPeriode1);

        dynamicPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        dynamicPanel1.setOpaque(false);

        txtAnnee1 = new JTextField(10);
        txtMois1 = new JTextField(10);
        txtDateStart1 = new JTextField(10);
        txtDateEnd1 = new JTextField(10);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAppliquerFiltres1 = createElegantButton("APPLIQUER LES FILTRES", COLOR_BUTTON_APPLY);
        btnAppliquerFiltres1.addActionListener(e -> appliquerFiltres1());

        btnReinitialiser1 = createElegantButton("REINITIALISER", COLOR_BUTTON_REFRESH);
        btnReinitialiser1.addActionListener(e -> chargerToutesVisites());

        actionsPanel.add(btnAppliquerFiltres1);
        actionsPanel.add(btnReinitialiser1);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(dynamicPanel1, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTableContainer() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Resultats de la requete");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecordCount.setForeground(Color.DARK_GRAY);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);

        complex1Table = new JTable(complex1Model);
        complex1Table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        complex1Table.setRowHeight(32);
        complex1Table.setIntercellSpacing(new Dimension(10, 5));
        complex1Table.setShowGrid(false);
        complex1Table.setBackground(COLOR_PANEL);
        complex1Table.setForeground(Color.BLACK);
        complex1Table.setSelectionBackground(new Color(160, 130, 110, 40));
        complex1Table.setSelectionForeground(Color.BLACK);

        JTableHeader header = complex1Table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_TABLE_HEADER);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        complex1Table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_ROW_ODD : COLOR_ROW_EVEN);
                    c.setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(complex1Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(COLOR_PANEL);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        lblTotal1 = new JLabel("Total: 0,00 Ar");
        lblTotal1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal1.setForeground(COLOR_ACCENT);
        totalPanel.add(lblTotal1);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateDynamicFields1() {
        if (dynamicPanel1 == null || cmbPeriode1 == null) {
            return;
        }

        dynamicPanel1.removeAll();
        String periode = (String) cmbPeriode1.getSelectedItem();

        if ("Annee specifique".equals(periode)) {
            dynamicPanel1.add(createLabel("Annee (YYYY) :"));
            dynamicPanel1.add(txtAnnee1);
            txtAnnee1.setPreferredSize(new Dimension(120, 30));
            txtAnnee1.setText(String.valueOf(Year.now().getValue()));
        } else if ("Mois specifique".equals(periode)) {
            dynamicPanel1.add(createLabel("Annee (YYYY) :"));
            dynamicPanel1.add(txtAnnee1);
            dynamicPanel1.add(createLabel("Mois (1-12) :"));
            dynamicPanel1.add(txtMois1);
            txtAnnee1.setPreferredSize(new Dimension(100, 30));
            txtMois1.setPreferredSize(new Dimension(80, 30));
            txtAnnee1.setText(String.valueOf(Year.now().getValue()));
            txtMois1.setText(String.valueOf(LocalDate.now().getMonthValue()));
        } else if ("Entre 2 dates".equals(periode)) {
            dynamicPanel1.add(createLabel("Date debut (YYYY-MM-DD) :"));
            dynamicPanel1.add(txtDateStart1);
            dynamicPanel1.add(createLabel("Date fin (YYYY-MM-DD) :"));
            dynamicPanel1.add(txtDateEnd1);
            txtDateStart1.setPreferredSize(new Dimension(120, 30));
            txtDateEnd1.setPreferredSize(new Dimension(120, 30));
            txtDateStart1.setText(LocalDate.now().withDayOfYear(1).toString());
            txtDateEnd1.setText(LocalDate.now().toString());
        }

        dynamicPanel1.revalidate();
        dynamicPanel1.repaint();
    }

    private void chargerToutesVisites() {
        setLoading(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                Date startDate = Date.valueOf("2000-01-01");
                Date endDate = Date.valueOf(LocalDate.now().plusYears(10).toString());
                return VisiterService.complex1("Tous les sites", startDate, endDate);
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    complex1Model.setComplex1(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    lblTotal1.setText("Total: " + String.format("%,.2f", complex1Model.getTotalMontant()) + " Ar");
                    lblStatus.setText(result.length() == 0
                            ? "Aucune visite trouvee dans la base de donnees"
                            : "Toutes les visites chargees - " + result.length() + " visiteur(s) trouve(s)");
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                    complex1Model.setComplex1(new JSONArray());
                    lblRecordCount.setText("0 enregistrement(s)");
                    lblTotal1.setText("Total: 0,00 Ar");
                } finally {
                    setLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void appliquerFiltres1() {
        String siteNom = (String) cmbFiltreSite1.getSelectedItem();
        String periode = (String) cmbPeriode1.getSelectedItem();

        if (!validerPeriodes1(periode)) {
            return;
        }

        setLoading(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                Date startDate = getStartDate1(periode);
                Date endDate = getEndDate1(periode);
                return VisiterService.complex1(siteNom, startDate, endDate);
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    complex1Model.setComplex1(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    lblTotal1.setText("Total: " + String.format("%,.2f", complex1Model.getTotalMontant()) + " Ar");
                    lblStatus.setText(result.length() == 0
                            ? "Aucun resultat pour les filtres selectionnes"
                            : "Filtres appliques - " + result.length() + " visiteur(s) trouve(s)");
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                } finally {
                    setLoading(false);
                }
            }
        };
        worker.execute();
    }

    private boolean validerPeriodes1(String periode) {
        if ("Annee specifique".equals(periode)) {
            if (txtAnnee1.getText().trim().isEmpty()) {
                showError("Veuillez saisir une annee !");
                return false;
            }
        } else if ("Mois specifique".equals(periode)) {
            if (txtAnnee1.getText().trim().isEmpty() || txtMois1.getText().trim().isEmpty()) {
                showError("Veuillez saisir l'annee et le mois !");
                return false;
            }
        } else if ("Entre 2 dates".equals(periode)) {
            if (txtDateStart1.getText().trim().isEmpty() || txtDateEnd1.getText().trim().isEmpty()) {
                showError("Veuillez saisir les dates debut et fin !");
                return false;
            }
        }
        return true;
    }

    private Date getStartDate1(String periode) {
        int currentYear = Year.now().getValue();

        if ("Toute l'annee en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-01-01");
        } else if ("Annee specifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-01-01");
        } else if ("Mois specifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-"
                    + String.format("%02d", Integer.parseInt(txtMois1.getText().trim())) + "-01");
        }
        return Date.valueOf(txtDateStart1.getText().trim());
    }

    private Date getEndDate1(String periode) {
        int currentYear = Year.now().getValue();

        if ("Toute l'annee en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-12-31");
        } else if ("Annee specifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-12-31");
        } else if ("Mois specifique".equals(periode)) {
            int mois = Integer.parseInt(txtMois1.getText().trim());
            int lastDay = getLastDayOfMonth(mois, Integer.parseInt(txtAnnee1.getText().trim()));
            return Date.valueOf(txtAnnee1.getText().trim() + "-"
                    + String.format("%02d", mois) + "-" + lastDay);
        }
        return Date.valueOf(txtDateEnd1.getText().trim());
    }

    private int getLastDayOfMonth(int mois, int annee) {
        if (mois == 2) {
            if ((annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0)) {
                return 29;
            }
            return 28;
        } else if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
            return 30;
        }
        return 31;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statusPanel.setBackground(COLOR_HEADER_BG);

        lblStatus = new JLabel("Chargement des donnees en cours...");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(150, 12));
        progressBar.setBackground(new Color(80, 75, 85));
        progressBar.setForeground(COLOR_ACCENT);

        statusPanel.add(lblStatus, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.EAST);

        return statusPanel;
    }

    private void setLoading(boolean loading) {
        progressBar.setVisible(loading);
        btnAppliquerFiltres1.setEnabled(!loading);
        btnReinitialiser1.setEnabled(!loading);
        progressBar.setIndeterminate(loading);
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Erreur de saisie", JOptionPane.ERROR_MESSAGE));
    }

    private JButton createElegantButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(9, 21, 9, 21)
        ));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER.darker(), 1),
                        BorderFactory.createEmptyBorder(9, 21, 9, 21)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(9, 21, 9, 21)
                ));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Complex1();
        });
    }
}
