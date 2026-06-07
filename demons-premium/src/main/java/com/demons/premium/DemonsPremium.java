package com.demons.premium;

/**
 * DemonsPremium - Main class untuk core functionality
 * Supported: Android 10 (API 29) - Android 16 (API 35)
 * Architecture: 64-bit only (arm64-v8a)
 */
public class DemonsPremium {
    
    private static final String TAG = "DemonsPremium";
    private static DemonsPremium instance;
    private final java.util.Map<String, Object> data = new java.util.HashMap<>();
    
    /**
     * Get singleton instance
     */
    public static synchronized DemonsPremium getInstance() {
        if (instance == null) {
            instance = new DemonsPremium();
        }
        return instance;
    }
    
    /**
     * Private constructor untuk singleton pattern
     */
    private DemonsPremium() {
        // Initialize dengan system information
        int osVersion = android.os.Build.VERSION.SDK_INT;
        String cpuAbi = android.os.Build.CPU_ABI;
    }
    
    /**
     * Simpan data
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Ambil data
     */
    public Object get(String key) {
        return data.get(key);
    }
    
    /**
     * Hapus data
     */
    public Object remove(String key) {
        return data.remove(key);
    }
    
    /**
     * Clear semua data
     */
    public void clear() {
        data.clear();
    }
    
    /**
     * Cek apakah key ada
     */
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }
    
    /**
     * Get Android OS Version
     */
    public int getOsVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
    
    /**
     * Get CPU ABI
     */
    public String getCpuAbi() {
        return android.os.Build.CPU_ABI;
    }
    
    /**
     * Get data size
     */
    public int getDataSize() {
        return data.size();
    }
}
