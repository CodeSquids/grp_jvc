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
            case 0: return v.get("n_visiteur").toString();
            case 1: return v.get("nom").toString();
            case 2: return v.get("adresse").toString();
            case 3: return v.get("nom_site").toString();
            case 4: return v.get("date_visite").toString();
            case 5: return v.get("nbjours").toString();
            case 6: return v.get("montant").toString();
            default: return null;
        }
    }
}