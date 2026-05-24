package com.projet.Services;

import java.io.*;
import java.net.*;
import org.json.*;

public class SiteService {
    private static final String BASE_URL = "http://localhost:5000/api";

    // Récupérer tous les site
    public static JSONArray getAllSite() throws Exception {
        URL url = new URL(BASE_URL + "/site");
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

    // Rechercher site
    public static JSONArray searchSite(String n_site) throws Exception {
        URL url = new URL(BASE_URL + "/site/" + n_site);
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

    // Créer site
    public static boolean createSite(String n_site, String nom, String lieu, Float tarif_journalier) throws Exception {
        URL url = new URL(BASE_URL + "/site");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("n_site", n_site);
        json.put("nom", nom);
        json.put("lieu", lieu);
        json.put("tarif_journalier", tarif_journalier);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();

        int responseCode = conn.getResponseCode();
        return responseCode == 201;
    }

    // Modifier site
    public static boolean updateSite(String n_site, String nom, String lieu, Float tarif_journalier) throws Exception {
        URL url = new URL(BASE_URL + "/site/" + n_site);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("lieu", lieu);
        json.put("tarif_journalier", tarif_journalier);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    // Supprimer site
    public static boolean deleteSite(String n_site) throws Exception {
        URL url = new URL(BASE_URL + "/site/" + n_site);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }
}