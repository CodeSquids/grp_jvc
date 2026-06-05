package com.projet.Pages;

import com.projet.Services.VisiterService;
import com.projet.Tables.Complex1Model;
import com.projet.Tables.Complex2Model;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

public class ComplexQueriesFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel;
    private JPanel complex1Panel;
    private JPanel complex2Panel;
    
    private JTable complex1Table;
    private JTable complex2Table;
    private Complex1Model complex1Model;
    private Complex2Model complex2Model;
    
    // Filtres pour complex1
    private JComboBox<String> cmbFiltreSite1;
    private JComboBox<String> cmbPeriode1;
    private JTextField txtAnnee1;
    private JTextField txtMois1;
    private JTextField txtDateStart1;
    private JTextField txtDateEnd1;
    private JPanel dynamicPanel1;
    private JButton btnAppliquerFiltres1;
    private JButton btnReinitialiser1;
    
    // Filtres pour complex2
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
    //
    private JLabel lblTotal1;  // Ajouter avec les autres composants
    
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_WARNING = new Color(243, 156, 18);
    private static final Color COLOR_INFO = new Color(52, 152, 219);
    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);       // Gris profond élégant

    // Liste des sites (sera chargée dynamiquement)
    private String[] sites = {"Tous les sites"};
    
    public ComplexQueriesFrame() {
        setTitle("HereVisit : Gestion des Visites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 950);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new Header(
                this,
                Header.ActivePage.COMPLEX
        );
        headerPanel.setPreferredSize(new Dimension(200, getHeight()));
        mainPanel.add(headerPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(1000, 0));

        complex1Model = new Complex1Model();
        complex2Model = new Complex2Model();
        chargerListeSites();
        createComplex1Panel();
        createComplex2Panel();

        centerPanel.add(complex1Panel, BorderLayout.NORTH);
        centerPanel.add(complex2Panel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
        chargerToutesDonnees();
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
        complex1Panel.setBorder(createTitledBorder("📋 REQUÊTE 1 : Liste des visiteurs par site et période", COLOR_PRIMARY));
        complex1Panel.setBackground(Color.WHITE);
        
        // Panel de filtres
        JPanel filterPanel = new JPanel(new BorderLayout(10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Ligne 1 : Filtres principaux
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPanel.setBackground(Color.WHITE);
        
        topPanel.add(createLabel("Filtrer par site :"));
        cmbFiltreSite1 = new JComboBox<>(sites);
        cmbFiltreSite1.setPreferredSize(new Dimension(180, 30));
        topPanel.add(cmbFiltreSite1);
        
        topPanel.add(createLabel("Période :"));
        cmbPeriode1 = new JComboBox<>(new String[]{"Toute l'année en cours", "Année spécifique", "Mois spécifique", "Entre 2 dates"});
        cmbPeriode1.setPreferredSize(new Dimension(180, 30));
        cmbPeriode1.addActionListener(e -> updateDynamicFields1());
        topPanel.add(cmbPeriode1);
        
        // Ligne 2 : Champs dynamiques pour la période
        dynamicPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        dynamicPanel1.setBackground(Color.WHITE);
        
        txtAnnee1 = new JTextField(10);
        txtMois1 = new JTextField(10);
        txtDateStart1 = new JTextField(10);
        txtDateEnd1 = new JTextField(10);
        
        // Ligne 3 : Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAppliquerFiltres1 = createStyledButton("🔍 Appliquer les filtres", COLOR_SUCCESS);
        btnAppliquerFiltres1.addActionListener(e -> appliquerFiltres1());
        
        btnReinitialiser1 = createStyledButton("🔄 Réinitialiser (Toutes les données)", COLOR_WARNING);
        btnReinitialiser1.addActionListener(e -> chargerToutesVisites());
        
        buttonPanel.add(btnAppliquerFiltres1);
        buttonPanel.add(btnReinitialiser1);
        
        filterPanel.add(topPanel, BorderLayout.NORTH);
        filterPanel.add(dynamicPanel1, BorderLayout.CENTER);
        filterPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table
        complex1Table = new JTable(complex1Model);
        complex1Table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        complex1Table.setRowHeight(25);
        complex1Table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        complex1Table.getTableHeader().setBackground(COLOR_PRIMARY);
        complex1Table.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(complex1Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        lblTotal1 = new JLabel("Total: 0,00 Ar");
        lblTotal1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal1.setForeground(COLOR_PRIMARY);
        bottomPanel.add(lblTotal1);

        complex1Panel.add(bottomPanel, BorderLayout.SOUTH);
        complex1Panel.add(filterPanel, BorderLayout.NORTH);
        complex1Panel.add(scrollPane, BorderLayout.CENTER);
        updateDynamicFields1();
    }
    
    private void createComplex2Panel() {
        complex2Panel = new JPanel(new BorderLayout(10, 10));
        complex2Panel.setBorder(createTitledBorder("📊 REQUÊTE 2 : Effectif et montant total par site", COLOR_INFO));
        complex2Panel.setBackground(Color.WHITE);
        
        // Panel de filtres
        JPanel filterPanel = new JPanel(new BorderLayout(10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Ligne 1 : Filtres
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPanel.setBackground(Color.WHITE);
        
        topPanel.add(createLabel("Période :"));
        cmbPeriode2 = new JComboBox<>(new String[]{"Toute l'année en cours", "Année spécifique", "Mois spécifique", "Entre 2 dates"});
        cmbPeriode2.setPreferredSize(new Dimension(180, 30));
        cmbPeriode2.addActionListener(e -> updateDynamicFields2());
        topPanel.add(cmbPeriode2);
        
        // Ligne 2 : Champs dynamiques
        dynamicPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        dynamicPanel2.setBackground(Color.WHITE);
        
        txtAnnee2 = new JTextField(10);
        txtMois2 = new JTextField(10);
        txtDateStart2 = new JTextField(10);
        txtDateEnd2 = new JTextField(10);
        
        // Ligne 3 : Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAppliquerFiltres2 = createStyledButton("🔍 Appliquer les filtres", COLOR_SUCCESS);
        btnAppliquerFiltres2.addActionListener(e -> appliquerFiltres2());
        
        btnReinitialiser2 = createStyledButton("🔄 Réinitialiser (Toutes les données)", COLOR_WARNING);
        btnReinitialiser2.addActionListener(e -> chargerToutesStats());
        
        buttonPanel.add(btnAppliquerFiltres2);
        buttonPanel.add(btnReinitialiser2);
        
        filterPanel.add(topPanel, BorderLayout.NORTH);
        filterPanel.add(dynamicPanel2, BorderLayout.CENTER);
        filterPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table
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
    
    private void updateDynamicFields1() {
        dynamicPanel1.removeAll();
        String periode = (String) cmbPeriode1.getSelectedItem();
        
        if ("Année spécifique".equals(periode)) {
            dynamicPanel1.add(createLabel("Année (YYYY) :"));
            dynamicPanel1.add(txtAnnee1);
            txtAnnee1.setPreferredSize(new Dimension(120, 30));
            txtAnnee1.setText(String.valueOf(Year.now().getValue()));
            txtAnnee1.setToolTipText("Exemple: 2024");
        } else if ("Mois spécifique".equals(periode)) {
            dynamicPanel1.add(createLabel("Année (YYYY) :"));
            dynamicPanel1.add(txtAnnee1);
            dynamicPanel1.add(createLabel("Mois (1-12) :"));
            dynamicPanel1.add(txtMois1);
            txtAnnee1.setPreferredSize(new Dimension(100, 30));
            txtMois1.setPreferredSize(new Dimension(80, 30));
            txtAnnee1.setText(String.valueOf(Year.now().getValue()));
            txtMois1.setText(String.valueOf(LocalDate.now().getMonthValue()));
        } else if ("Entre 2 dates".equals(periode)) {
            dynamicPanel1.add(createLabel("Date début (YYYY-MM-DD) :"));
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
    
    private void updateDynamicFields2() {
        dynamicPanel2.removeAll();
        String periode = (String) cmbPeriode2.getSelectedItem();
        
        if ("Année spécifique".equals(periode)) {
            dynamicPanel2.add(createLabel("Année (YYYY) :"));
            dynamicPanel2.add(txtAnnee2);
            txtAnnee2.setPreferredSize(new Dimension(120, 30));
            txtAnnee2.setText(String.valueOf(Year.now().getValue()));
        } else if ("Mois spécifique".equals(periode)) {
            dynamicPanel2.add(createLabel("Année (YYYY) :"));
            dynamicPanel2.add(txtAnnee2);
            dynamicPanel2.add(createLabel("Mois (1-12) :"));
            dynamicPanel2.add(txtMois2);
            txtAnnee2.setPreferredSize(new Dimension(100, 30));
            txtMois2.setPreferredSize(new Dimension(80, 30));
            txtAnnee2.setText(String.valueOf(Year.now().getValue()));
            txtMois2.setText(String.valueOf(LocalDate.now().getMonthValue()));
        } else if ("Entre 2 dates".equals(periode)) {
            dynamicPanel2.add(createLabel("Date début (YYYY-MM-DD) :"));
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
    
    private void chargerToutesDonnees() {
        chargerToutesVisites();
        chargerToutesStats();
    }
    
    private void chargerToutesVisites() {
        setLoading(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Date startDate = Date.valueOf("2000-01-01");
                    Date endDate = Date.valueOf(LocalDate.now().plusYears(10).toString());
                    String siteNom = "Tous les sites";
                    
                    JSONArray result = VisiterService.complex1(siteNom, startDate, endDate);
                    complex1Model.setComplex1(result);
                    
                    // Mettre à jour le total
                    double total = complex1Model.getTotalMontant();
                    SwingUtilities.invokeLater(() -> {
                        lblTotal1.setText("Total: " + String.format("%,.2f", total) + " Ar");
                    });
                    
                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("ℹ️ Aucune visite trouvée dans la base de données");
                    } else {
                        lblStatus.setText("✅ Toutes les visites chargées - " + count + " visiteur(s) trouvé(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("❌ Erreur: " + e.getMessage());
                    complex1Model.setComplex1(new JSONArray());
                    SwingUtilities.invokeLater(() -> {
                        lblTotal1.setText("Total: 0,00 Ar");
                    });
                }
                return null;
            }
            @Override
            protected void done() { setLoading(false); }
        };
        worker.execute();
    }
    //
    private void chargerToutesStats() {
        setLoading(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    JSONArray result = VisiterService.complex4();
                    complex2Model.setComplex2(result);
                    
                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("ℹ️ Aucune statistique disponible");
                    } else {
                        lblStatus.setText("✅ Toutes les statistiques chargées - " + count + " site(s) trouvé(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("❌ Erreur: " + e.getMessage());
                    complex2Model.setComplex2(new JSONArray());
                }
                return null;
            }
            @Override
            protected void done() { setLoading(false); }
        };
        worker.execute();
    }
    
private void appliquerFiltres1() {
        String siteNom = (String) cmbFiltreSite1.getSelectedItem();
        String periode = (String) cmbPeriode1.getSelectedItem();
        
        if (!validerPeriodes1(periode)) return;
        
        setLoading(true);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Date startDate = getStartDate1(periode);
                    Date endDate = getEndDate1(periode);
                    
                    JSONArray result = VisiterService.complex1(siteNom, startDate, endDate);
                    complex1Model.setComplex1(result);
                    
                    // Mettre à jour le total
                    double total = complex1Model.getTotalMontant();
                    SwingUtilities.invokeLater(() -> {
                        lblTotal1.setText("Total: " + String.format("%,.2f", total) + " Ar");
                    });
                    
                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("⚠️ Aucun résultat pour les filtres sélectionnés");
                    } else {
                        lblStatus.setText("✅ Filtres appliqués - " + count + " visiteur(s) trouvé(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("❌ Erreur: " + e.getMessage());
                }
                return null;
            }
            @Override
            protected void done() { setLoading(false); }
        };
        worker.execute();
    }
    
    //
    
    private void appliquerFiltres2() {
        String periode = (String) cmbPeriode2.getSelectedItem();
        
        if (!validerPeriodes2(periode)) return;
        
        setLoading(true);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    JSONArray result;
                    
                    if ("Toute l'année en cours".equals(periode)) {
                        result = VisiterService.complex4();
                    } else {
                        Date startDate = getStartDate2(periode);
                        Date endDate = getEndDate2(periode);

                            result = VisiterService.complex2(startDate, endDate);
                    }
                    
                    complex2Model.setComplex2(result);
                    
                    int count = result.length();
                    if (count == 0) {
                        lblStatus.setText("⚠️ Aucune statistique pour les filtres sélectionnés");
                    } else {
                        lblStatus.setText("✅ Statistiques filtrées - " + count + " site(s) trouvé(s)");
                    }
                } catch (Exception e) {
                    lblStatus.setText("❌ Erreur: " + e.getMessage());
                }
                return null;
            }
            @Override
            protected void done() { setLoading(false); }
        };
        worker.execute();
    }
    
    private JSONArray agregerStatsParSite(JSONArray visites, String nomSite) throws Exception {
        JSONArray result = new JSONArray();
        JSONObject stats = new JSONObject();
        
        int effectif = 0;
        double montantTotal = 0;
        String nSite = "";
        
        for (int i = 0; i < visites.length(); i++) {
            JSONObject visite = visites.getJSONObject(i);
            if (i == 0) {
                nSite = visite.optString("n_site", "");
            }
            effectif++;
            montantTotal += visite.optDouble("montant", 0);
        }
        
        stats.put("n_site", nSite);
        stats.put("nom_site", nomSite);
        stats.put("effectif", effectif);
        stats.put("montant", montantTotal);
        result.put(stats);
        
        return result;
    }
    
    private boolean validerPeriodes1(String periode) {
        if ("Année spécifique".equals(periode)) {
            if (txtAnnee1.getText().trim().isEmpty()) {
                showError("Veuillez saisir une année !");
                return false;
            }
        } else if ("Mois spécifique".equals(periode)) {
            if (txtAnnee1.getText().trim().isEmpty() || txtMois1.getText().trim().isEmpty()) {
                showError("Veuillez saisir l'année et le mois !");
                return false;
            }
        } else if ("Entre 2 dates".equals(periode)) {
            if (txtDateStart1.getText().trim().isEmpty() || txtDateEnd1.getText().trim().isEmpty()) {
                showError("Veuillez saisir les dates début et fin !");
                return false;
            }
        }
        return true;
    }
    
    private boolean validerPeriodes2(String periode) {
        if ("Année spécifique".equals(periode)) {
            if (txtAnnee2.getText().trim().isEmpty()) {
                showError("Veuillez saisir une année !");
                return false;
            }
        } else if ("Mois spécifique".equals(periode)) {
            if (txtAnnee2.getText().trim().isEmpty() || txtMois2.getText().trim().isEmpty()) {
                showError("Veuillez saisir l'année et le mois !");
                return false;
            }
        } else if ("Entre 2 dates".equals(periode)) {
            if (txtDateStart2.getText().trim().isEmpty() || txtDateEnd2.getText().trim().isEmpty()) {
                showError("Veuillez saisir les dates début et fin !");
                return false;
            }
        }
        return true;
    }
    
    private Date getStartDate1(String periode) {
        int currentYear = Year.now().getValue();
        
        if ("Toute l'année en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-01-01");
        } else if ("Année spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-01-01");
        } else if ("Mois spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-" + 
                String.format("%02d", Integer.parseInt(txtMois1.getText().trim())) + "-01");
        } else {
            return Date.valueOf(txtDateStart1.getText().trim());
        }
    }
    
    private Date getEndDate1(String periode) {
        int currentYear = Year.now().getValue();
        LocalDate now = LocalDate.now();
        
        if ("Toute l'année en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-12-31");
        } else if ("Année spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee1.getText().trim() + "-12-31");
        } else if ("Mois spécifique".equals(periode)) {
            int mois = Integer.parseInt(txtMois1.getText().trim());
            int lastDay = getLastDayOfMonth(mois, Integer.parseInt(txtAnnee1.getText().trim()));
            return Date.valueOf(txtAnnee1.getText().trim() + "-" + 
                String.format("%02d", mois) + "-" + lastDay);
        } else {
            return Date.valueOf(txtDateEnd1.getText().trim());
        }
    }
    
    private Date getStartDate2(String periode) {
        int currentYear = Year.now().getValue();
        
        if ("Toute l'année en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-01-01");
        } else if ("Année spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-01-01");
        } else if ("Mois spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-" + 
                String.format("%02d", Integer.parseInt(txtMois2.getText().trim())) + "-01");
        } else {
            return Date.valueOf(txtDateStart2.getText().trim());
        }
    }
    
    private Date getEndDate2(String periode) {
        int currentYear = Year.now().getValue();
        
        if ("Toute l'année en cours".equals(periode)) {
            return Date.valueOf(currentYear + "-12-31");
        } else if ("Année spécifique".equals(periode)) {
            return Date.valueOf(txtAnnee2.getText().trim() + "-12-31");
        } else if ("Mois spécifique".equals(periode)) {
            int mois = Integer.parseInt(txtMois2.getText().trim());
            int annee = Integer.parseInt(txtAnnee2.getText().trim());
            int lastDay = getLastDayOfMonth(mois, annee);
            return Date.valueOf(txtAnnee2.getText().trim() + "-" + 
                String.format("%02d", mois) + "-" + lastDay);
        } else {
            return Date.valueOf(txtDateEnd2.getText().trim());
        }
    }
    
    private int getLastDayOfMonth(int mois, int annee) {
        if (mois == 2) {
            // Gestion des années bissextiles
            if ((annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0)) {
                return 29;
            }
            return 28;
        } else if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
            return 30;
        } else {
            return 31;
        }
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
        
        lblStatus = new JLabel("✅ Chargement des données en cours...");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);

        statusPanel.add(progressBar, BorderLayout.NORTH);
        statusPanel.add(lblStatus, BorderLayout.WEST);
        
        return statusPanel;
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisible(loading);
        btnAppliquerFiltres1.setEnabled(!loading);
        btnAppliquerFiltres2.setEnabled(!loading);
        btnReinitialiser1.setEnabled(!loading);
        btnReinitialiser2.setEnabled(!loading);
        
        if (loading) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message, "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        });
    }
    
    private Border createTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );
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
            new ComplexQueriesFrame();
        });
    }
}
