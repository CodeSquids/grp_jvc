package com.projet.Pages;

import com.projet.Dialog.SiteCRUDDialog;
import com.projet.Main;
import com.projet.Services.SiteService;
import com.projet.Tables.SiteTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SitePage extends JFrame {
    private JTable table;
    private SiteTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JLabel lblRecordCount;

    private static final Color COLOR_BACKGROUND = new Color(250, 248, 245);
    private static final Color COLOR_PANEL = new Color(255, 255, 255);
    private static final Color COLOR_BORDER = new Color(210, 200, 190);
    private static final Color COLOR_HEADER_BG = new Color(45, 40, 55);
    private static final Color COLOR_ACCENT = new Color(160, 130, 110);
    private static final Color COLOR_BUTTON_ADD = new Color(200, 230, 210);
    private static final Color COLOR_BUTTON_EDIT = new Color(210, 195, 220);
    private static final Color COLOR_BUTTON_DELETE = new Color(230, 195, 195);
    private static final Color COLOR_BUTTON_REFRESH = new Color(200, 210, 220);
    private static final Color COLOR_BUTTON_EXPORT = new Color(230, 210, 180);
    private static final Color COLOR_TABLE_HEADER = new Color(55, 50, 65);
    private static final Color COLOR_ROW_ODD = new Color(255, 255, 255);
    private static final Color COLOR_ROW_EVEN = new Color(248, 245, 242);

    public SitePage() {
        setTitle("Gestion des Sites - Système de Gestion des Sites Touristiques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1100, 700);
        setSize(1400, 950);

        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);

        mainPanel.add(new Header(
                this,
                "GESTION DES SITES",
                "Système de gestion des sites touristiques",
                Header.ActivePage.SITES
        ), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTableContainer(), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        setupKeyboardShortcuts();
        loadSites();
        updateStatus("Système prêt", "success");
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Recherche par numéro de site");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchControls.setOpaque(false);

        JLabel lblValue = new JLabel("N° Site :");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(Color.BLACK);

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setForeground(Color.BLACK);
        txtSearch.setBackground(COLOR_PANEL);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton btnSearch = createElegantButton("Rechercher", COLOR_ACCENT);
        btnSearch.addActionListener(e -> searchSite());

        JButton btnReset = createElegantButton("Réinitialiser", new Color(200, 195, 190));
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadSites();
        });

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

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Liste des sites");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecordCount.setForeground(Color.DARK_GRAY);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);

        tableModel = new SiteTableModel();
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

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createActionButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createActionButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnAdd = createElegantButton("AJOUTER UN SITE", COLOR_BUTTON_ADD);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.addActionListener(e -> addSite());

        JButton btnEdit = createElegantButton("MODIFIER", COLOR_BUTTON_EDIT);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.addActionListener(e -> editSite());

        JButton btnDelete = createElegantButton("SUPPRIMER", COLOR_BUTTON_DELETE);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.addActionListener(e -> deleteSite());

        JButton btnRefresh = createElegantButton("ACTUALISER", COLOR_BUTTON_REFRESH);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.addActionListener(e -> loadSites());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblStatus = new JLabel("Système prêt");
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
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
                ));            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(9, 21, 9, 21)
                ));            }
        });

        return button;
    }

    private void setupKeyboardShortcuts() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");
        getRootPane().getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSites();
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

    private void loadSites() {
        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return SiteService.getAllSite();
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setSite(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    updateStatus("Données chargées (" + result.length() + " sites)", "success");
                } catch (Exception e) {
                    updateStatus("Erreur de connexion au serveur", "error");
                    JOptionPane.showMessageDialog(SitePage.this,
                            "Impossible de charger les données.\nVérifiez que le serveur backend est démarré sur http://localhost:5000",
                            "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }

    private void searchSite() {
        String value = txtSearch.getText().trim();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer un numéro de site !",
                    "Recherche", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return SiteService.searchSite(value);
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setSite(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    updateStatus(result.length() == 0 ? "Aucun résultat pour: " + value : "Site trouvé", "success");
                } catch (Exception e) {
                    updateStatus("Erreur de recherche", "error");
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }

    private void addSite() {
        SiteCRUDDialog dialog = new SiteCRUDDialog(this, "add", null, null, null, null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return SiteService.createSite(
                            dialog.getNumero(),
                            dialog.getNom(),
                            dialog.getLieu(),
                            dialog.getTarifJournalier()
                    );
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Site ajouté avec succès !", "success");
                            loadSites();
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

    private void editSite() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un site à modifier !",
                    "Modification", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JSONObject site = tableModel.getSiteAt(table.convertRowIndexToModel(selectedRow));
        SiteCRUDDialog dialog = new SiteCRUDDialog(this, "edit",
                site.getString("n_site"),
                site.getString("nom"),
                site.optString("lieu"),
                getTarif(site));
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return SiteService.updateSite(
                            dialog.getNumero(),
                            dialog.getNom(),
                            dialog.getLieu(),
                            dialog.getTarifJournalier()
                    );
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Site modifié avec succès !", "success");
                            loadSites();
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

    private void deleteSite() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un site à supprimer !",
                    "Suppression", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JSONObject site = tableModel.getSiteAt(table.convertRowIndexToModel(selectedRow));
        String numero = site.getString("n_site");
        String nom = site.getString("nom");

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le site :\n\nN°: " + numero + "\nNom: " + nom + "\n\nCette action est irréversible !",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return SiteService.deleteSite(numero);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Site supprimé avec succès !", "success");
                            loadSites();
                            txtSearch.setText("");
                        } else {
                            updateStatus("Impossible de supprimer le site", "error");
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

    private Float getTarif(JSONObject site) {
        Object value = site.opt("tarif_journalier");
        if (value instanceof Number number) {
            return number.floatValue();
        }
        if (value != null) {
            try {
                return Float.parseFloat(value.toString());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private void openVisitorPage() {
        Main main = new Main();
        main.setVisible(true);
        dispose();
    }

    private void openVisiterPage() {
        VisiterPage visiterPage = new VisiterPage();
        visiterPage.setVisible(true);
        dispose();
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

        SwingUtilities.invokeLater(() -> new SitePage().setVisible(true));
    }
}
