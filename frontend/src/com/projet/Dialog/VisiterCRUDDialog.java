package com.projet.Dialog;

import com.projet.VisiteurService;
import com.projet.Services.SiteService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class VisiterCRUDDialog extends JDialog {
    private JTextField txtNumero, txtVisiteur, txtSite, txtNbJours;
    private JComboBox<VisitorOption> comboNomVisiteur;
    private JComboBox<SiteOption> comboNomSite;
    private JFormattedTextField dateField;
    private JButton btnSave, btnCancel;
    private boolean saved = false;
    private String mode;

    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);

    public VisiterCRUDDialog(JFrame parent, String mode, String numero, String visiteur, String site, Integer nbJours, String dateVisite) {
        super(parent, mode.equals("add") ? "Ajouter une visite" : "Modifier une visite", true);
        this.mode = mode;

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblNumero = new JLabel("N° Visite *");
        lblNumero.setFont(labelFont);
        lblNumero.setForeground(COLOR_PRIMARY);
        formPanel.add(lblNumero, gbc);

        gbc.gridx = 1;
        txtNumero = new JTextField(numero != null ? numero : "", 20);
        txtNumero.setFont(fieldFont);
        txtNumero.setBorder(createTextFieldBorder());
        txtNumero.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtNumero, gbc);

        row++;

        if (mode.equals("add")) {
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel lblNomVisiteur = new JLabel("Nom Visiteur *");
            lblNomVisiteur.setFont(labelFont);
            lblNomVisiteur.setForeground(COLOR_PRIMARY);
            formPanel.add(lblNomVisiteur, gbc);

            gbc.gridx = 1;
            comboNomVisiteur = new JComboBox<>();
            comboNomVisiteur.setFont(fieldFont);
            comboNomVisiteur.setBorder(createTextFieldBorder());
            comboNomVisiteur.setPreferredSize(new Dimension(250, 35));
            comboNomVisiteur.addActionListener(e -> syncVisiteurFieldFromSelection());
            formPanel.add(comboNomVisiteur, gbc);

            row++;
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblVisiteur = new JLabel("N° Visiteur *");
        lblVisiteur.setFont(labelFont);
        lblVisiteur.setForeground(COLOR_PRIMARY);
        formPanel.add(lblVisiteur, gbc);

        gbc.gridx = 1;
        txtVisiteur = new JTextField(visiteur != null ? visiteur : "", 20);
        txtVisiteur.setFont(fieldFont);
        txtVisiteur.setBorder(createTextFieldBorder());
        txtVisiteur.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtVisiteur, gbc);

        row++;

        if (mode.equals("add")) {
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel lblNomSite = new JLabel("Nom Site *");
            lblNomSite.setFont(labelFont);
            lblNomSite.setForeground(COLOR_PRIMARY);
            formPanel.add(lblNomSite, gbc);

            gbc.gridx = 1;
            comboNomSite = new JComboBox<>();
            comboNomSite.setFont(fieldFont);
            comboNomSite.setBorder(createTextFieldBorder());
            comboNomSite.setPreferredSize(new Dimension(250, 35));
            comboNomSite.addActionListener(e -> syncSiteFieldFromSelection());
            formPanel.add(comboNomSite, gbc);

            row++;
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblSite = new JLabel("N° Site *");
        lblSite.setFont(labelFont);
        lblSite.setForeground(COLOR_PRIMARY);
        formPanel.add(lblSite, gbc);

        gbc.gridx = 1;
        txtSite = new JTextField(site != null ? site : "", 20);
        txtSite.setFont(fieldFont);
        txtSite.setBorder(createTextFieldBorder());
        txtSite.setPreferredSize(new Dimension(250, 35));
        if (mode.equals("add")) {
            txtSite.setEditable(false);
            txtSite.setBackground(new Color(245, 245, 245));
        }
        formPanel.add(txtSite, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblNbJours = new JLabel("Nb Jours *");
        lblNbJours.setFont(labelFont);
        lblNbJours.setForeground(COLOR_PRIMARY);
        formPanel.add(lblNbJours, gbc);

        gbc.gridx = 1;
        txtNbJours = new JTextField(nbJours != null ? nbJours.toString() : "", 20);
        txtNbJours.setFont(fieldFont);
        txtNbJours.setBorder(createTextFieldBorder());
        txtNbJours.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtNbJours, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblFormattedDate = new JLabel("Date Visite *");
        lblFormattedDate.setFont(labelFont);
        lblFormattedDate.setForeground(COLOR_PRIMARY);
        formPanel.add(lblFormattedDate, gbc);

        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        dateField = new JFormattedTextField(dateFormat);
        dateField.setFont(fieldFont);
        dateField.setBorder(createTextFieldBorder());
        dateField.setPreferredSize(new Dimension(250, 35));
        dateField.setColumns(10);
        dateField.setValue(parseDateVisiteOrToday(dateVisite));
        formPanel.add(dateField, gbc);

        if (mode.equals("add")) {
            loadVisitorOptions();
            loadSiteOptions();
        }

        if (mode.equals("edit")) {
            txtNumero.setEnabled(false);
            txtNumero.setBackground(new Color(240, 240, 240));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnSave = createStyledButton(mode.equals("add") ? "Enregistrer" : "Mettre a jour", COLOR_SUCCESS);
        btnCancel = createStyledButton("Annuler", COLOR_DANGER);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(500, mode.equals("add") ? 650 : 490);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private Border createTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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

    private void save() {
        String numero = txtNumero.getText().trim();
        String visiteur = txtVisiteur.getText().trim();
        String site = txtSite.getText().trim();
        String nbJours = txtNbJours.getText().trim();
        String dateVisite = dateField.getText().trim();

        if (numero.isEmpty() || visiteur.isEmpty() || site.isEmpty() || nbJours.isEmpty() || dateVisite.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires (*) !",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Integer.parseInt(nbJours);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Le nombre de jours doit etre un nombre entier valide !",
                    "Valeur invalide",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            parseDateVisite(dateVisite);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "La date doit etre au format yyyy-mm-dd !",
                    "Date invalide",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        saved = true;
        dispose();
    }

    private void loadVisitorOptions() {
        if (comboNomVisiteur == null) {
            return;
        }

        comboNomVisiteur.addItem(new VisitorOption("", "-- Selectionner un visiteur --"));
        comboNomVisiteur.setSelectedIndex(0);

        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return VisiteurService.getAllVisiteurs();
            }

            @Override
            protected void done() {
                try {
                    JSONArray visitors = get();
                    comboNomVisiteur.removeAllItems();
                    comboNomVisiteur.addItem(new VisitorOption("", "-- Selectionner un visiteur --"));

                    for (int i = 0; i < visitors.length(); i++) {
                        JSONObject visitor = visitors.getJSONObject(i);
                        String numero = visitor.optString("n_visiteur", "").trim();
                        String nom = visitor.optString("nom", "").trim();
                        if (nom.isEmpty()) {
                            nom = numero.isEmpty() ? "Visiteur " + (i + 1) : numero;
                        }
                        comboNomVisiteur.addItem(new VisitorOption(numero, nom));
                    }

                    comboNomVisiteur.setSelectedIndex(0);
                    txtVisiteur.setText("");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VisiterCRUDDialog.this,
                            "Impossible de charger la liste des visiteurs.",
                            "Erreur de chargement",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void loadSiteOptions() {
        if (comboNomSite == null) {
            return;
        }

        comboNomSite.addItem(new SiteOption("", "-- Selectionner un site --"));
        comboNomSite.setSelectedIndex(0);

        SwingWorker<JSONArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                return SiteService.getAllSite();
            }

            @Override
            protected void done() {
                try {
                    JSONArray sites = get();
                    comboNomSite.removeAllItems();
                    comboNomSite.addItem(new SiteOption("", "-- Selectionner un site --"));

                    for (int i = 0; i < sites.length(); i++) {
                        JSONObject site = sites.getJSONObject(i);
                        String numero = site.optString("n_site", "").trim();
                        String nom = site.optString("nom", "").trim();
                        if (nom.isEmpty()) {
                            nom = numero.isEmpty() ? "Site " + (i + 1) : numero;
                        }
                        comboNomSite.addItem(new SiteOption(numero, nom));
                    }

                    comboNomSite.setSelectedIndex(0);
                    txtSite.setText("");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VisiterCRUDDialog.this,
                            "Impossible de charger la liste des sites.",
                            "Erreur de chargement",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void syncVisiteurFieldFromSelection() {
        if (comboNomVisiteur == null) {
            return;
        }

        VisitorOption selected = (VisitorOption) comboNomVisiteur.getSelectedItem();
        if (selected == null || selected.numero.isBlank()) {
            txtVisiteur.setText("");
            return;
        }

        txtVisiteur.setText(selected.numero);
    }

    private void syncSiteFieldFromSelection() {
        if (comboNomSite == null) {
            return;
        }

        SiteOption selected = (SiteOption) comboNomSite.getSelectedItem();
        if (selected == null || selected.numero.isBlank()) {
            txtSite.setText("");
            return;
        }

        txtSite.setText(selected.numero);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getNumero() {
        return txtNumero.getText().trim();
    }

    public String getVisiteur() {
        return txtVisiteur.getText().trim();
    }

    public String getSite() {
        return txtSite.getText().trim();
    }

    public int getNbJours() {
        return Integer.parseInt(txtNbJours.getText().trim());
    }

    public Date getDateVisite() {
        return parseDateVisite(dateField.getText());
    }

    private Date parseDateVisiteOrToday(String dateVisite) {
        try {
            return parseDateVisite(dateVisite);
        } catch (IllegalArgumentException e) {
            return new Date(System.currentTimeMillis());
        }
    }

    private Date parseDateVisite(String dateVisite) {
        String normalizedDate = normalizeDateVisite(dateVisite);
        return Date.valueOf(normalizedDate);
    }

    private String normalizeDateVisite(String dateVisite) {
        if (dateVisite == null) {
            throw new IllegalArgumentException("Date visite manquante");
        }

        String trimmedDate = dateVisite.trim();
        if (trimmedDate.isEmpty() || trimmedDate.equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Date visite manquante");
        }

        if (trimmedDate.length() >= 10
                && Character.isDigit(trimmedDate.charAt(0))
                && Character.isDigit(trimmedDate.charAt(1))
                && Character.isDigit(trimmedDate.charAt(2))
                && Character.isDigit(trimmedDate.charAt(3))
                && trimmedDate.charAt(4) == '-'
                && Character.isDigit(trimmedDate.charAt(5))
                && Character.isDigit(trimmedDate.charAt(6))
                && trimmedDate.charAt(7) == '-'
                && Character.isDigit(trimmedDate.charAt(8))
                && Character.isDigit(trimmedDate.charAt(9))) {
            return trimmedDate.substring(0, 10);
        }

        return trimmedDate;
    }

    private static class VisitorOption {
        private final String numero;
        private final String nom;

        private VisitorOption(String numero, String nom) {
            this.numero = numero == null ? "" : numero;
            this.nom = nom == null ? "" : nom;
        }

        @Override
        public String toString() {
            return nom;
        }
    }

    private static class SiteOption {
        private final String numero;
        private final String nom;

        private SiteOption(String numero, String nom) {
            this.numero = numero == null ? "" : numero;
            this.nom = nom == null ? "" : nom;
        }

        @Override
        public String toString() {
            return nom;
        }
    }
}
