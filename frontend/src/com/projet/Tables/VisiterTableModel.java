package com.projet.Tables;

import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class VisiterTableModel extends AbstractTableModel {
    private final List<JSONObject> visiter = new ArrayList<>();
    private final String[] columns = {"N Visiter", "N Visiteur", "N Site", "Nb jours", "Date visite"};

    public void setVisiter(JSONArray array) {
        visiter.clear();
        for (int i = 0; i < array.length(); i++) {
            visiter.add(array.getJSONObject(i));
        }
        fireTableDataChanged();
    }

    public JSONObject getVisiterAt(int row) {
        return visiter.get(row);
    }

    @Override
    public int getRowCount() {
        return visiter.size();
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
        JSONObject v = visiter.get(rowIndex);
        switch (columnIndex) {
            case 0: return v.opt("n_visiter");
            case 1: return v.opt("n_visiteur");
            case 2: return v.opt("n_site");
            case 3: return v.opt("nbjours");
            case 4: return v.opt("date_visite");
            default: return null;
        }
    }
}
