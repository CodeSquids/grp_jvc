package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Complex1Model extends AbstractTableModel {
    private List<JSONObject> complex1 = new ArrayList<>();
    private String[] columns = { "N* Visiteur", "Nom", "Adresse", "Nom de site", "Date visite", "Nb jours", "Montant"};

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
            case 0: return v.getString("n_visiteur");
            case 1: return v.getString("nom");
            case 2: return v.getString("adresse");
            case 3: return v.getString("nom_site");
            case 4: return v.getString("date_visite");
            case 5: return v.getString("nbjours");
            case 6: return v.getString("montant");
            default: return null;
        }
    }
}