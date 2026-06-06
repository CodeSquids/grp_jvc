package com.projet;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import com.projet.Pages.SitePage;
import com.projet.Pages.VisiterPage;
import com.projet.Pages.Header;
import com.projet.Pages.Complex1;
import com.projet.Tables.VisiteurTableModel;
import com.projet.Dialog.*;
import com.projet.Services.*;
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
    
    // Palette couleurs élégantes et sophistiquées
    private static final Color COLOR_BACKGROUND = new Color(250, 248, 245);  // Beige clair élégant
    private static final Color COLOR_PANEL = new Color(255, 255, 255);       // Blanc pur
    private static final Color COLOR_BORDER = new Color(210, 200, 190);       // Beige grisé
    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);       // Gris profond élégant
    private static final Color COLOR_ACCENT = new Color(160, 130, 110);       // Brun élégant
    private static final Color COLOR_BUTTON_ADD = new Color(200, 230, 210);   // Vert sauge clair
    private static final Color COLOR_BUTTON_EDIT = new Color(210, 195, 220);  // Violet doux clair
    private static final Color COLOR_BUTTON_DELETE = new Color(230, 195, 195);// Rouge terreux clair
    private static final Color COLOR_BUTTON_REFRESH = new Color(200, 210, 220);// Gris bleuté clair
    private static final Color COLOR_BUTTON_EXPORT = new Color(230, 210, 180); // Or cuivré clair
    private static final Color COLOR_TABLE_HEADER = new Color(55, 50, 65);    // Header tableau
    private static final Color COLOR_ROW_ODD = new Color(255, 255, 255);      // Lignes impaires
    private static final Color COLOR_ROW_EVEN = new Color(248, 245, 242);     // Lignes paires
    
    public Main() {
        setTitle("HereVisit : Gestion des Visites");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 950);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);
        
        JPanel headerPanel = new Header(
            this,
            Header.ActivePage.VISITORS
        );
        headerPanel.setPreferredSize(new Dimension(200, getHeight()));
        mainPanel.add(headerPanel, BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(1000, 0));
        
        JPanel searchPanel = createSearchPanel();
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tableau + Boutons
        JPanel tableContainer = createTableContainer();
        centerPanel.add(tableContainer, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setupKeyboardShortcuts();
        loadVisiteurs();
        updateStatus("Système prêt", "success");
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Recherche avancée");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);
        
        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchControls.setOpaque(false);
        
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        JLabel lblSearchBy = new JLabel("Rechercher par :");
        lblSearchBy.setFont(labelFont);
        lblSearchBy.setForeground(Color.BLACK);
        
        cmbSearchType = new JComboBox<>(new String[]{"📋 Numéro", "👤 Nom"});
        cmbSearchType.setFont(fieldFont);
        cmbSearchType.setBackground(COLOR_PANEL);
        cmbSearchType.setForeground(Color.BLACK);
        cmbSearchType.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        
        JLabel lblValue = new JLabel("Valeur :");
        lblValue.setFont(labelFont);
        lblValue.setForeground(Color.BLACK);
        
        txtSearch = new JTextField(25);
        txtSearch.setFont(fieldFont);
        txtSearch.setForeground(Color.BLACK);
        txtSearch.setBackground(COLOR_PANEL);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton btnSearch = createElegantButton("🔍 Rechercher", COLOR_ACCENT);
        btnSearch.addActionListener(e -> searchVisiteur());
        
        JButton btnReset = createElegantButton("🔄 Réinitialiser", new Color(200, 195, 190));
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
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Titre avec compteur
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Liste des visiteurs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);
        
        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecordCount.setForeground(Color.DARK_GRAY);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);
        
        // Tableau
        tableModel = new VisiteurTableModel();
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(false);
        table.setBackground(COLOR_PANEL);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(160, 130, 110, 40));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_TABLE_HEADER);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        
        // Boutons d'action
        JPanel buttonPanel = createActionButtonPanel();
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createActionButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnAdd = createElegantButton("AJOUTER UN VISITEUR", COLOR_BUTTON_ADD);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.addActionListener(e -> addVisiteur());
        
        JButton btnEdit = createElegantButton("MODIFIER", COLOR_BUTTON_EDIT);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.addActionListener(e -> editVisiteur());
        
        JButton btnDelete = createElegantButton("SUPPRIMER", COLOR_BUTTON_DELETE);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.addActionListener(e -> deleteVisiteur());
        
        JButton btnRefresh = createElegantButton("ACTUALISER", COLOR_BUTTON_REFRESH);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.addActionListener(e -> loadVisiteurs());
        
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);

        //
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        
        lblStatus = new JLabel("✅ Système prêt");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(150, 12));
        progressBar.setBackground(new Color(80, 75, 85));
        progressBar.setForeground(COLOR_ACCENT);
        
        panel.add(lblStatus, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createElegantButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.BLACK);  // Texte en NOIR
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bordure élégante avec contour foncé pour meilleur contraste
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(9, 21, 9, 21)
        ));
        
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
            case "success" -> "✓ ";
            case "error" -> "✗ ";
            case "warning" -> "⚠ ";
            default -> "ℹ ";
        };
        lblStatus.setText(icon + message);
        
        if (!type.equals("error")) {
            Timer timer = new Timer(3000, e -> lblStatus.setText("✓ Système prêt"));
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

    private void openSitePage() {
        SitePage sitePage = new SitePage();
        sitePage.setVisible(true);
        dispose();
    }

    private void openVisiterPage() {
        VisiterPage visiterPage = new VisiterPage();
        visiterPage.setVisible(true);
        dispose();
    }
    
    //
    private void openCompPage() {
        Complex1 compPage = new Complex1();
        compPage.setVisible(true);
        dispose();
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
