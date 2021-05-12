package xyz.fragbots.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/*

* Easy request function to make it easier to communicate with the backend

*/

public class Request {
    public static String getRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            return response.toString();
        } catch (IOException ex) {
            return null;
        }

    }
    public static JsonObject getRequestJson(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            String resp = response.toString();
            return new Gson().fromJson(resp, JsonObject.class);
        } catch (IOException e) {
            return null;
        }

    }
    public static String postRequest(String urlString, String body){
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            return response.toString();
        }catch (IOException ex){
                return null;
        }
    }
}
