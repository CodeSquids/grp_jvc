package com.projet.Pages;

import com.projet.Services.VisiterService;
import com.projet.Tables.Complex2Model;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

public class Complex2 extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JPanel complex2Panel;
    private JTable complex2Table;
    private Complex2Model complex2Model;

    private JComboBox<String> cmbPeriode2;
    private JTextField txtAnnee2;
    private JTextField txtMois2;
    private JTextField txtDateStart2;
    private JTextField txtDateEnd2;
    private JPanel dynamicPanel2;
    private JButton btnAppliquerFiltres2;
    private JButton btnReinitialiser2;

    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JLabel lblRecordCount;

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

    public Complex2() {
        setTitle("HereVisit : Gestion des Visites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 950);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JPanel headerPanel = new Header(this, Header.ActivePage.COMPLEX2);
        headerPanel.setPreferredSize(new Dimension(200, getHeight()));
        mainPanel.add(headerPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(1000, 0));

        complex2Model = new Complex2Model();
        createComplex2Panel();
        centerPanel.add(complex2Panel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        chargerToutesStats();
    }

    private void createComplex2Panel() {
        complex2Panel = new JPanel(new BorderLayout(10, 10));
        complex2Panel.setOpaque(false);
        complex2Panel.add(createFilterPanel(), BorderLayout.NORTH);
        complex2Panel.add(createTableContainer(), BorderLayout.CENTER);
        updateDynamicFields2();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Filtres de la requete 2");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);

        JLabel periodeLabel = new JLabel("Periode :");
        periodeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        periodeLabel.setForeground(Color.BLACK);

        cmbPeriode2 = new JComboBox<>(new String[]{
                "Toute l'annee en cours",
                "Annee specifique",
                "Mois specifique",
                "Entre 2 dates"
        });
        cmbPeriode2.setPreferredSize(new Dimension(180, 30));
        cmbPeriode2.addActionListener(e -> updateDynamicFields2());

        topPanel.add(periodeLabel);
        topPanel.add(cmbPeriode2);

        dynamicPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        dynamicPanel2.setOpaque(false);

        txtAnnee2 = new JTextField(10);
        txtMois2 = new JTextField(10);
        txtDateStart2 = new JTextField(10);
        txtDateEnd2 = new JTextField(10);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAppliquerFiltres2 = createElegantButton("APPLIQUER LES FILTRES", COLOR_BUTTON_APPLY);
        btnAppliquerFiltres2.addActionListener(e -> appliquerFiltres2());

        btnReinitialiser2 = createElegantButton("REINITIALISER", COLOR_BUTTON_REFRESH);
        btnReinitialiser2.addActionListener(e -> chargerToutesStats());

        actionsPanel.add(btnAppliquerFiltres2);
        actionsPanel.add(btnReinitialiser2);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(dynamicPanel2, BorderLayout.CENTER);
        contentPanel.add(actionsPanel, BorderLayout.SOUTH);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

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

        JLabel titleLabel = new JLabel("Resultats de la requete :  Nombres de visiteurs et montant total des visites par site dans une annee ou dans un mois ou entre deux dates");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecordCount.setForeground(Color.DARK_GRAY);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);

        complex2Table = new JTable(complex2Model);
        complex2Table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        complex2Table.setRowHeight(32);
        complex2Table.setIntercellSpacing(new Dimension(10, 5));
        complex2Table.setShowGrid(false);
        complex2Table.setBackground(COLOR_PANEL);
        complex2Table.setForeground(Color.BLACK);
        complex2Table.setSelectionBackground(new Color(160, 130, 110, 40));
        complex2Table.setSelectionForeground(Color.BLACK);

        JTableHeader header = complex2Table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_TABLE_HEADER);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        complex2Table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(complex2Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(COLOR_PANEL);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateDynamicFields2() {
        if (dynamicPanel2 == null || cmbPeriode2 == null) {
            return;
        }

        dynamicPanel2.removeAll();
        String periode = (String) cmbPeriode2.getSelectedItem();

        if ("Annee specifique".equals(periode)) {
            dynamicPanel2.add(createLabel("Annee (YYYY) :"));
            dynamicPanel2.add(txtAnnee2);
            txtAnnee2.setPreferredSize(new Dimension(120, 30));
            txtAnnee2.setText(String.valueOf(Year.now().getValue()));
        } else if ("Mois specifique".equals(periode)) {
            dynamicPanel2.add(createLabel("Annee (YYYY) :"));
            dynamicPanel2.add(txtAnnee2);
            dynamicPanel2.add(createLabel("Mois (1-12) :"));
            dynamicPanel2.add(txtMois2);
            txtAnnee2.setPreferredSize(new Dimension(100, 30));
            txtMois2.setPreferredSize(new Dimension(80, 30));
            txtAnnee2.setText(String.valueOf(Year.now().getValue()));
            txtMois2.setText(String.valueOf(LocalDate.now().getMonthValue()));
        } else if ("Entre 2 dates".equals(periode)) {
            dynamicPanel2.add(createLabel("Date debut (YYYY-MM-DD) :"));
            dynamicPanel2.add(txtDateStart2);
            dynamicPanel2.add(createLabel("Date fin (YYYY-MM-DD) :"));
            dynamicPanel2.add(txtDateEnd2);
            txtDateStart2.setPreferredSize(new Dimension(120, 30));
            txtDateEnd2.setPreferredSize(new Dimension(120, 30));
            txtDateStart2.setText(LocalDate.now().withDayOfYear(1).toString());
            txtDateEnd2.setText(LocalDate.now().toString());
        }

        dynamicPanel2.revalidate();
        dynamicPanel2.repaint();
    }

    private void chargerToutesStats() {
        setLoading(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiterService.complex4();
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    complex2Model.setComplex2(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    lblStatus.setText(result.length() == 0
                            ? "Aucune statistique disponible"
                            : "Toutes les statistiques chargees - " + result.length() + " site(s) trouve(s)");
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                    complex2Model.setComplex2(new JSONArray());
                    lblRecordCount.setText("0 enregistrement(s)");
                } finally {
                    setLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void appliquerFiltres2() {
        String periode = (String) cmbPeriode2.getSelectedItem();

        if (!validerPeriodes2(periode)) {
            return;
        }

        setLoading(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                if ("Toute l'annee en cours".equals(periode)) {
                    return VisiterService.complex4();
                }

                Date startDate = getStartDate2(periode);
                Date endDate = getEndDate2(periode);
                return VisiterService.complex2(startDate, endDate);
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    complex2Model.setComplex2(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    lblStatus.setText(result.length() == 0
                            ? "Aucune statistique pour les filtres selectionnes"
                            : "Statistiques filtrees - " + result.length() + " site(s) trouve(s)");
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                } finally {
                    setLoading(false);
                }
            }
        };
        worker.execute();
    }

    private boolean validerPeriodes2(String periode) {
        if ("Annee specifique".equals(periode)) {
            if (txtAnnee2.getText().trim().isEmpty()) {
                showError("Veuillez saisir une annee !");
                return false;
            }
        } else if ("Mois specifique".equals(periode)) {
            if (txtAnnee2.getText().trim().isEmpty() || txtMois2.getText().trim().isEmpty()) {
                showError("Veuillez saisir l'annee et le mois !");
                return false;
            }
        } else if ("Entre 2 dates".equals(periode)) {
            if (txtDateStart2.getText().trim().isEmpty() || txtDateEnd2.getText().trim().isEmpty()) {
                showError("Veuillez saisir les dates debut et fin !");
                return false;
            }
        }
        return true;
    }

    private Date getStartDate2(String periode) {
        int currentYear = Year.now().getValue();

        if ("Toute l'annee en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-01-01");
        } else if ("Annee specifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-01-01");
        } else if ("Mois specifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-"
                    + String.format("%02d", Integer.parseInt(txtMois2.getText().trim())) + "-01");
        }
        return Date.valueOf(txtDateStart2.getText().trim());
    }

    private Date getEndDate2(String periode) {
        int currentYear = Year.now().getValue();

        if ("Toute l'annee en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-12-31");
        } else if ("Annee specifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-12-31");
        } else if ("Mois specifique".equals(periode)) {
            int mois = Integer.parseInt(txtMois2.getText().trim());
            int annee = Integer.parseInt(txtAnnee2.getText().trim());
            int lastDay = getLastDayOfMonth(mois, annee);
            return Date.valueOf(txtAnnee2.getText().trim() + "-"
                    + String.format("%02d", mois) + "-" + lastDay);
        }
        return Date.valueOf(txtDateEnd2.getText().trim());
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
        btnAppliquerFiltres2.setEnabled(!loading);
        btnReinitialiser2.setEnabled(!loading);
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
            new Complex2();
        });
    }
}
