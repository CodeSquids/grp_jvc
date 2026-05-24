package com.projet.Dialog;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.Date;

public class VisiterCRUDDialog extends JDialog {
    private final JTextField txtNumero;
    private final JTextField txtVisiteur;
    private final JTextField txtSite;
    private final JTextField txtNbJours;
    private final JTextField txtDateVisite;
    private boolean saved = false;

    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);

    public VisiterCRUDDialog(JFrame parent, String mode, String numero, String visiteur, String site, Integer nbJours, String dateVisite) {
//        super(parent, getTitle(mode), true);
        super(parent, mode.equals("add") ? "Ajouter un site" : "Modifier un site", true);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        txtNumero = createTextField(numero, fieldFont);
        txtVisiteur = createTextField(visiteur, fieldFont);
        txtSite = createTextField(site, fieldFont);
        txtNbJours = createTextField(nbJours != null ? nbJours.toString() : null, fieldFont);
        txtDateVisite = createTextField(dateVisite, fieldFont);

        addFormRow(formPanel,  0, "N Visiter *", txtNumero, labelFont);
        addFormRow(formPanel,  1, "N Visiteur *", txtVisiteur, labelFont);
        addFormRow(formPanel,  2, "N Site *", txtSite, labelFont);
        addFormRow(formPanel,  3, "Nb jours *", txtNbJours, labelFont);
        addFormRow(formPanel,  4, "Date visite *", txtDateVisite, labelFont);

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;



        if (mode.equals("edit")) {
            txtNumero.setEnabled(false);
            txtNumero.setBackground(new Color(240, 240, 240));
        }

//        if (mode.equals("delete")) {
//            disableField(txtVisiteur);
//            disableField(txtSite);
//            disableField(txtNbJours);
//            disableField(txtDateVisite);
//        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnSave = createStyledButton(mode.equals("add") ? "Enregistrer" : "Mettre a jour", COLOR_SUCCESS);
        JButton btnCancel = createStyledButton("Annuler", COLOR_DANGER);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setSize(500, 430);
        setLocationRelativeTo(parent);
        setResizable(false);

        SwingUtilities.invokeLater(() -> {
            JTextField firstField = mode.equals("edit") ? txtVisiteur : txtNumero;
            firstField.requestFocusInWindow();
            firstField.selectAll();
        });
    }

//    private static String getTitle(String mode) {
//        if (mode.equals("add")) {
//            return "Ajouter une visite";
//        }
//        if (mode.equals("delete")) {
//            return "Supprimer une visite";
//        }
//        return "Modifier une visite";
//    }

//    private static String getActionText(String mode) {
//        if (mode.equals("add")) {
//            return "Enregistrer";
//        }
//        if (mode.equals("delete")) {
//            return "Supprimer";
//        }
//        return "Mettre a jour";
//    }

    private JTextField createTextField(String value, Font font) {
        JTextField textField = new JTextField(value != null ? value : "", 20);
        textField.setFont(font);
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(Color.BLACK);
        textField.setColumns(20);
        textField.setBorder(createTextFieldBorder());
        return textField;
    }

//    private void disableField(JTextField textField) {
//        textField.setEnabled(false);
//        textField.setBackground(new Color(240, 240, 240));
//    }

    private void addFormRow(JPanel formPanel,  int row, String labelText, JTextField textField, Font labelFont) {
//        gbc.gridx = 0;
//        gbc.gridy = row;
//        JLabel label = new JLabel(labelText);
//        label.setFont(labelFont);
//        label.setForeground(COLOR_PRIMARY);
//        formPanel.add(label, gbc);
//
//        gbc.gridx = 1;
//        formPanel.add(textField, gbc);
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = row;
        labelGbc.insets = new Insets(10, 10, 10, 10);
        labelGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(COLOR_PRIMARY);
        formPanel.add(label, labelGbc);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = row;
        fieldGbc.insets = new Insets(10, 10, 10, 10);
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(textField, fieldGbc);
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
        if (getNumero().isEmpty() || getVisiteur().isEmpty() || getSite().isEmpty()
                || txtNbJours.getText().trim().isEmpty() || txtDateVisite.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires (*) !",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            getNbJours();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Nb jours doit etre un nombre entier valide.",
                    "Nombre invalide",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            getDateVisite();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Date visite doit respecter le format yyyy-mm-dd.",
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
        return Date.valueOf(txtDateVisite.getText().trim());
    }
}
