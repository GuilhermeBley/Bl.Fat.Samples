package com.example.blfatsamples.model;

import org.json.JSONObject;
import android.util.Base64;

public class UserLoginResultModel {
    private  int id;
    private  String name;
    private  String token;
    private  String email;

    public UserLoginResultModel(int id, String name, String token, String email) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean IsTokenExpired(){
        if (token == null || token.isBlank()) return true;

        return isTokenExpired(token);
    }
    private static boolean isTokenExpired(String jwtToken) {
        try {
            // Split the JWT into its three parts
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                return true;
            }
            byte[] decodedBytes = Base64.decode(
                    parts[1],
                    Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP
            );
            // Decode the payload (second part)
            String payload = new String(decodedBytes, "UTF-8");

            // Parse the JSON payload
            JSONObject payloadJson = new JSONObject(payload);

            // Get the expiration time (exp claim)
            if (!payloadJson.has("exp")) {
                // No expiration claim means the token doesn't expire
                return false;
            }

            long expTimestamp = payloadJson.getLong("exp");
            long currentTimestamp = System.currentTimeMillis() / 1000;

            // Check if token is expired
            return currentTimestamp > expTimestamp;

        } catch (Exception e) {
            // Handle parsing errors as expired tokens
            return true;
        }
    }
}
