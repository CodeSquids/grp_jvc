package com.projet.Pages;

import com.projet.Dialog.VisiterCRUDDialog;
import com.projet.Main;
import com.projet.Services.VisiterService;
import com.projet.Tables.VisiterTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class VisiterPage extends JFrame {
    private JTable table;
    private VisiterTableModel tableModel;
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
    private static final Color COLOR_TABLE_HEADER = new Color(55, 50, 65);
    private static final Color COLOR_ROW_ODD = new Color(255, 255, 255);
    private static final Color COLOR_ROW_EVEN = new Color(248, 245, 242);

    public VisiterPage() {
        setTitle("Gestion des Visites - Systeme de Gestion des Sites Touristiques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BACKGROUND);

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTableContainer(), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);
        add(mainPanel);

        setupKeyboardShortcuts();
        loadVisiter();
        updateStatus("Systeme pret", "success");
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_HEADER_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("GESTION DES VISITES");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Systeme de gestion des visites touristiques");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 195, 190));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        JLabel logoLabel = new JLabel("*");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        logoLabel.setForeground(new Color(200, 180, 150));

        header.add(logoLabel, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Recherche par numero de visite");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchControls.setOpaque(false);

        JLabel lblValue = new JLabel("N Visiter :");
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
        btnSearch.addActionListener(e -> searchVisiter());

        JButton btnReset = createElegantButton("Reinitialiser", new Color(200, 195, 190));
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadVisiter();
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

        JLabel titleLabel = new JLabel("Liste des visites");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(COLOR_ACCENT);

        lblRecordCount = new JLabel("0 enregistrement(s)");
        lblRecordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecordCount.setForeground(Color.DARK_GRAY);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(lblRecordCount, BorderLayout.EAST);

        tableModel = new VisiterTableModel();
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
        header.setForeground(Color.WHITE);
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

        JButton btnAdd = createElegantButton("AJOUTER UNE VISITE", COLOR_BUTTON_ADD);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.addActionListener(e -> addVisiter());

        JButton btnEdit = createElegantButton("MODIFIER", COLOR_BUTTON_EDIT);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.addActionListener(e -> editVisiter());

        JButton btnDelete = createElegantButton("SUPPRIMER", COLOR_BUTTON_DELETE);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.addActionListener(e -> deleteVisiter());

        JButton btnRefresh = createElegantButton("ACTUALISER", COLOR_BUTTON_REFRESH);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.addActionListener(e -> loadVisiter());

        JButton btnVisitors = createElegantButton("CRUD VISITEURS", COLOR_BUTTON_REFRESH);
        btnVisitors.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVisitors.addActionListener(e -> openVisitorPage());

        JButton btnSites = createElegantButton("CRUD SITES", COLOR_BUTTON_REFRESH);
        btnSites.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSites.addActionListener(e -> openSitePage());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        panel.add(btnVisitors);
        panel.add(btnSites);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblStatus = new JLabel("Systeme pret");
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
                loadVisiter();
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
            case "success" -> "";
            case "error" -> "Erreur: ";
            case "warning" -> "Attention: ";
            default -> "";
        };
        lblStatus.setText(icon + message);

        if (!type.equals("error")) {
            Timer timer = new Timer(3000, e -> lblStatus.setText("Systeme pret"));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void loadVisiter() {
        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiterService.getAllVisiter();
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setVisiter(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    updateStatus("Donnees chargees (" + result.length() + " visites)", "success");
                } catch (Exception e) {
                    updateStatus("Erreur de connexion au serveur", "error");
                    JOptionPane.showMessageDialog(VisiterPage.this,
                            "Impossible de charger les donnees.\nVerifiez que le serveur backend est demarre sur http://localhost:5000",
                            "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }

    private void searchVisiter() {
        String value = txtSearch.getText().trim();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer un numero de visite !",
                    "Recherche", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showProgress(true);
        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiterService.searchVisiter(value);
            }

            @Override
            protected void done() {
                try {
                    JSONArray result = get();
                    tableModel.setVisiter(result);
                    lblRecordCount.setText(result.length() + " enregistrement(s)");
                    updateStatus(result.length() == 0 ? "Aucun resultat pour: " + value : "Visite trouvee", "success");
                } catch (Exception e) {
                    updateStatus("Erreur de recherche", "error");
                } finally {
                    showProgress(false);
                }
            }
        };
        worker.execute();
    }

    private void addVisiter() {
        VisiterCRUDDialog dialog = new VisiterCRUDDialog(this, "add", null, null, null, null, null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiterService.createVisiter(
                            dialog.getNumero(),
                            dialog.getVisiteur(),
                            dialog.getSite(),
                            dialog.getNbJours(),
                            dialog.getDateVisite()
                    );
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visite ajoutee avec succes !", "success");
                            loadVisiter();
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

    private void editVisiter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez selectionner une visite a modifier !",
                    "Modification", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JSONObject visiter = tableModel.getVisiterAt(table.convertRowIndexToModel(selectedRow));
        VisiterCRUDDialog dialog = new VisiterCRUDDialog(this, "edit",
                visiter.optString("n_visiter"),
                visiter.optString("n_visiteur"),
                visiter.optString("n_site"),
                getInteger(visiter, "nbjours"),
                visiter.optString("date_visite"));
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiterService.updateVisiter(
                            dialog.getNumero(),
                            dialog.getVisiteur(),
                            dialog.getSite(),
                            dialog.getNbJours(),
                            dialog.getDateVisite()
                    );
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visite modifiee avec succes !", "success");
                            loadVisiter();
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

    private void deleteVisiter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez selectionner une visite a supprimer !",
                    "Suppression", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JSONObject visiter = tableModel.getVisiterAt(table.convertRowIndexToModel(selectedRow));
        String numero = visiter.optString("n_visiter");

        VisiterCRUDDialog dialog = new VisiterCRUDDialog(this, "delete",
                numero,
                visiter.optString("n_visiteur"),
                visiter.optString("n_site"),
                getInteger(visiter, "nbjours"),
                visiter.optString("date_visite"));
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            showProgress(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return VisiterService.deleteVisiter(numero);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            updateStatus("Visite supprimee avec succes !", "success");
                            loadVisiter();
                            txtSearch.setText("");
                        } else {
                            updateStatus("Impossible de supprimer la visite", "error");
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

    private Integer getInteger(JSONObject object, String key) {
        Object value = object.opt(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
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

    private void openSitePage() {
        SitePage sitePage = new SitePage();
        sitePage.setVisible(true);
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

        SwingUtilities.invokeLater(() -> new VisiterPage().setVisible(true));
    }
}
