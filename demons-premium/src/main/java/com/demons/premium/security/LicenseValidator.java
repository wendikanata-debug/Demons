package com.demons.premium.security;

import android.content.Context;
import java.util.Calendar;

/**
 * LicenseValidator - Validasi license key dan expiry date
 */
public class LicenseValidator {
    private static final String TAG = "LicenseValidator";
    private static final int MIN_LICENSE_KEY_LENGTH = 32;
    
    private Context context;
    private SecurityConfig config;
    
    public LicenseValidator(Context context, SecurityConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
    }
    
    /**
     * Validate license key format and expiry
     */
    public boolean validateLicense() {
        try {
            // Check if license key exists
            if (config.getLicenseKey() == null || config.getLicenseKey().isEmpty()) {
                android.util.Log.e(TAG, "License key is empty");
                return false;
            }
            
            // Check license key format (minimum length)
            if (config.getLicenseKey().length() < MIN_LICENSE_KEY_LENGTH) {
                android.util.Log.e(TAG, "License key format invalid");
                return false;
            }
            
            // Check if license is expired
            if (config.getLicenseExpiryDate() > 0) {
                long currentTime = System.currentTimeMillis();
                if (currentTime > config.getLicenseExpiryDate()) {
                    android.util.Log.e(TAG, "License expired");
                    return false;
                }
            }
            
            android.util.Log.d(TAG, "License valid");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "License validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get license expiry days remaining
     */
    public long getDaysRemaining() {
        try {
            if (config.getLicenseExpiryDate() <= 0) {
                return Long.MAX_VALUE; // No expiry
            }
            
            long currentTime = System.currentTimeMillis();
            long remainingTime = config.getLicenseExpiryDate() - currentTime;
            
            if (remainingTime <= 0) {
                return 0; // Expired
            }
            
            return remainingTime / (1000 * 60 * 60 * 24); // Convert to days
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting days remaining: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Check if license will expire soon (within 7 days)
     */
    public boolean isExpiringSoon() {
        return getDaysRemaining() > 0 && getDaysRemaining() <= 7;
    }
    
    /**
     * Generate license key (for admin purposes)
     */
    public static String generateLicenseKey(String packageName, long expiryDate) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String expiry = String.valueOf(expiryDate);
            
            // Simple hash-based key generation
            String combined = packageName + timestamp + expiry;
            String hash = Integer.toHexString(combined.hashCode());
            
            // Create 32+ char key
            return (packageName + "_" + hash + "_" + System.nanoTime()).substring(0, 32);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error generating license key: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Validate license key format
     */
    public static boolean isValidFormat(String licenseKey) {
        return licenseKey != null && licenseKey.length() >= MIN_LICENSE_KEY_LENGTH;
    }
}
