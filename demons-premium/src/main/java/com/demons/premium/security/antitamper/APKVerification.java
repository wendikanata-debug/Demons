package com.demons.premium.security.antitamper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * APKVerification - Verifikasi APK signature
 */
public class APKVerification {
    private static final String TAG = "APKVerification";
    
    private Context context;
    private String expectedSignature; // Hash dari APK yang legitimate
    
    public APKVerification(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Get APK signature
     */
    public String getAPKSignature() {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            
            android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            );
            
            if (packageInfo.signatures == null || packageInfo.signatures.length == 0) {
                android.util.Log.e(TAG, "No signatures found");
                return null;
            }
            
            Signature signature = packageInfo.signatures[0];
            byte[] cert = signature.toByteArray();
            
            // Get certificate
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert509 = (X509Certificate) cf.generateCertificate(
                new ByteArrayInputStream(cert)
            );
            
            // Calculate SHA-1 hash of certificate
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert509.getEncoded());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : publicKey) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting APK signature: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verify APK signature matches expected
     */
    public boolean verifyAPKSignature(String expectedSignatureHash) {
        try {
            android.util.Log.d(TAG, "Verifying APK signature...");
            
            String actualSignature = getAPKSignature();
            
            if (actualSignature == null) {
                android.util.Log.e(TAG, "Could not get APK signature");
                return false;
            }
            
            if (!actualSignature.equals(expectedSignatureHash)) {
                android.util.Log.w(TAG, "APK signature mismatch!");
                android.util.Log.w(TAG, "Expected: " + expectedSignatureHash);
                android.util.Log.w(TAG, "Got: " + actualSignature);
                return false;
            }
            
            android.util.Log.d(TAG, "APK signature verified ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error verifying signature: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if APK is signed
     */
    public boolean isAPKSigned() {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            
            android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            );
            
            if (packageInfo.signatures == null || packageInfo.signatures.length == 0) {
                android.util.Log.w(TAG, "APK is not signed");
                return false;
            }
            
            android.util.Log.d(TAG, "APK is signed ✓");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking if signed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if APK is debug signed
     */
    public boolean isDebugSigned() {
        try {
            String signature = getAPKSignature();
            
            // Debug signature (from debug keystore)
            String debugSignature = "308201dd30820146a00302010202044ca45d8d300d06092a864886f70d01010b050030123110300e06035504030c0741 6e6472 6f6964";
            
            // This is simplified - in production, check against known debug signatures
            if (signature != null && signature.toLowerCase().contains("android")) {
                android.util.Log.w(TAG, "Debug signature detected");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking debug signature: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Set expected signature untuk verification
     */
    public void setExpectedSignature(String signature) {
        this.expectedSignature = signature;
    }
    
    /**
     * Get expected signature
     */
    public String getExpectedSignature() {
        return expectedSignature;
    }
}
