package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Complex1Model extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<JSONObject> complex1 = new ArrayList<>();
    private String[] columns = { "N° Visiteur", "Nom", "Adresse", "Nom de site", "Date visite", "Nb jours", "Montant"};

    public void setComplex1(JSONArray array) {
        complex1.clear();
        for (int i = 0; i < array.length(); i++) {
            complex1.add(array.getJSONObject(i));
        }
        fireTableDataChanged();
    }

    public JSONObject getComplex1At(int row) {
        return complex1.get(row);
    }

    @Override
    public int getRowCount() {
        return complex1.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JSONObject v = complex1.get(rowIndex);
        switch (columnIndex) {
            case 0: return v.opt("n_visiteur");
            case 1: return v.opt("nom");
            case 2: return v.opt("adresse");
            case 3: return v.opt("nom_site");
            case 4: return formatDateVisite(v.opt("date_visite"));
                // La date arrive au format YYYY-MM-DD, on la garde telle quelle
                //String date = 
                // Optionnel: convertir en format français si besoin
                // if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                //     date = date.substring(8,10) + "/" + date.substring(5,7) + "/" + date.substring(0,4);
                // }
                //return date;
            case 5: return v.opt("nbjours");
            case 6: return v.opt("montant");
            default: return null;
        }
    }
    private String formatDateVisite(Object dateVisite) {
        if (dateVisite == null || JSONObject.NULL.equals(dateVisite)) {
            return "";
        }

        String value = dateVisite.toString().trim();
        if (value.length() >= 10
                && Character.isDigit(value.charAt(0))
                && Character.isDigit(value.charAt(1))
                && Character.isDigit(value.charAt(2))
                && Character.isDigit(value.charAt(3))
                && value.charAt(4) == '-'
                && Character.isDigit(value.charAt(5))
                && Character.isDigit(value.charAt(6))
                && value.charAt(7) == '-'
                && Character.isDigit(value.charAt(8))
                && Character.isDigit(value.charAt(9))) {
            return value.substring(0, 10);
        }

        return value.replaceFirst("\\s+\\d{2}:\\d{2}:\\d{2}\\s+.*$", "");
    }
}