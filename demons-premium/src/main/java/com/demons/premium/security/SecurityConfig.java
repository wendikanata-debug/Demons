package com.demons.premium.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * SecurityConfig - Konfigurasi security untuk library
 */
public class SecurityConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String licenseKey;
    private String apiKey;
    private String serverUrl;
    private String packageName;
    private String deviceId;
    private long licenseExpiryDate;
    private boolean enableServerValidation;
    private boolean enablePackageValidation;
    private boolean enableDeviceBinding;
    private Map<String, String> additionalConfig;
    
    public SecurityConfig() {
        this.additionalConfig = new HashMap<>();
        this.enableServerValidation = true;
        this.enablePackageValidation = true;
        this.enableDeviceBinding = false;
    }
    
    // Getters and Setters
    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }
    
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    
    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public long getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(long licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }
    
    public boolean isEnableServerValidation() { return enableServerValidation; }
    public void setEnableServerValidation(boolean enableServerValidation) { 
        this.enableServerValidation = enableServerValidation; 
    }
    
    public boolean isEnablePackageValidation() { return enablePackageValidation; }
    public void setEnablePackageValidation(boolean enablePackageValidation) { 
        this.enablePackageValidation = enablePackageValidation; 
    }
    
    public boolean isEnableDeviceBinding() { return enableDeviceBinding; }
    public void setEnableDeviceBinding(boolean enableDeviceBinding) { 
        this.enableDeviceBinding = enableDeviceBinding; 
    }
    
    public Map<String, String> getAdditionalConfig() { return additionalConfig; }
    public void setAdditionalConfig(Map<String, String> additionalConfig) { 
        this.additionalConfig = additionalConfig; 
    }
    
    public void addConfig(String key, String value) {
        this.additionalConfig.put(key, value);
    }
    
    @Override
    public String toString() {
        return "SecurityConfig{" +
                "licenseKey='" + (licenseKey != null ? "***" : "null") + '\'' +
                ", apiKey='" + (apiKey != null ? "***" : "null") + '\'' +
                ", packageName='" + packageName + '\'' +
                ", serverValidation=" + enableServerValidation +
                ", packageValidation=" + enablePackageValidation +
                ", deviceBinding=" + enableDeviceBinding +
                '}';
    }
}
