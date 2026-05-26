package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Complex2Model extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<JSONObject> complex2 = new ArrayList<>();
    private String[] columns = { "N° Site", "Nom Site", "Effectif", "Montant"};

    public void setComplex2(JSONArray array) {
        complex2.clear();
        for (int i = 0; i < array.length(); i++) {
            complex2.add(array.getJSONObject(i));
        }
        fireTableDataChanged();
    }

    public JSONObject getComplex2At(int row) {
        return complex2.get(row);
    }

    @Override
    public int getRowCount() {
        return complex2.size();
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
        JSONObject v = complex2.get(rowIndex);
        switch (columnIndex) {
            case 0: return v.get("n_site").toString();
            case 1: return v.get("nom_site").toString();
            case 2: return v.get("effectif").toString();
            case 3: return v.get("montant").toString();
            default: return null;
        }
    }
}