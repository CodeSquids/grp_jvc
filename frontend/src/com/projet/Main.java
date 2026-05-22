package com.projet;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main extends JFrame {
    private JTable table;
    private VisiteurTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JLabel lblRecordCount;
    
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    private static final Color COLOR_WARNING = new Color(241, 196, 15);
    private static final Color COLOR_BACKGROUND = new Color(236, 240, 241);
    private static final Color COLOR_HEADER = new Color(52, 73, 94);
    
    public Main() {
        setTitle("Gestion des Visiteurs - Système de Gestion des Sites Touristiques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);
        
        // Header (HAUT)
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central qui contient recherche + tableau + boutons
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        
        // Recherche
        JPanel searchPanel = createSearchPanel();
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tableau + Boutons
        JPanel tableContainer = createTableContainer();
        centerPanel.add(tableContainer, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Status bar (BAS)
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setupKeyboardShortcuts();
        loadVisiteurs();
        updateStatus("Système prêt", "success");
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("GESTION DES VISITEURS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Système de gestion des sites touristiques");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(189, 195, 199));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        JLabel logoLabel = new JLabel("🏛️");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        logoLabel.setForeground(Color.WHITE);
        
        header.add(logoLabel, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Recherche avancée");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_PRIMARY);
        
        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchControls.setOpaque(false);
        
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        
        JLabel lblSearchBy = new JLabel("Rechercher par :");
        lblSearchBy.setFont(labelFont);
        
        cmbSearchType = new JComboBox<>(new String[]{"📋 Numéro", "👤 Nom"});
        cmbSearchType.setFont(fieldFont);
        
        JLabel lblValue = new JLabel("Valeur :");
        lblValue.setFont(labelFont);
        
        txtSearch = new JTextField(25);
        txtSearch.setFont(fieldFont);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JButton btnSearch = createStyledButton("🔍 Rechercher", COLOR_PRIMARY);
        btnSearch.addActionListener(e -> searchVisiteur());
        
        JButton btnReset = createStyledButton("🔄 Réinitialiser", COLOR_WARNING);
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadVisiteurs();
        });
        
        searchControls.add(lblSearchBy);
        searchControls.add(cmbSearchType);
        searchControls.add(lblValue);
        searchControls.add(txtSearch);
        searchControls.add(btnSearch);
        searchControls.add(btnReset);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(searchControls, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTableContainer() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Titre avec compteur
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Liste des visiteurs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_PRIMARY);
        
        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRecordCount.setForeground(Color.GRAY);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);
        
        // Tableau
        tableModel = new VisiteurTableModel();
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(41, 128, 185, 50));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Boutons d'action
        JPanel buttonPanel = createActionButtonPanel();
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createActionButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Boutons avec textes plus visibles et impactants
        JButton btnAdd = createStyledButton("AJOUTER UN VISITEUR", COLOR_SUCCESS);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.addActionListener(e -> addVisiteur());
        
        JButton btnEdit = createStyledButton("MODIFIER", COLOR_PRIMARY);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.addActionListener(e -> editVisiteur());
        
        JButton btnDelete = createStyledButton("SUPPRIMER", COLOR_DANGER);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.addActionListener(e -> deleteVisiteur());
        
        JButton btnRefresh = createStyledButton("ACTUALISER", COLOR_HEADER);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.addActionListener(e -> loadVisiteurs());
        
        JButton btnExport = createStyledButton("EXPORTER", new Color(155, 89, 182));
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExport.addActionListener(e -> exportToExcel());
        
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        panel.add(btnExport);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        lblStatus = new JLabel("✅ Système prêt");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(Color.WHITE);
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(150, 15));
        
        panel.add(lblStatus, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
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
    
    private void setupKeyboardShortcuts() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");
        getRootPane().getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVisiteurs();
            }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), "focusSearch");
        getRootPane().getActionMap().put("focusSearch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.requestFocus();
            }
        });
    }
    
    private void updateStatus(String message, String type) {
        String icon = switch (type) {
            case "success" -> "✅ ";
            case "error" -> "❌ ";
            case "warning" -> "⚠️ ";
            default -> "ℹ️ ";
        };
        lblStatus.setText(icon + message);
        
        if (!type.equals("error")) {
            Timer timer = new Timer(3000, e -> lblStatus.setText("✅ Système prêt"));
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private void loadVisiteurs() {
        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiteurService.getAllVisiteurs();
            }
            
            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setVisiteurs(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    updateStatus("Données chargées (" + result.length() + " visiteurs)", "success");
                } catch (Exception e) {
                    updateStatus("Erreur de connexion au serveur", "error");
                    JOptionPane.showMessageDialog(Main.this, 
                        "Impossible de charger les données.\nVérifiez que le serveur backend est démarré sur http://localhost:5000",
                        "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }
    
    private void searchVisiteur() {
        String critere = cmbSearchType.getSelectedIndex() == 0 ? "numero" : "nom";
        String valeur = txtSearch.getText().trim();
        
        if (valeur.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer une valeur de recherche !", 
                "Recherche", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiteurService.searchVisiteur(critere, valeur);
            }
            
            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setVisiteurs(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    if (result.length() == 0) {
                        updateStatus("Aucun résultat pour: " + valeur, "warning");
                    } else {
                        updateStatus(result.length() + " visiteur(s) trouvé(s)", "success");
                    }
                } catch (Exception e) {
                    updateStatus("Erreur de recherche", "error");
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }
    
    private void addVisiteur() {
        VisiteurCRUDDialog dialog = new VisiteurCRUDDialog(this, "add", null, null, null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiteurService.createVisiteur(
                        dialog.getNumero(), 
                        dialog.getNom(), 
                        dialog.getAdresse()
                    );
                }
                
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visiteur ajouté avec succès !", "success");
                            loadVisiteurs();
                            txtSearch.setText("");
                        } else {
                            updateStatus("Erreur lors de l'ajout", "error");
                        }
                    } catch (Exception e) {
                        updateStatus("Erreur: " + e.getMessage(), "error");
                    } finally {
                        showProgress(false);
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void editVisiteur() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un visiteur à modifier !", 
                "Modification", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JSONObject visiteur = tableModel.getVisiteurAt(selectedRow);
        VisiteurCRUDDialog dialog = new VisiteurCRUDDialog(this, "edit", 
            visiteur.getString("n_visiteur"),
            visiteur.getString("nom"),
            visiteur.getString("adresse"));
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiteurService.updateVisiteur(
                        dialog.getNumero(),
                        dialog.getNom(),
                        dialog.getAdresse()
                    );
                }
                
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visiteur modifié avec succès !", "success");
                            loadVisiteurs();
                        } else {
                            updateStatus("Erreur lors de la modification", "error");
                        }
                    } catch (Exception e) {
                        updateStatus("Erreur: " + e.getMessage(), "error");
                    } finally {
                        showProgress(false);
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void deleteVisiteur() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un visiteur à supprimer !", 
                "Suppression", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JSONObject visiteur = tableModel.getVisiteurAt(selectedRow);
        String numero = visiteur.getString("n_visiteur");
        String nom = visiteur.getString("nom");
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Supprimer le visiteur :\n\n📋 N°: " + numero + "\n👤 Nom: " + nom + "\n\n⚠️ Cette action est irréversible !",
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiteurService.deleteVisiteur(numero);
                }
                
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visiteur supprimé avec succès !", "success");
                            loadVisiteurs();
                            txtSearch.setText("");
                        } else {
                            updateStatus("Impossible de supprimer (visites associées)", "error");
                        }
                    } catch (Exception e) {
                        updateStatus("Erreur: " + e.getMessage(), "error");
                    } finally {
                        showProgress(false);
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void exportToExcel() {
        JOptionPane.showMessageDialog(this,
            "Fonctionnalité d'export Excel\n\n" +
            "Données prêtes à être exportées : " + tableModel.getRowCount() + " enregistrements",
            "Export", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisible(show);
        if (show) {
            progressBar.setIndeterminate(true);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}