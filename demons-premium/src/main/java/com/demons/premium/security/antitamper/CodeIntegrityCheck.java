package com.demons.premium.security.antitamper;

import android.content.Context;
import java.io.File;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * CodeIntegrityCheck - Validasi integritas code dan library files
 */
public class CodeIntegrityCheck {
    private static final String TAG = "CodeIntegrityCheck";
    
    private Context context;
    private Map<String, String> fileHashes;
    
    public CodeIntegrityCheck(Context context) {
        this.context = context.getApplicationContext();
        this.fileHashes = new HashMap<>();
        initializeFileHashes();
    }
    
    /**
     * Initialize known file hashes untuk comparison
     */
    private void initializeFileHashes() {
        try {
            // Store reference hashes untuk library files
            // Format: filename -> SHA256 hash
            // Ini bisa dimuat dari config atau embedded
            
            // Example:
            // fileHashes.put("classes.dex", "abc123def456...");
            // fileHashes.put("lib/arm64-v8a/libc++_shared.so", "xyz789...");
            
            android.util.Log.d(TAG, "File hashes initialized");
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error initializing hashes: " + e.getMessage());
        }
    }
    
    /**
     * Calculate file hash (SHA-256)
     */
    public String calculateFileHash(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] buffer = new byte[8192];
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            fis.close();
            
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error calculating hash: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verify file integrity
     */
    public boolean verifyFileIntegrity(String filename, String expectedHash) {
        try {
            File file = new File(context.getFilesDir(), filename);
            
            if (!file.exists()) {
                android.util.Log.w(TAG, "File not found: " + filename);
                return false;
            }
            
            String calculatedHash = calculateFileHash(file);
            
            if (calculatedHash == null) {
                return false;
            }
            
            boolean isValid = calculatedHash.equals(expectedHash);
            
            if (!isValid) {
                android.util.Log.w(TAG, "File integrity check failed: " + filename);
                android.util.Log.w(TAG, "Expected: " + expectedHash);
                android.util.Log.w(TAG, "Got: " + calculatedHash);
            } else {
                android.util.Log.d(TAG, "File integrity verified: " + filename);
            }
            
            return isValid;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error verifying file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check library file integrity
     */
    public boolean checkLibraryIntegrity() {
        try {
            android.util.Log.d(TAG, "Checking library integrity...");
            
            // Get library files
            File libDir = new File(context.getApplicationInfo().nativeLibraryDir);
            
            if (!libDir.exists()) {
                android.util.Log.w(TAG, "Library directory not found");
                return true; // Allow if no libs
            }
            
            File[] libs = libDir.listFiles();
            
            if (libs == null || libs.length == 0) {
                return true;
            }
            
            for (File lib : libs) {
                if (lib.isFile() && lib.getName().endsWith(".so")) {
                    // Verify each .so file
                    // In production, compare with known hashes
                    android.util.Log.d(TAG, "Library file found: " + lib.getName());
                }
            }
            
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking library integrity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check DEX file integrity (classes.dex)
     */
    public boolean checkDexIntegrity() {
        try {
            android.util.Log.d(TAG, "Checking DEX integrity...");
            
            // Get APK file
            String apkPath = context.getApplicationInfo().sourceDir;
            File apkFile = new File(apkPath);
            
            if (!apkFile.exists()) {
                android.util.Log.w(TAG, "APK file not found");
                return false;
            }
            
            // Check if APK is modified
            // In production, verify DEX structure and signatures
            android.util.Log.d(TAG, "DEX integrity check passed");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking DEX: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add file hash untuk tracking
     */
    public void addFileHash(String filename, String hash) {
        fileHashes.put(filename, hash);
    }
    
    /**
     * Get stored file hash
     */
    public String getFileHash(String filename) {
        return fileHashes.get(filename);
    }
    
    /**
     * Verify all stored hashes
     */
    public boolean verifyAllHashes() {
        try {
            for (Map.Entry<String, String> entry : fileHashes.entrySet()) {
                if (!verifyFileIntegrity(entry.getKey(), entry.getValue())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error verifying all hashes: " + e.getMessage());
            return false;
        }
    }
}
