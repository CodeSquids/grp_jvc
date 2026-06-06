package com.projet.Dialog;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VisiteurCRUDDialog extends JDialog {
    private JTextField txtNumero, txtNom, txtAdresse;
    private JButton btnSave, btnCancel;
    private boolean saved = false;
    private String mode;
    
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    
    public VisiteurCRUDDialog(JFrame parent, String mode, String num, String nom, String adresse) {
        super(parent, mode.equals("add") ? "➕ Ajouter un visiteur" : "✏️ Modifier un visiteur", true);
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
        
        // Numéro
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNumero = new JLabel("N° Visiteur *");
        lblNumero.setFont(labelFont);
        lblNumero.setForeground(COLOR_PRIMARY);
        formPanel.add(lblNumero, gbc);
        
        gbc.gridx = 1;
        txtNumero = new JTextField(num != null ? num : "", 20);
        txtNumero.setFont(fieldFont);
        txtNumero.setBorder(createTextFieldBorder());
        formPanel.add(txtNumero, gbc);
        
        // Nom
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNom = new JLabel("Nom *");
        lblNom.setFont(labelFont);
        lblNom.setForeground(COLOR_PRIMARY);
        formPanel.add(lblNom, gbc);
        
        gbc.gridx = 1;
        txtNom = new JTextField(nom != null ? nom : "", 20);
        txtNom.setFont(fieldFont);
        txtNom.setBorder(createTextFieldBorder());
        formPanel.add(txtNom, gbc);
        
        // Adresse
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblAdresse = new JLabel("Adresse");
        lblAdresse.setFont(labelFont);
        lblAdresse.setForeground(COLOR_PRIMARY);
        formPanel.add(lblAdresse, gbc);
        
        gbc.gridx = 1;
        txtAdresse = new JTextField(adresse != null ? adresse : "", 20);
        txtAdresse.setFont(fieldFont);
        txtAdresse.setBorder(createTextFieldBorder());
        formPanel.add(txtAdresse, gbc);
        
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
        
        setSize(500, 350);
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
        String nom = txtNom.getText().trim();
        String adresse = txtAdresse.getText().trim();
        
        if (numero.isEmpty() || nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs obligatoires (*) !", 
                "Champs manquants", 
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
    
    public String getNom() {
        return txtNom.getText().trim();
    }
    
    public String getAdresse() {
        return txtAdresse.getText().trim();
    }
}