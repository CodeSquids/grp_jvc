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
            case 3: return v.opt("nbjours") + " j";
            case 4: return formatDateVisite(v.opt("date_visite"));
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
