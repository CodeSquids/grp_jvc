package com.projet.Services;

import java.io.*;
import java.net.*;
import java.sql.Date;
import org.json.*;

public class VisiterService {
    private static final String BASE_URL = "http://localhost:5000/api";

    // Récupérer tous les site
    public static JSONArray getAllVisiter() throws Exception {
        URL url = new URL(BASE_URL + "/visiter");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONArray(response.toString());
    }

    // Rechercher visiter
    public static JSONArray searchVisiter(String n_visiter) throws Exception {
        URL url = new URL(BASE_URL + "/visiter/" + n_visiter);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONArray(response.toString());
    }

    // Créer visiter
    public static boolean createVisiter(String n_visiter, String n_visiteur, String n_site, int nbjours, Date date_visite) throws Exception {
        URL url = new URL(BASE_URL + "/visiter");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("n_visiter", n_visiter);
        json.put("n_visiteur", n_visiteur);
        json.put("n_site", n_site);
        json.put("nbjours", nbjours);
        json.put("date_visite", date_visite);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();

        int responseCode = conn.getResponseCode();
        return responseCode == 201;
    }

    // Modifier visiter
    public static boolean updateVisiter(String n_visiter, String n_visiteur, String n_site, int nbjours, Date date_visite) throws Exception {
        URL url = new URL(BASE_URL + "/visiter/" + n_visiter);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("n_visiteur", n_visiteur);
        json.put("n_site", n_site);
        json.put("nbjours", nbjours);
        json.put("date_visite", date_visite);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    // Supprimer visiter
    public static boolean deleteVisiter(String n_visiter) throws Exception {
        URL url = new URL(BASE_URL + "/visiter/" + n_visiter);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    public static JSONArray complex1(String site_nom, Date date_start, Date date_end) throws Exception {
        URL url = new URL(BASE_URL + "/visiter/complex1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("site_nom", site_nom);
        json.put("date_start", date_start.toString());
        json.put("date_end", date_end.toString());

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONArray(response.toString());
    }

    public static JSONArray complex2(Date date_start, Date date_end) throws Exception {
        URL url = new URL(BASE_URL + "/visiter/complex2");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("date_start", date_start.toString());
        json.put("date_end", date_end.toString());

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONArray(response.toString());
    }
}
