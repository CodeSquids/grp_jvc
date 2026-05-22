package com.projet;

import java.io.*;
import java.net.*;
import org.json.*;

public class VisiteurService {
    private static final String BASE_URL = "http://localhost:5000/api";
    
    // Récupérer tous les visiteurs
    public static JSONArray getAllVisiteurs() throws Exception {
        URL url = new URL(BASE_URL + "/visiteurs");
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
    
    // Rechercher visiteur
    public static JSONArray searchVisiteur(String critere, String valeur) throws Exception {
        String urlString = BASE_URL + "/visiteurs/search?critere=" + 
                          URLEncoder.encode(critere, "UTF-8") + 
                          "&valeur=" + URLEncoder.encode(valeur, "UTF-8");
        URL url = new URL(urlString);
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
    
    // Créer visiteur
    public static boolean createVisiteur(String n_visiteur, String nom, String adresse) throws Exception {
        URL url = new URL(BASE_URL + "/visiteurs");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        JSONObject json = new JSONObject();
        json.put("n_visiteur", n_visiteur);
        json.put("nom", nom);
        json.put("adresse", adresse);
        
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();
        
        int responseCode = conn.getResponseCode();
        return responseCode == 201;
    }
    
    // Modifier visiteur
    public static boolean updateVisiteur(String n_visiteur, String nom, String adresse) throws Exception {
        URL url = new URL(BASE_URL + "/visiteurs/" + n_visiteur);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("adresse", adresse);
        
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json.toString());
        writer.flush();
        
        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }
    
    // Supprimer visiteur
    public static boolean deleteVisiteur(String n_visiteur) throws Exception {
        URL url = new URL(BASE_URL + "/visiteurs/" + n_visiteur);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        
        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }
}