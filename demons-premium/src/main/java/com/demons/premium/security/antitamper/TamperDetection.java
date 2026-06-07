package com.demons.premium.security.antitamper;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * TamperDetection - Deteksi modifikasi pada library dan APK
 */
public class TamperDetection {
    private static final String TAG = "TamperDetection";
    
    private Context context;
    private CodeIntegrityCheck integrityCheck;
    private List<String> suspiciousProcesses;
    private List<String> suspiciousPackages;
    
    public TamperDetection(Context context) {
        this.context = context.getApplicationContext();
        this.integrityCheck = new CodeIntegrityCheck(context);
        this.suspiciousProcesses = new ArrayList<>();
        this.suspiciousPackages = new ArrayList<>();
        initializeSuspiciousList();
    }
    
    /**
     * Initialize list of suspicious processes dan packages
     */
    private void initializeSuspiciousList() {
        // Suspicious processes (tools untuk modifikasi/hack)
        suspiciousProcesses.add("frida-server");
        suspiciousProcesses.add("xposed");
        suspiciousProcesses.add("substrate");
        suspiciousProcesses.add("substrated");
        suspiciousProcesses.add("dexposed");
        suspiciousProcesses.add("superuser");
        suspiciousProcesses.add("su");
        suspiciousProcesses.add("magisk");
        suspiciousProcesses.add("ebinit");
        
        // Suspicious packages (apps untuk modifikasi/hack)
        suspiciousPackages.add("com.chelpus.lackypatch");
        suspiciousPackages.add("com.dimonvideo.luckypatcher");
        suspiciousPackages.add("com.xmodgames");
        suspiciousPackages.add("com.androidvts.umd");
        suspiciousPackages.add("com.ramdroid.appquanta");
        suspiciousPackages.add("de.robv.android.xposed.installer");
        suspiciousPackages.add("com.frida.server");
        suspiciousPackages.add("com.topjohnwu.magisk");
    }
    
    /**
     * Check for code injection
     */
    public boolean checkCodeInjection() {
        try {
            android.util.Log.d(TAG, "Checking for code injection...");
            
            // Check if library code is modified
            if (!integrityCheck.checkDexIntegrity()) {
                android.util.Log.w(TAG, "DEX integrity check failed - possible injection");
                return false;
            }
            
            if (!integrityCheck.checkLibraryIntegrity()) {
                android.util.Log.w(TAG, "Library integrity check failed - possible injection");
                return false;
            }
            
            android.util.Log.d(TAG, "No code injection detected ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking code injection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for suspicious processes
     */
    public boolean checkSuspiciousProcesses() {
        try {
            android.util.Log.d(TAG, "Checking for suspicious processes...");
            
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("/proc/net/tcp")
            );
            
            String line;
            while ((line = reader.readLine()) != null) {
                for (String process : suspiciousProcesses) {
                    if (line.contains(process)) {
                        android.util.Log.w(TAG, "Suspicious process detected: " + process);
                        reader.close();
                        return false;
                    }
                }
            }
            reader.close();
            
            // Check running processes
            try {
                java.io.BufferedReader ps = new java.io.BufferedReader(
                    new java.io.InputStreamReader(
                        Runtime.getRuntime().exec("ps").getInputStream()
                    )
                );
                
                while ((line = ps.readLine()) != null) {
                    for (String process : suspiciousProcesses) {
                        if (line.contains(process)) {
                            android.util.Log.w(TAG, "Suspicious process detected: " + process);
                            ps.close();
                            return false;
                        }
                    }
                }
                ps.close();
            } catch (Exception e) {
                android.util.Log.d(TAG, "Could not check ps output: " + e.getMessage());
            }
            
            android.util.Log.d(TAG, "No suspicious processes detected ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking processes: " + e.getMessage());
            return true; // Don't fail if we can't check
        }
    }
    
    /**
     * Check for suspicious packages installed
     */
    public boolean checkSuspiciousPackages() {
        try {
            android.util.Log.d(TAG, "Checking for suspicious packages...");
            
            android.content.pm.PackageManager pm = context.getPackageManager();
            
            for (String suspiciousPackage : suspiciousPackages) {
                try {
                    pm.getPackageInfo(suspiciousPackage, android.content.pm.PackageManager.GET_ACTIVITIES);
                    android.util.Log.w(TAG, "Suspicious package installed: " + suspiciousPackage);
                    return false;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    // Package not installed, good
                }
            }
            
            android.util.Log.d(TAG, "No suspicious packages detected ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking packages: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for file tampering
     */
    public boolean checkFileTampering() {
        try {
            android.util.Log.d(TAG, "Checking for file tampering...");
            
            // Check if app files are writable (sign of tampering)
            String apkPath = context.getApplicationInfo().sourceDir;
            java.io.File apkFile = new java.io.File(apkPath);
            
            // APK should not be writable
            if (apkFile.canWrite()) {
                android.util.Log.w(TAG, "APK file is writable - possible tampering");
                return false;
            }
            
            android.util.Log.d(TAG, "No file tampering detected ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking file tampering: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Run all tamper checks
     */
    public boolean runAllChecks() {
        try {
            android.util.Log.d(TAG, "\n========== TAMPER DETECTION START ==========");
            
            boolean codeInjection = checkCodeInjection();
            boolean suspiciousProc = checkSuspiciousProcesses();
            boolean suspiciousPkg = checkSuspiciousPackages();
            boolean fileTamper = checkFileTampering();
            
            boolean allPassed = codeInjection && suspiciousProc && suspiciousPkg && fileTamper;
            
            android.util.Log.d(TAG, "");
            if (allPassed) {
                android.util.Log.d(TAG, "========== TAMPER DETECTION PASSED ✓ ==========");
            } else {
                android.util.Log.w(TAG, "========== TAMPER DETECTED ✗ ==========");
            }
            
            return allPassed;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Tamper detection error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add suspicious process to list
     */
    public void addSuspiciousProcess(String processName) {
        suspiciousProcesses.add(processName);
    }
    
    /**
     * Add suspicious package to list
     */
    public void addSuspiciousPackage(String packageName) {
        suspiciousPackages.add(packageName);
    }
}
