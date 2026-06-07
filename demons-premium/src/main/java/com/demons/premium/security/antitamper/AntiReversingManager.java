package com.demons.premium.security.antitamper;

import android.content.Context;

/**
 * AntiReversingManager - Central manager untuk anti-tampering dan anti-reversing
 * Menggabungkan semua protection mechanisms
 */
public class AntiReversingManager {
    private static final String TAG = "AntiReversingManager";
    private static AntiReversingManager instance;
    
    private Context context;
    private TamperDetection tamperDetection;
    private DebuggerDetection debuggerDetection;
    private EmulatorDetection emulatorDetection;
    private RootDetection rootDetection;
    private APKVerification apkVerification;
    
    private boolean protectionEnabled;
    private String protectionError;
    
    /**
     * Get singleton instance
     */
    public static synchronized AntiReversingManager getInstance(Context context) {
        if (instance == null) {
            instance = new AntiReversingManager(context);
        }
        return instance;
    }
    
    /**
     * Private constructor
     */
    private AntiReversingManager(Context context) {
        this.context = context.getApplicationContext();
        initializeProtections();
    }
    
    /**
     * Initialize semua protection modules
     */
    private void initializeProtections() {
        try {
            this.tamperDetection = new TamperDetection(context);
            this.debuggerDetection = new DebuggerDetection(context);
            this.emulatorDetection = new EmulatorDetection(context);
            this.rootDetection = new RootDetection();
            this.apkVerification = new APKVerification(context);
            
            protectionEnabled = true;
            android.util.Log.d(TAG, "AntiReversingManager initialized");
        } catch (Exception e) {
            protectionError = "Initialization error: " + e.getMessage();
            android.util.Log.e(TAG, protectionError);
        }
    }
    
    /**
     * PROTECTION LAYER 1: Check Tamper Detection
     */
    private boolean checkLayer1_Tamper() {
        try {
            android.util.Log.d(TAG, "[Protection 1] Checking for tampering...");
            
            if (!tamperDetection.runAllChecks()) {
                protectionError = "Tampering detected on library";
                android.util.Log.e(TAG, "[Protection 1] " + protectionError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Protection 1] Tamper Detection PASSED ✓");
            return true;
        } catch (Exception e) {
            protectionError = "Tamper detection error: " + e.getMessage();
            android.util.Log.e(TAG, "[Protection 1] " + protectionError);
            return false;
        }
    }
    
    /**
     * PROTECTION LAYER 2: Check Debugger
     */
    private boolean checkLayer2_Debugger() {
        try {
            android.util.Log.d(TAG, "[Protection 2] Checking for debugger...");
            
            if (debuggerDetection.isBeingDebugged()) {
                protectionError = "Debugger detected - cannot use in debug mode";
                android.util.Log.e(TAG, "[Protection 2] " + protectionError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Protection 2] Debugger Check PASSED ✓");
            return true;
        } catch (Exception e) {
            protectionError = "Debugger detection error: " + e.getMessage();
            android.util.Log.e(TAG, "[Protection 2] " + protectionError);
            return false;
        }
    }
    
    /**
     * PROTECTION LAYER 3: Check Emulator
     */
    private boolean checkLayer3_Emulator() {
        try {
            android.util.Log.d(TAG, "[Protection 3] Checking for emulator...");
            
            if (emulatorDetection.isRunningOnEmulator()) {
                protectionError = "Emulator detected - library only works on real devices";
                android.util.Log.e(TAG, "[Protection 3] " + protectionError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Protection 3] Emulator Check PASSED ✓");
            return true;
        } catch (Exception e) {
            protectionError = "Emulator detection error: " + e.getMessage();
            android.util.Log.e(TAG, "[Protection 3] " + protectionError);
            return false;
        }
    }
    
    /**
     * PROTECTION LAYER 4: Check Root/Jailbreak
     */
    private boolean checkLayer4_Root() {
        try {
            android.util.Log.d(TAG, "[Protection 4] Checking for root/jailbreak...");
            
            if (rootDetection.isDeviceRooted()) {
                protectionError = "Rooted device detected - library only works on unrooted devices";
                android.util.Log.e(TAG, "[Protection 4] " + protectionError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Protection 4] Root Check PASSED ✓");
            return true;
        } catch (Exception e) {
            protectionError = "Root detection error: " + e.getMessage();
            android.util.Log.e(TAG, "[Protection 4] " + protectionError);
            return false;
        }
    }
    
    /**
     * PROTECTION LAYER 5: Check APK Signature
     */
    private boolean checkLayer5_APKSignature(String expectedSignature) {
        try {
            android.util.Log.d(TAG, "[Protection 5] Verifying APK signature...");
            
            // Check if APK is signed first
            if (!apkVerification.isAPKSigned()) {
                protectionError = "APK is not signed";
                android.util.Log.e(TAG, "[Protection 5] " + protectionError);
                return false;
            }
            
            // Check if it's debug signed
            if (apkVerification.isDebugSigned()) {
                protectionError = "APK is debug signed - only release signed APK allowed";
                android.util.Log.e(TAG, "[Protection 5] " + protectionError);
                return false;
            }
            
            // Verify expected signature if provided
            if (expectedSignature != null && !expectedSignature.isEmpty()) {
                if (!apkVerification.verifyAPKSignature(expectedSignature)) {
                    protectionError = "APK signature does not match expected signature";
                    android.util.Log.e(TAG, "[Protection 5] " + protectionError);
                    return false;
                }
            }
            
            android.util.Log.d(TAG, "[Protection 5] APK Signature Verification PASSED ✓");
            return true;
        } catch (Exception e) {
            protectionError = "APK verification error: " + e.getMessage();
            android.util.Log.e(TAG, "[Protection 5] " + protectionError);
            return false;
        }
    }
    
    /**
     * RUN FULL ANTI-REVERSING PROTECTION
     */
    public boolean runFullProtection(String expectedAPKSignature) {
        try {
            android.util.Log.d(TAG, "\n========== ANTI-REVERSING PROTECTION START ==========");
            android.util.Log.d(TAG, emulatorDetection.getDeviceInfo());
            android.util.Log.d(TAG, "");
            
            // Layer 1: Tamper Detection
            if (!checkLayer1_Tamper()) return false;
            
            // Layer 2: Debugger Detection
            if (!checkLayer2_Debugger()) return false;
            
            // Layer 3: Emulator Detection
            if (!checkLayer3_Emulator()) return false;
            
            // Layer 4: Root Detection
            if (!checkLayer4_Root()) return false;
            
            // Layer 5: APK Signature Verification
            if (!checkLayer5_APKSignature(expectedAPKSignature)) return false;
            
            android.util.Log.d(TAG, "");
            android.util.Log.d(TAG, "========== ALL PROTECTIONS PASSED ✓ ==========");
            
            return true;
        } catch (Exception e) {
            protectionError = "Protection error: " + e.getMessage();
            android.util.Log.e(TAG, "========== PROTECTION FAILED ✗ ==========");
            android.util.Log.e(TAG, protectionError);
            return false;
        }
    }
    
    /**
     * Quick protection check (skip APK signature)
     */
    public boolean runQuickProtection() {
        return runFullProtection(null);
    }
    
    // Getters
    public boolean isProtectionEnabled() { return protectionEnabled; }
    public String getProtectionError() { return protectionError; }
    
    public TamperDetection getTamperDetection() { return tamperDetection; }
    public DebuggerDetection getDebuggerDetection() { return debuggerDetection; }
    public EmulatorDetection getEmulatorDetection() { return emulatorDetection; }
    public RootDetection getRootDetection() { return rootDetection; }
    public APKVerification getAPKVerification() { return apkVerification; }
    
    /**
     * Get protection status
     */
    public String getProtectionStatus() {
        return "AntiReversingManager{" +
                "enabled=" + protectionEnabled +
                ", error='" + (protectionError != null ? protectionError : "None") + '\'' +
                ", device='" + emulatorDetection.getDeviceInfo().replace("\n", " | ") + '\'' +
                '}';
    }
}
