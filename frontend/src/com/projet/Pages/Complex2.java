package com.projet.Pages;

import com.projet.Services.VisiterService;
import com.projet.Tables.Complex2Model;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

    private static final Color COLOR_INFO = new Color(52, 152, 219);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_WARNING = new Color(243, 156, 18);
    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);

    public Complex2() {
        setTitle("HereVisit : Gestion des Visites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 950);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new Header(this, Header.ActivePage.COMPLEX2);
        headerPanel.setPreferredSize(new Dimension(200, getHeight()));
        mainPanel.add(headerPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        complex2Model = new Complex2Model();
        createComplex2Panel();
        centerPanel.add(complex2Panel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
        chargerToutesStats();
    }

    private void createComplex2Panel() {
        complex2Panel = new JPanel(new BorderLayout(10, 10));
        complex2Panel.setBorder(createTitledBorder("REQUETE 2 : Effectif et montant total par site", COLOR_INFO));
        complex2Panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new BorderLayout(10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(createLabel("Periode :"));
        cmbPeriode2 = new JComboBox<>(new String[]{"Toute l'annee en cours", "Annee specifique", "Mois specifique", "Entre 2 dates"});
        cmbPeriode2.setPreferredSize(new Dimension(180, 30));
        cmbPeriode2.addActionListener(e -> updateDynamicFields2());
        topPanel.add(cmbPeriode2);

        dynamicPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        dynamicPanel2.setBackground(Color.WHITE);

        txtAnnee2 = new JTextField(10);
        txtMois2 = new JTextField(10);
        txtDateStart2 = new JTextField(10);
        txtDateEnd2 = new JTextField(10);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        btnAppliquerFiltres2 = createStyledButton("Appliquer les filtres", COLOR_SUCCESS);
        btnAppliquerFiltres2.addActionListener(e -> appliquerFiltres2());

        btnReinitialiser2 = createStyledButton("Reinitialiser (Toutes les donnees)", COLOR_WARNING);
        btnReinitialiser2.addActionListener(e -> chargerToutesStats());

        buttonPanel.add(btnAppliquerFiltres2);
        buttonPanel.add(btnReinitialiser2);

        filterPanel.add(topPanel, BorderLayout.NORTH);
        filterPanel.add(dynamicPanel2, BorderLayout.CENTER);
        filterPanel.add(buttonPanel, BorderLayout.SOUTH);

        complex2Table = new JTable(complex2Model);
        complex2Table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        complex2Table.setRowHeight(25);
        complex2Table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        complex2Table.getTableHeader().setBackground(COLOR_INFO);
        complex2Table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(complex2Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        complex2Panel.add(filterPanel, BorderLayout.NORTH);
        complex2Panel.add(scrollPane, BorderLayout.CENTER);

        updateDynamicFields2();
    }

    private void updateDynamicFields2() {
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
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    JSONArray result = VisiterService.complex4();
                    complex2Model.setComplex2(result);

                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("Aucune statistique disponible");
                    } else {
                        lblStatus.setText("Toutes les statistiques chargees - " + count + " site(s) trouve(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                    complex2Model.setComplex2(new JSONArray());
                }
                return null;
            }

            @Override
            protected void done() {
                setLoading(false);
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

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    JSONArray result;

                    if ("Toute l'annee en cours".equals(periode)) {
                        result = VisiterService.complex4();
                    } else {
                        Date startDate = getStartDate2(periode);
                        Date endDate = getEndDate2(periode);
                        result = VisiterService.complex2(startDate, endDate);
                    }

                    complex2Model.setComplex2(result);

                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("Aucune statistique pour les filtres selectionnes");
                    } else {
                        lblStatus.setText("Statistiques filtrees - " + count + " site(s) trouve(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("Erreur: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                setLoading(false);
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
            return Date.valueOf(txtAnnee2.getText().trim() + "-" +
                    String.format("%02d", Integer.parseInt(txtMois2.getText().trim())) + "-01");
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
            return Date.valueOf(txtAnnee2.getText().trim() + "-" +
                    String.format("%02d", mois) + "-" + lastDay);
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
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        statusPanel.setBackground(COLOR_HEADER_BG);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);

        lblStatus = new JLabel("Chargement des donnees en cours...");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);

        statusPanel.add(progressBar, BorderLayout.NORTH);
        statusPanel.add(lblStatus, BorderLayout.WEST);

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

    private TitledBorder createTitledBorder(String title, Color color) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, 2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                color
        );
        border.setTitleColor(color);
        return border;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
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
