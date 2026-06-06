package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class VisiteurTableModel extends AbstractTableModel {
    private List<JSONObject> visiteurs = new ArrayList<>();
    private String[] columns = {"N° Visiteur", "Nom", "Adresse"};
    
    public void setVisiteurs(JSONArray array) {
        visiteurs.clear();
        for (int i = 0; i < array.length(); i++) {
            visiteurs.add(array.getJSONObject(i));
        }
        fireTableDataChanged();
    }
    
    public JSONObject getVisiteurAt(int row) {
        return visiteurs.get(row);
    }
    
    @Override
    public int getRowCount() {
        return visiteurs.size();
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
        JSONObject v = visiteurs.get(rowIndex);
        switch (columnIndex) {
            case 0: return v.getString("n_visiteur");
            case 1: return v.getString("nom");
            case 2: return v.getString("adresse");
            default: return null;
        }
    }
}