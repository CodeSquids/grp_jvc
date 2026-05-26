package com.projet.Pages;

import com.projet.Services.VisiterService;
import com.projet.Tables.Complex1Model;
import com.projet.Tables.Complex2Model;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;

public class ComplexQueriesFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel complex1Panel;
    private JPanel complex2Panel;
    
    private JTable complex1Table;
    private JTable complex2Table;
    private Complex1Model complex1Model;
    private Complex2Model complex2Model;
    
    // Champs pour complex1
    private JTextField txtSiteNom;
    private JTextField txtDateStart1;
    private JTextField txtDateEnd1;
    private JButton btnSearch1;
    
    // Champs pour complex2
    private JTextField txtDateStart2;
    private JTextField txtDateEnd2;
    private JButton btnSearch2;
    
    private JButton btnRefreshAll;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    private static final Color COLOR_INFO = new Color(52, 152, 219);
    
    public ComplexQueriesFrame() {
        setTitle("📊 Requêtes Complexes - Gestion des Visites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel principal
        mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Initialiser les modèles
        complex1Model = new Complex1Model();
        complex2Model = new Complex2Model();
        
        // Créer les panels
        createComplex1Panel();
        createComplex2Panel();
        
        mainPanel.add(complex1Panel);
        mainPanel.add(complex2Panel);
        
        // Panel de statut
        JPanel statusPanel = createStatusPanel();
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Charger les données par défaut
        loadAllData();
    }
    
    private void createComplex1Panel() {
        complex1Panel = new JPanel(new BorderLayout(10, 10));
        complex1Panel.setBorder(createTitledBorder("🔍 Requête 1 : Visiteurs par site et période", COLOR_PRIMARY));
        complex1Panel.setBackground(Color.WHITE);
        
        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblSite = new JLabel("Nom du site :");
        lblSite.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSiteNom = new JTextField(20);
        txtSiteNom.setBorder(createTextFieldBorder());
        
        JLabel lblDateStart1 = new JLabel("Date début (YYYY-MM-DD) :");
        lblDateStart1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtDateStart1 = new JTextField(12);
        txtDateStart1.setBorder(createTextFieldBorder());
        
        JLabel lblDateEnd1 = new JLabel("Date fin :");
        lblDateEnd1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtDateEnd1 = new JTextField(12);
        txtDateEnd1.setBorder(createTextFieldBorder());
        
        btnSearch1 = createStyledButton("Rechercher", COLOR_SUCCESS);
        btnSearch1.addActionListener(e -> loadComplex1());
        
        searchPanel.add(lblSite);
        searchPanel.add(txtSiteNom);
        searchPanel.add(lblDateStart1);
        searchPanel.add(txtDateStart1);
        searchPanel.add(lblDateEnd1);
        searchPanel.add(txtDateEnd1);
        searchPanel.add(btnSearch1);
        
        // Table
        complex1Table = new JTable(complex1Model);
        complex1Table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        complex1Table.setRowHeight(25);
        complex1Table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        complex1Table.getTableHeader().setBackground(COLOR_PRIMARY);
        complex1Table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(complex1Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        complex1Panel.add(searchPanel, BorderLayout.NORTH);
        complex1Panel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createComplex2Panel() {
        complex2Panel = new JPanel(new BorderLayout(10, 10));
        complex2Panel.setBorder(createTitledBorder("📈 Requête 2 : Statistiques par site et période", COLOR_INFO));
        complex2Panel.setBackground(Color.WHITE);
        
        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblDateStart2 = new JLabel("Date début (YYYY-MM-DD) :");
        lblDateStart2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtDateStart2 = new JTextField(12);
        txtDateStart2.setBorder(createTextFieldBorder());
        
        JLabel lblDateEnd2 = new JLabel("Date fin :");
        lblDateEnd2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtDateEnd2 = new JTextField(12);
        txtDateEnd2.setBorder(createTextFieldBorder());
        
        btnSearch2 = createStyledButton("Rechercher", COLOR_SUCCESS);
        btnSearch2.addActionListener(e -> loadComplex2());
        
        btnRefreshAll = createStyledButton("🔄 Actualiser tout", COLOR_PRIMARY);
        btnRefreshAll.addActionListener(e -> loadAllData());
        
        searchPanel.add(lblDateStart2);
        searchPanel.add(txtDateStart2);
        searchPanel.add(lblDateEnd2);
        searchPanel.add(txtDateEnd2);
        searchPanel.add(btnSearch2);
        searchPanel.add(btnRefreshAll);
        
        // Table
        complex2Table = new JTable(complex2Model);
        complex2Table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        complex2Table.setRowHeight(25);
        complex2Table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        complex2Table.getTableHeader().setBackground(COLOR_INFO);
        complex2Table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(complex2Table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        complex2Panel.add(searchPanel, BorderLayout.NORTH);
        complex2Panel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        statusPanel.setBackground(new Color(240, 240, 240));
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        
        lblStatus = new JLabel("✅ Prêt");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        statusPanel.add(progressBar, BorderLayout.NORTH);
        statusPanel.add(lblStatus, BorderLayout.WEST);
        
        return statusPanel;
    }
    
    private void loadComplex1() {
        String siteNom = txtSiteNom.getText().trim();
        String dateStart = txtDateStart1.getText().trim();
        String dateEnd = txtDateEnd1.getText().trim();
        
        if (siteNom.isEmpty() || dateStart.isEmpty() || dateEnd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir tous les champs pour la requête 1 !",
                "Champs manquants",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setLoading(true);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Date startDate = Date.valueOf(dateStart);
                    Date endDate = Date.valueOf(dateEnd);
                    
                    JSONArray result = VisiterService.complex1(siteNom, startDate, endDate);
                    complex1Model.setComplex1(result);
                    
                    lblStatus.setText("✅ Requête 1 exécutée - " + result.length() + " résultat(s)");
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ComplexQueriesFrame.this,
                            "Format de date invalide ! Utilisez YYYY-MM-DD",
                            "Erreur de date",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    lblStatus.setText("❌ Erreur : Format de date invalide");
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ComplexQueriesFrame.this,
                            "Erreur lors de l'exécution de la requête 1 : " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    lblStatus.setText("❌ Erreur : " + e.getMessage());
                    e.printStackTrace();
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
    
    private void loadComplex2() {
        String dateStart = txtDateStart2.getText().trim();
        String dateEnd = txtDateEnd2.getText().trim();
        
        if (dateStart.isEmpty() || dateEnd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir les dates pour la requête 2 !",
                "Champs manquants",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setLoading(true);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Date startDate = Date.valueOf(dateStart);
                    Date endDate = Date.valueOf(dateEnd);
                    
                    JSONArray result = VisiterService.complex2(startDate, endDate);
                    complex2Model.setComplex2(result);
                    
                    lblStatus.setText("✅ Requête 2 exécutée - " + result.length() + " résultat(s)");
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ComplexQueriesFrame.this,
                            "Format de date invalide ! Utilisez YYYY-MM-DD",
                            "Erreur de date",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    lblStatus.setText("❌ Erreur : Format de date invalide");
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ComplexQueriesFrame.this,
                            "Erreur lors de l'exécution de la requête 2 : " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    lblStatus.setText("❌ Erreur : " + e.getMessage());
                    e.printStackTrace();
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
    
    private void loadAllData() {
        // Charger la requête 1 avec des valeurs par défaut
        if (!txtSiteNom.getText().trim().isEmpty() && 
            !txtDateStart1.getText().trim().isEmpty() && 
            !txtDateEnd1.getText().trim().isEmpty()) {
            loadComplex1();
        }
        
        // Charger la requête 2 avec des valeurs par défaut
        if (!txtDateStart2.getText().trim().isEmpty() && 
            !txtDateEnd2.getText().trim().isEmpty()) {
            loadComplex2();
        }
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisible(loading);
        btnSearch1.setEnabled(!loading);
        btnSearch2.setEnabled(!loading);
        btnRefreshAll.setEnabled(!loading);
        
        if (loading) {
            progressBar.setIndeterminate(true);
            lblStatus.setText("⏳ Chargement en cours...");
        } else {
            progressBar.setIndeterminate(false);
        }
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