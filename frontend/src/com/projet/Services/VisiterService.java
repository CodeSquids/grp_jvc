//package com.projet.Services;
//
//import java.io.*;
//import java.net.*;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.sql.Date;
//import org.json.*;
//
//public class VisiterService {
//    private static final String BASE_URL = "http://localhost:5000/api";
//
//    // Récupérer tous les site
//    public static JSONArray getAllVisiter() throws Exception {
//        URL url = new URL(BASE_URL + "/visiter");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(conn.getInputStream())
//        );
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            response.append(line);
//        }
//        reader.close();
//
//        return new JSONArray(response.toString());
//    }
//
//    // Rechercher visiter
//    public static JSONArray searchVisiter(String n_visiter) throws Exception {
//        URL url = new URL(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8"));
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode == 404) {
//            return new JSONArray();
//        }
//        if (responseCode >= 400) {
//            throw new IOException(readResponse(conn));
//        }
//
//        return asJSONArray(readResponse(conn));
//    }
//
//    public static JSONObject getVisiterById(String n_visiter) throws Exception {
//        JSONArray result = searchVisiter(n_visiter);
//        return result.length() == 0 ? null : result.getJSONObject(0);
//    }
//
//    // Créer visiter
//    public static boolean createVisiter(String n_visiter, String n_visiteur, String n_site, int nbjours, Date date_visite) throws Exception {
//        URL url = new URL(BASE_URL + "/visiter");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
//        JSONObject json = new JSONObject();
//        json.put("n_visiter", n_visiter);
//        json.put("n_visiteur", n_visiteur);
//        json.put("n_site", n_site);
//        json.put("nbjours", nbjours);
//        json.put("date_visite", date_visite);
//
//        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//        writer.write(json.toString());
//        writer.flush();
//
//        int responseCode = conn.getResponseCode();
//        return responseCode == 201;
//    }
//
//    // Modifier visiter
//    public static boolean updateVisiter(String n_visiter, String n_visiteur, String n_site, int nbjours, Date date_visite) throws Exception {
//        URL url = new URL(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8"));
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("PUT");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
//        JSONObject json = new JSONObject();
//        json.put("n_visiteur", n_visiteur);
//        json.put("n_site", n_site);
//        json.put("nbjours", nbjours);
//        json.put("date_visite", date_visite);
//
//        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//        writer.write(json.toString());
//        writer.flush();
//
//        int responseCode = conn.getResponseCode();
//        return responseCode == 200;
//    }
//
//    // Supprimer visiter
//    public static boolean deleteVisiter(String n_visiter) throws Exception {
//        URL url = new URL(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8"));
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("DELETE");
//
//        int responseCode = conn.getResponseCode();
//        return responseCode == 200;
//    }
//
//    public static JSONArray complex1(String site_nom, Date date_start, Date date_end) throws Exception {
//        JSONObject json = new JSONObject();
//        json.put("site_nom", site_nom);
//        json.put("date_start", date_start.toString());
//        json.put("date_end", date_end.toString());
//
//        return sendGetJsonBody(BASE_URL + "/visiter/complex1", json);
//    }
//
//    public static JSONArray complex3() throws Exception {
//
//        URL url = new URL(BASE_URL + "/visiter/complex3");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(conn.getInputStream())
//        );
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            response.append(line);
//        }
//        reader.close();
//
//        return new JSONArray(response.toString());
//    }
//
//    public static JSONArray complex2(Date date_start, Date date_end) throws Exception {
//        JSONObject json = new JSONObject();
//        json.put("date_start", date_start.toString());
//        json.put("date_end", date_end.toString());
//
//        return sendGetJsonBody(BASE_URL + "/visiter/complex2", json);
//    }
//
//    public static JSONArray complex4() throws Exception {
//        URL url = new URL(BASE_URL + "/visiter/complex4");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(conn.getInputStream())
//        );
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            response.append(line);
//        }
//        reader.close();
//
//        return new JSONArray(response.toString());
//    }
//
//    private static JSONArray sendGetJsonBody(String url, JSONObject json) throws Exception {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Content-Type", "application/json")
//                .header("Accept", "application/json")
//                .method("GET", HttpRequest.BodyPublishers.ofString(json.toString()))
//                .build();
//
//        HttpResponse<String> response = HttpClient.newHttpClient()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() >= 400) {
//            throw new IOException(response.body());
//        }
//
//        return new JSONArray(response.body());
//    }
//
//    private static String readResponse(HttpURLConnection conn) throws IOException {
//        InputStream stream = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
//        if (stream == null) {
//            return "";
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            response.append(line);
//        }
//        reader.close();
//        return response.toString();
//    }
//
//    private static JSONArray asJSONArray(String response) {
//        Object json = new JSONTokener(response).nextValue();
//        if (json instanceof JSONArray) {
//            return (JSONArray) json;
//        }
//
//        JSONArray result = new JSONArray();
//        result.put((JSONObject) json);
//        return result;
//    }
//}
package com.projet.Services;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import org.json.*;

public class VisiterService {
    private static final String BASE_URL = "http://localhost:5000/api";

    // Récupérer tous les visites
    public static JSONArray getAllVisiter() throws Exception {
        URL url = URI.create(BASE_URL + "/visiter").toURL();
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
        URL url = URI.create(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8")).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == 404) {
            return new JSONArray();
        }
        if (responseCode >= 400) {
            throw new IOException(readResponse(conn));
        }

        return asJSONArray(readResponse(conn));
    }

    public static JSONObject getVisiterById(String n_visiter) throws Exception {
        JSONArray result = searchVisiter(n_visiter);
        return result.length() == 0 ? null : result.getJSONObject(0);
    }

    // Créer visiter
    public static boolean createVisiter(String n_visiter, String n_visiteur, String n_site, int nbjours, Date date_visite) throws Exception {
        URL url = URI.create(BASE_URL + "/visiter").toURL();
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
        URL url = URI.create(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8")).toURL();
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
        URL url = URI.create(BASE_URL + "/visiter/" + URLEncoder.encode(n_visiter, "UTF-8")).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    // ===================== REQUÊTE 1 : Utiliser POST au lieu de GET =====================
    public static JSONArray complex1(String site_nom, Date date_start, Date date_end) throws Exception {
        JSONObject json = new JSONObject();
        json.put("site_nom", site_nom);
        json.put("date_start", date_start.toString());
        json.put("date_end", date_end.toString());

        // Changement : utiliser POST au lieu de GET
        return sendPostJsonBody(BASE_URL + "/visiter/complex1", json);
    }

    // ===================== REQUÊTE 2 : Utiliser POST au lieu de GET =====================
    public static JSONArray complex2(Date date_start, Date date_end) throws Exception {
        JSONObject json = new JSONObject();
        json.put("date_start", date_start.toString());
        json.put("date_end", date_end.toString());

        // Changement : utiliser POST au lieu de GET
        return sendPostJsonBody(BASE_URL + "/visiter/complex2", json);
    }

    public static JSONArray complex3() throws Exception {
        URL url = URI.create(BASE_URL + "/visiter/complex3").toURL();
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

    public static JSONArray complex4() throws Exception {
        URL url = URI.create(BASE_URL + "/visiter/complex4").toURL();
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

    // Nouvelle méthode pour envoyer des requêtes POST avec body JSON
    private static JSONArray sendPostJsonBody(String url, JSONObject json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IOException(response.body());
        }

        return new JSONArray(response.body());
    }

    // Ancienne méthode GET (gardée pour compatibilité mais non utilisée)
    private static JSONArray sendGetJsonBody(String url, JSONObject json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?" + jsonToStringParams(json)))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IOException(response.body());
        }

        return new JSONArray(response.body());
    }

    private static String jsonToStringParams(JSONObject json) {
        StringBuilder params = new StringBuilder();
        for (String key : json.keySet()) {
            if (params.length() > 0) params.append("&");
            params.append(key).append("=").append(URLEncoder.encode(json.get(key).toString(), java.nio.charset.StandardCharsets.UTF_8));
        }
        return params.toString();
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        InputStream stream = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (stream == null) {
            return "";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static JSONArray asJSONArray(String response) {
        try {
            Object json = new JSONTokener(response).nextValue();
            if (json instanceof JSONArray) {
                return (JSONArray) json;
            }
            JSONArray result = new JSONArray();
            result.put((JSONObject) json);
            return result;
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}