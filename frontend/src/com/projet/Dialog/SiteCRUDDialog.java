package com.projet.Dialog;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SiteCRUDDialog extends JDialog {
    private JTextField txtNumero, txtNom, txtLieu, txtTarifJournalier;
    private boolean saved = false;

    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);

    public SiteCRUDDialog(JFrame parent, String mode, String num, String nom, String lieu, Float tarifJournalier) {
        super(parent, mode.equals("add") ? "Ajouter un site" : "Modifier un site", true);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        txtNumero = createTextField(num, fieldFont);
        txtNom = createTextField(nom, fieldFont);
        txtLieu = createTextField(lieu, fieldFont);
        txtTarifJournalier = createTextField(tarifJournalier != null ? tarifJournalier.toString() : null, fieldFont);

        addFormRow(formPanel, 0, "N Site *", txtNumero, labelFont);
        addFormRow(formPanel, 1, "Nom *", txtNom, labelFont);
        addFormRow(formPanel, 2, "Lieu", txtLieu, labelFont);
        addFormRow(formPanel, 3, "Tarif journalier *", txtTarifJournalier, labelFont);

        if (mode.equals("edit")) {
            txtNumero.setEnabled(false);
            txtNumero.setBackground(new Color(240, 240, 240));
        }

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

        setSize(500, 380);
        setLocationRelativeTo(parent);
        setResizable(false);

        SwingUtilities.invokeLater(() -> {
            JTextField firstField = mode.equals("edit") ? txtNom : txtNumero;
            firstField.requestFocusInWindow();
            firstField.selectAll();
        });
    }

    private JTextField createTextField(String value, Font font) {
        JTextField textField = new JTextField(value != null ? value : "");
        textField.setFont(font);
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(Color.BLACK);
        textField.setColumns(20);
        textField.setBorder(createTextFieldBorder());
        return textField;
    }

    private void addFormRow(JPanel formPanel, int row, String labelText, JTextField textField, Font labelFont) {
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
        String numero = txtNumero.getText().trim();
        String nom = txtNom.getText().trim();
        String tarif = txtTarifJournalier.getText().trim();

        if (numero.isEmpty() || nom.isEmpty() || tarif.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires (*) !",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Float.parseFloat(tarif);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Le tarif journalier doit etre un nombre valide.",
                    "Tarif invalide",
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

    public String getLieu() {
        return txtLieu.getText().trim();
    }

    public Float getTarifJournalier() {
        return Float.parseFloat(txtTarifJournalier.getText().trim());
    }
}
