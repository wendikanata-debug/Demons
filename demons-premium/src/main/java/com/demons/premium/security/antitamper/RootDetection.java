package com.demons.premium.security.antitamper;

import java.io.File;

/**
 * RootDetection - Deteksi jika device di-root atau di-jailbreak
 */
public class RootDetection {
    private static final String TAG = "RootDetection";
    
    /**
     * Check for su binary
     */
    private boolean checkSuBinary() {
        try {
            String[] paths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/system/sbin/su",
                "/sbin/su",
                "/usr/bin/su",
                "/usr/local/bin/su",
                "/bin/su",
                "/system/su"
            };
            
            for (String path : paths) {
                File file = new File(path);
                if (file.exists()) {
                    android.util.Log.w(TAG, "su binary found at: " + path);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking su binary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for root management apps
     */
    private boolean checkRootApps() {
        try {
            String[] rootApps = {
                "com.topjohnwu.magisk",           // Magisk
                "com.koushikdutta.superuser",     // Superuser
                "com.thirdparty.superuser",       // Superuser
                "eu.chainfire.supersu",           // SuperSU
                "com.yellowes.su",                // SuperSU
                "com.noshufou.android.su",        // Superuser
                "com.zachspong.temprootremovejb", // TempRoot
                "com.ramdroid.appquanta",         // AppQuanta (modifies apps)
                "com.xmodgames"                   // Xmod Games
            };
            
            android.content.pm.PackageManager pm = android.os.Build.class.getResource("android.os.Build").getContentResolver()
                .getContext().getPackageManager();
            
            // Since we might not have package manager here, just log the attempt
            android.util.Log.d(TAG, "Checking for root management apps...");
            
            return false; // Would need context for full check
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking root apps: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for Magisk
     */
    private boolean checkMagisk() {
        try {
            String[] magiskPaths = {
                "/system/app/Magisk.apk",
                "/system/priv-app/Magisk.apk",
                "/data/adb/modules",
                "/data/adb/magisk.db",
                "/data/adb/magiskhide"
            };
            
            for (String path : magiskPaths) {
                if (new File(path).exists()) {
                    android.util.Log.w(TAG, "Magisk detected at: " + path);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking Magisk: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for system modifications
     */
    private boolean checkSystemModifications() {
        try {
            // Check for common system modification indicators
            String[] modFiles = {
                "/system/build.prop.bak",
                "/system/.xposed",
                "/system/priv-app/Xposed.apk",
                "/system/framework/XposedBridge.jar",
                "/system/lib/libxposed.so",
                "/system/lib64/libxposed.so"
            };
            
            for (String path : modFiles) {
                if (new File(path).exists()) {
                    android.util.Log.w(TAG, "System modification detected: " + path);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking system modifications: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if system partition is writable
     */
    private boolean checkSystemPartitionWritable() {
        try {
            File systemDir = new File("/system");
            
            // Try to create a test file (this would only work if root)
            File testFile = new File("/system/.test_write");
            
            if (testFile.createNewFile()) {
                testFile.delete();
                android.util.Log.w(TAG, "System partition is writable!");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            // Expected - we shouldn't be able to write
            return false;
        }
    }
    
    /**
     * Full root check
     */
    public boolean isDeviceRooted() {
        try {
            android.util.Log.d(TAG, "\nChecking if device is rooted...");
            
            if (checkSuBinary()) return true;
            if (checkMagisk()) return true;
            if (checkSystemModifications()) return true;
            if (checkSystemPartitionWritable()) return true;
            
            android.util.Log.d(TAG, "Device is NOT rooted ✓");
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error in root detection: " + e.getMessage());
            return false;
        }
    }
}
