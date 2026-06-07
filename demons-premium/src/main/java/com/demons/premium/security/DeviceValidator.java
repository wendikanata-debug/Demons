package com.demons.premium.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import java.security.MessageDigest;

/**
 * DeviceValidator - Validasi dan binding device ID
 */
public class DeviceValidator {
    private static final String TAG = "DeviceValidator";
    private static final String PREFS_NAME = "demons_premium_security";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DEVICE_HASH = "device_hash";
    
    private Context context;
    private SecurityConfig config;
    private SharedPreferences prefs;
    
    public DeviceValidator(Context context, SecurityConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Get unique device ID
     */
    public String getDeviceId() {
        try {
            // Get Android ID (unique untuk device)
            String androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            );
            
            if (androidId == null || androidId.isEmpty()) {
                androidId = Build.SERIAL;
            }
            
            return androidId;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting device ID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Generate device hash
     */
    public String generateDeviceHash() {
        try {
            String deviceId = getDeviceId();
            if (deviceId == null) return null;
            
            // Create hash of device ID
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(deviceId.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error generating device hash: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Bind license ke device ini
     */
    public boolean bindDeviceLicense() {
        try {
            String deviceId = getDeviceId();
            String deviceHash = generateDeviceHash();
            
            if (deviceId == null || deviceHash == null) {
                return false;
            }
            
            // Save device info
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_DEVICE_ID, deviceId);
            editor.putString(KEY_DEVICE_HASH, deviceHash);
            editor.apply();
            
            android.util.Log.d(TAG, "Device license bound");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error binding device: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validate jika device sudah terikat
     */
    public boolean validateDeviceBinding() {
        try {
            String storedDeviceId = prefs.getString(KEY_DEVICE_ID, null);
            String storedDeviceHash = prefs.getString(KEY_DEVICE_HASH, null);
            
            // Jika tidak pernah di-bind, bind sekarang
            if (storedDeviceId == null) {
                return bindDeviceLicense();
            }
            
            // Verify device masih sama
            String currentDeviceId = getDeviceId();
            String currentDeviceHash = generateDeviceHash();
            
            boolean isValid = currentDeviceId.equals(storedDeviceId) && 
                             currentDeviceHash.equals(storedDeviceHash);
            
            if (!isValid) {
                android.util.Log.w(TAG, "Device mismatch detected");
            }
            
            return isValid;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Device validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get stored device ID
     */
    public String getStoredDeviceId() {
        return prefs.getString(KEY_DEVICE_ID, null);
    }
    
    /**
     * Get device model info
     */
    public String getDeviceInfo() {
        return Build.MANUFACTURER + " " + Build.MODEL + " (Android " + Build.VERSION.RELEASE + ")";
    }
    
    /**
     * Reset device binding
     */
    public void resetDeviceBinding() {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(KEY_DEVICE_ID);
            editor.remove(KEY_DEVICE_HASH);
            editor.apply();
            
            android.util.Log.d(TAG, "Device binding reset");
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error resetting device binding: " + e.getMessage());
        }
    }
}
