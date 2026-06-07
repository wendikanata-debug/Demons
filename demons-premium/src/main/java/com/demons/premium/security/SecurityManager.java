package com.demons.premium.security;

import android.content.Context;

/**
 * SecurityManager - Central security manager yang mengintegrasikan semua validators
 * HYBRID APPROACH: License + Server + Package + Device
 */
public class SecurityManager {
    private static final String TAG = "SecurityManager";
    private static SecurityManager instance;
    
    private Context context;
    private SecurityConfig config;
    
    private LicenseValidator licenseValidator;
    private ServerValidator serverValidator;
    private PackageValidator packageValidator;
    private DeviceValidator deviceValidator;
    
    private boolean isSecurityPassed;
    private String securityError;
    
    /**
     * Get singleton instance
     */
    public static synchronized SecurityManager getInstance(Context context) {
        if (instance == null) {
            instance = new SecurityManager(context);
        }
        return instance;
    }
    
    /**
     * Private constructor
     */
    private SecurityManager(Context context) {
        this.context = context.getApplicationContext();
        this.config = new SecurityConfig();
        this.isSecurityPassed = false;
        this.securityError = "Not initialized";
    }
    
    /**
     * Initialize security dengan configuration
     */
    public void initialize(SecurityConfig securityConfig) {
        try {
            this.config = securityConfig;
            
            // Initialize semua validators
            this.licenseValidator = new LicenseValidator(context, config);
            this.serverValidator = new ServerValidator(context, config);
            this.packageValidator = new PackageValidator(context, config);
            this.deviceValidator = new DeviceValidator(context, config);
            
            // Set device ID di config
            config.setDeviceId(deviceValidator.getDeviceId());
            
            android.util.Log.d(TAG, "SecurityManager initialized");
        } catch (Exception e) {
            this.securityError = "Initialization error: " + e.getMessage();
            android.util.Log.e(TAG, securityError);
        }
    }
    
    /**
     * LAYER 1: Validate License Key
     */
    private boolean validateLayer1_LicenseKey() {
        try {
            android.util.Log.d(TAG, "[Layer 1] Validating License Key...");
            
            if (!config.isEnableServerValidation()) {
                android.util.Log.w(TAG, "[Layer 1] License validation disabled");
                return true;
            }
            
            boolean isValid = licenseValidator.validateLicense();
            
            if (!isValid) {
                securityError = "Invalid or expired license key";
                android.util.Log.e(TAG, "[Layer 1] " + securityError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Layer 1] License Key VALID ✓");
            return true;
        } catch (Exception e) {
            securityError = "License validation error: " + e.getMessage();
            android.util.Log.e(TAG, "[Layer 1] " + securityError);
            return false;
        }
    }
    
    /**
     * LAYER 2: Validate dengan Server
     */
    private boolean validateLayer2_Server() {
        try {
            android.util.Log.d(TAG, "[Layer 2] Validating with Server...");
            
            if (!config.isEnableServerValidation()) {
                android.util.Log.w(TAG, "[Layer 2] Server validation disabled");
                return true;
            }
            
            // Check server availability
            if (!serverValidator.isServerAvailable()) {
                android.util.Log.w(TAG, "[Layer 2] Server not available, using offline validation");
                return true; // Allow offline mode
            }
            
            boolean isValid = serverValidator.validateLicenseWithServer();
            
            if (!isValid) {
                securityError = "Server validation failed";
                android.util.Log.e(TAG, "[Layer 2] " + securityError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Layer 2] Server Validation PASSED ✓");
            return true;
        } catch (Exception e) {
            securityError = "Server validation error: " + e.getMessage();
            android.util.Log.e(TAG, "[Layer 2] " + securityError);
            return false;
        }
    }
    
    /**
     * LAYER 3: Validate Package Name
     */
    private boolean validateLayer3_Package() {
        try {
            android.util.Log.d(TAG, "[Layer 3] Validating Package Name...");
            
            if (!config.isEnablePackageValidation()) {
                android.util.Log.w(TAG, "[Layer 3] Package validation disabled");
                return true;
            }
            
            boolean isValid = packageValidator.validatePackage();
            
            if (!isValid) {
                securityError = "Package not whitelisted: " + packageValidator.getCurrentPackageName();
                android.util.Log.e(TAG, "[Layer 3] " + securityError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Layer 3] Package Validation PASSED ✓");
            return true;
        } catch (Exception e) {
            securityError = "Package validation error: " + e.getMessage();
            android.util.Log.e(TAG, "[Layer 3] " + securityError);
            return false;
        }
    }
    
    /**
     * LAYER 4: Validate Device Binding
     */
    private boolean validateLayer4_Device() {
        try {
            android.util.Log.d(TAG, "[Layer 4] Validating Device Binding...");
            
            if (!config.isEnableDeviceBinding()) {
                android.util.Log.w(TAG, "[Layer 4] Device binding disabled");
                return true;
            }
            
            boolean isValid = deviceValidator.validateDeviceBinding();
            
            if (!isValid) {
                securityError = "Device binding failed or device mismatch detected";
                android.util.Log.e(TAG, "[Layer 4] " + securityError);
                return false;
            }
            
            android.util.Log.d(TAG, "[Layer 4] Device Binding VALIDATED ✓");
            return true;
        } catch (Exception e) {
            securityError = "Device validation error: " + e.getMessage();
            android.util.Log.e(TAG, "[Layer 4] " + securityError);
            return false;
        }
    }
    
    /**
     * RUN HYBRID APPROACH - Validate ALL layers
     */
    public boolean validate() {
        try {
            android.util.Log.d(TAG, "\n========== HYBRID SECURITY VALIDATION START ==========");
            android.util.Log.d(TAG, "Device: " + deviceValidator.getDeviceInfo());
            android.util.Log.d(TAG, "Package: " + packageValidator.getCurrentPackageName());
            android.util.Log.d(TAG, "");
            
            // Layer 1: License
            if (!validateLayer1_LicenseKey()) return false;
            
            // Layer 2: Server
            if (!validateLayer2_Server()) return false;
            
            // Layer 3: Package
            if (!validateLayer3_Package()) return false;
            
            // Layer 4: Device
            if (!validateLayer4_Device()) return false;
            
            android.util.Log.d(TAG, "");
            android.util.Log.d(TAG, "========== ALL SECURITY LAYERS PASSED ✓ ==========");
            
            isSecurityPassed = true;
            return true;
        } catch (Exception e) {
            securityError = "Validation error: " + e.getMessage();
            android.util.Log.e(TAG, "========== SECURITY VALIDATION FAILED ✗ ==========");
            android.util.Log.e(TAG, securityError);
            isSecurityPassed = false;
            return false;
        }
    }
    
    // Getters
    public boolean isSecurityPassed() { return isSecurityPassed; }
    public String getSecurityError() { return securityError; }
    public SecurityConfig getConfig() { return config; }
    
    public LicenseValidator getLicenseValidator() { return licenseValidator; }
    public ServerValidator getServerValidator() { return serverValidator; }
    public PackageValidator getPackageValidator() { return packageValidator; }
    public DeviceValidator getDeviceValidator() { return deviceValidator; }
    
    /**
     * Get security status info
     */
    public String getSecurityStatus() {
        return "SecurityManager{" +
                "passed=" + isSecurityPassed +
                ", error='" + securityError + '\'' +
                ", device='" + deviceValidator.getDeviceInfo() + '\'' +
                ", package='" + packageValidator.getCurrentPackageName() + '\'' +
                '}';
    }
}
