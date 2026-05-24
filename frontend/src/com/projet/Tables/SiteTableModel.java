package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SiteTableModel extends AbstractTableModel {
    private List<JSONObject> site = new ArrayList<>();
    private String[] columns = {"N° Site", "Nom", "Lieu", "Tarif journalier"};

    public void setSite(JSONArray array) {
        site.clear();
        for (int i = 0; i < array.length(); i++) {
            site.add(array.getJSONObject(i));
        }
        fireTableDataChanged();
    }

    public JSONObject getSiteAt(int row) {
        return site.get(row);
    }

    @Override
    public int getRowCount() {
        return site.size();
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
        JSONObject v = site.get(rowIndex);
        switch (columnIndex) {
            case 0: return v.getString("n_site");
            case 1: return v.getString("nom");
            case 2: return v.getString("lieu");
            case 3: return v.getString("tarif_journalier");
            default: return null;
        }
    }
}