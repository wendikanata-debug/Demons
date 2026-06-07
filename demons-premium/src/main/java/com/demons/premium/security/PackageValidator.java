package com.demons.premium.security;

import android.content.Context;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PackageValidator - Validasi package name yang diizinkan
 */
public class PackageValidator {
    private static final String TAG = "PackageValidator";
    
    private Context context;
    private Set<String> whitelistedPackages;
    private SecurityConfig config;
    
    public PackageValidator(Context context, SecurityConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
        this.whitelistedPackages = new HashSet<>();
        initializeWhitelist();
    }
    
    /**
     * Initialize whitelist (bisa dari config atau hardcoded)
     */
    private void initializeWhitelist() {
        try {
            // Default whitelist (bisa disesuaikan)
            // whitelistedPackages.add("com.tencent.ig"); // PUBG Mobile
            // whitelistedPackages.add("com.activision.callofduty.shooter"); // COD Mobile
            
            // Atau ambil dari config
            if (config.getPackageName() != null) {
                whitelistedPackages.add(config.getPackageName());
            }
            
            android.util.Log.d(TAG, "Whitelist initialized with " + whitelistedPackages.size() + " packages");
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error initializing whitelist: " + e.getMessage());
        }
    }
    
    /**
     * Validate current package name
     */
    public boolean validatePackage() {
        try {
            String currentPackage = context.getPackageName();
            
            // Check if package is in whitelist
            if (whitelistedPackages.contains(currentPackage)) {
                android.util.Log.d(TAG, "Package valid: " + currentPackage);
                return true;
            }
            
            android.util.Log.e(TAG, "Package not whitelisted: " + currentPackage);
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Package validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add package to whitelist
     */
    public void addPackage(String packageName) {
        try {
            whitelistedPackages.add(packageName);
            android.util.Log.d(TAG, "Package added to whitelist: " + packageName);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error adding package: " + e.getMessage());
        }
    }
    
    /**
     * Add multiple packages to whitelist
     */
    public void addPackages(List<String> packageNames) {
        try {
            whitelistedPackages.addAll(packageNames);
            android.util.Log.d(TAG, "Packages added to whitelist: " + packageNames.size());
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error adding packages: " + e.getMessage());
        }
    }
    
    /**
     * Remove package from whitelist
     */
    public void removePackage(String packageName) {
        try {
            whitelistedPackages.remove(packageName);
            android.util.Log.d(TAG, "Package removed from whitelist: " + packageName);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error removing package: " + e.getMessage());
        }
    }
    
    /**
     * Get current application package name
     */
    public String getCurrentPackageName() {
        return context.getPackageName();
    }
    
    /**
     * Get list of whitelisted packages
     */
    public List<String> getWhitelistedPackages() {
        return new ArrayList<>(whitelistedPackages);
    }
    
    /**
     * Clear all whitelisted packages
     */
    public void clearWhitelist() {
        whitelistedPackages.clear();
        android.util.Log.d(TAG, "Whitelist cleared");
    }
    
    /**
     * Check if package is installed
     */
    public boolean isPackageInstalled(String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
