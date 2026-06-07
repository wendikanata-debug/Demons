package com.demons.premium.security.antitamper;

import android.content.Context;
import java.io.File;

/**
 * EmulatorDetection - Deteksi jika app berjalan di emulator
 */
public class EmulatorDetection {
    private static final String TAG = "EmulatorDetection";
    
    private Context context;
    
    public EmulatorDetection(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Check using Build properties
     */
    private boolean checkBuildProperties() {
        try {
            String buildFingerprint = android.os.Build.FINGERPRINT;
            String buildProduct = android.os.Build.PRODUCT;
            String buildModel = android.os.Build.MODEL;
            String buildManufacturer = android.os.Build.MANUFACTURER;
            
            // Check for common emulator signatures
            if (buildFingerprint.contains("generic") || 
                buildFingerprint.contains("unknown") ||
                buildProduct.contains("sdk") ||
                buildProduct.contains("google_sdk") ||
                buildProduct.contains("vbox") ||
                buildModel.contains("emulator") ||
                buildModel.contains("Android SDK") ||
                buildManufacturer.contains("Genymotion")) {
                
                android.util.Log.w(TAG, "Emulator detected via Build properties");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking build properties: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check using system files
     */
    private boolean checkSystemFiles() {
        try {
            // Files yang ada di emulator tapi tidak ada di real device
            String[] emulatorFiles = {
                "/system/lib/libc_malloc_debug_leak.so",
                "/system/lib64/libc_malloc_debug_leak.so",
                "/system/lib/libc_malloc_debug_qemu.so",
                "/system/lib64/libc_malloc_debug_qemu.so",
                "/system/bin/qemu-props",
                "/dev/qemu_pipe",
                "/dev/socket/qemud",
                "/.android/avd/hardware.ini"
            };
            
            for (String file : emulatorFiles) {
                if (new File(file).exists()) {
                    android.util.Log.w(TAG, "Emulator file found: " + file);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking system files: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check using hardware properties
     */
    private boolean checkHardwareProperties() {
        try {
            // Check for emulator-specific features
            String hardware = android.os.Build.HARDWARE;
            String device = android.os.Build.DEVICE;
            String serial = android.os.Build.SERIAL;
            
            if (hardware.contains("ranchu") ||
                hardware.contains("goldfish") ||
                device.contains("generic") ||
                device.contains("emulator") ||
                serial.equals("unknown") ||
                serial.equals("android")) {
                
                android.util.Log.w(TAG, "Emulator detected via hardware properties");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking hardware: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check using telephony properties
     */
    private boolean checkTelephonyProperties() {
        try {
            String operatorName = android.telephony.TelephonyManager.getDefault().getNetworkOperatorName();
            String operatorCode = android.telephony.TelephonyManager.getDefault().getNetworkOperator();
            
            // Emulator usually has empty or default values
            if (operatorName == null || operatorName.isEmpty() ||
                operatorCode == null || operatorCode.isEmpty()) {
                
                android.util.Log.w(TAG, "Emulator detected via telephony properties");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking telephony: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check using environment variables
     */
    private boolean checkEnvironmentVariables() {
        try {
            String qemuVersion = System.getProperty("ro.kernel.qemu");
            String genyVersion = System.getProperty("ro.genymotion.version");
            
            if (qemuVersion != null || genyVersion != null) {
                android.util.Log.w(TAG, "Emulator detected via environment variables");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking environment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Full emulator check
     */
    public boolean isRunningOnEmulator() {
        try {
            android.util.Log.d(TAG, "\nChecking if running on emulator...");
            
            if (checkBuildProperties()) return true;
            if (checkSystemFiles()) return true;
            if (checkHardwareProperties()) return true;
            if (checkTelephonyProperties()) return true;
            if (checkEnvironmentVariables()) return true;
            
            android.util.Log.d(TAG, "Running on real device ✓");
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking emulator: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get device info
     */
    public String getDeviceInfo() {
        return "Device: " + android.os.Build.DEVICE +
               "\nModel: " + android.os.Build.MODEL +
               "\nManufacturer: " + android.os.Build.MANUFACTURER +
               "\nProduct: " + android.os.Build.PRODUCT +
               "\nHardware: " + android.os.Build.HARDWARE;
    }
}
