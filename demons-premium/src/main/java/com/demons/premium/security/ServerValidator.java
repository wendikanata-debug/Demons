package com.demons.premium.security;

import android.content.Context;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * ServerValidator - Validasi license key dengan server
 */
public class ServerValidator {
    private static final String TAG = "ServerValidator";
    private static final int TIMEOUT = 10000; // 10 seconds
    
    private Context context;
    private SecurityConfig config;
    
    public ServerValidator(Context context, SecurityConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
    }
    
    /**
     * Validate license dengan server
     */
    public boolean validateLicenseWithServer() {
        try {
            if (config.getServerUrl() == null || config.getServerUrl().isEmpty()) {
                android.util.Log.w(TAG, "Server URL not configured");
                return false;
            }
            
            // Create request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("license_key", config.getLicenseKey());
            requestBody.put("api_key", config.getApiKey());
            requestBody.put("package_name", config.getPackageName());
            requestBody.put("device_id", config.getDeviceId());
            requestBody.put("timestamp", System.currentTimeMillis());
            
            // Send request to server
            HttpURLConnection connection = (HttpURLConnection) 
                new URL(config.getServerUrl() + "/validate").openConnection();
            
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setDoOutput(true);
            
            // Write request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                String response = readResponse(connection);
                return parseResponse(response);
            } else {
                android.util.Log.e(TAG, "Server response code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Server validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read response from server
     */
    private String readResponse(HttpURLConnection connection) {
        try {
            Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error reading response: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Parse server response
     */
    private boolean parseResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            
            boolean isValid = jsonResponse.getBoolean("valid");
            String message = jsonResponse.optString("message", "");
            long expiryDate = jsonResponse.optLong("expiry_date", 0);
            
            if (expiryDate > 0) {
                config.setLicenseExpiryDate(expiryDate);
            }
            
            android.util.Log.d(TAG, "Server validation: " + (isValid ? "VALID" : "INVALID") + " - " + message);
            return isValid;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error parsing response: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check server connectivity
     */
    public boolean isServerAvailable() {
        try {
            URL url = new URL(config.getServerUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("HEAD");
            
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (Exception e) {
            android.util.Log.w(TAG, "Server not available: " + e.getMessage());
            return false;
        }
    }
}
