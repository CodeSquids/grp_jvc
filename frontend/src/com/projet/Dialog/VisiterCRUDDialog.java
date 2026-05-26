package com.projet.Dialog;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class VisiterCRUDDialog extends JDialog {
    private JTextField txtNumero, txtVisiteur, txtSite, txtNbJours, txtDateVisite;
    private JFormattedTextField dateField;
    private JButton btnSave, btnCancel;
    private boolean saved = false;
    private String mode;
    
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);

    public VisiterCRUDDialog(JFrame parent, String mode, String numero, String visiteur, String site, Integer nbJours, String dateVisite) {
        super(parent, mode.equals("add") ? "➕ Ajouter une visite" : "✏️ Modifier une visite", true);
        this.mode = mode;
        
        setLayout(new BorderLayout());
        
        // Panel principal avec fond
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Style des labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        // N° Visite
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNumero = new JLabel("N° Visite *");
        lblNumero.setFont(labelFont);
        lblNumero.setForeground(COLOR_PRIMARY);
        formPanel.add(lblNumero, gbc);
        
        gbc.gridx = 1;
        txtNumero = new JTextField(numero != null ? numero : "", 20);
        txtNumero.setFont(fieldFont);
        txtNumero.setBorder(createTextFieldBorder());
        txtNumero.setPreferredSize(new Dimension(250, 35)); // Ajout de taille préférée
        formPanel.add(txtNumero, gbc);
        
        // N° Visiteur
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        
        // N° Site
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblSite = new JLabel("N° Site *");
        lblSite.setFont(labelFont);
        lblSite.setForeground(COLOR_PRIMARY);
        formPanel.add(lblSite, gbc);
        
        gbc.gridx = 1;
        txtSite = new JTextField(site != null ? site : "", 20);
        txtSite.setFont(fieldFont);
        txtSite.setBorder(createTextFieldBorder());
        txtSite.setPreferredSize(new Dimension(250, 35));
        formPanel.add(txtSite, gbc);
        
        // Nb Jours
        gbc.gridx = 0;
        gbc.gridy = 3;
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
        
        // Date Visite
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        JLabel lblDateVisite = new JLabel("Date Visite *");
//        lblDateVisite.setFont(labelFont);
//        lblDateVisite.setForeground(COLOR_PRIMARY);
//        formPanel.add(lblDateVisite, gbc);
//
//        gbc.gridx = 1;
//        txtDateVisite = new JTextField(dateVisite != null ? dateVisite : "", 20);
//        txtDateVisite.setFont(fieldFont);
//        txtDateVisite.setBorder(createTextFieldBorder());
//        txtDateVisite.setPreferredSize(new Dimension(250, 35));
//        formPanel.add(txtDateVisite, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
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
        dateField.setValue(dateVisite != null ? Date.valueOf(dateVisite) : new java.util.Date());
        formPanel.add(dateField, gbc);
        
        // Ajout d'un espacement pour que les champs prennent toute la largeur disponible
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        if (mode.equals("edit")) {
            txtNumero.setEnabled(false);
            txtNumero.setBackground(new Color(240, 240, 240));
        }
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnSave = createStyledButton(mode.equals("add") ? "Enregistrer" : "Mettre à jour", COLOR_SUCCESS);
        btnCancel = createStyledButton("Annuler", COLOR_DANGER);
        
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setSize(500, 490);
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
        
        // Validation du nombre de jours
        try {
            Integer.parseInt(nbJours);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le nombre de jours doit être un nombre entier valide !",
                "Valeur invalide",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validation de la date
        try {
            Date.valueOf(dateVisite);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "La date doit être au format yyyy-mm-dd !",
                "Date invalide",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        saved = true;
        dispose();
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
        return Date.valueOf(dateField.getText().trim());
    }
}
