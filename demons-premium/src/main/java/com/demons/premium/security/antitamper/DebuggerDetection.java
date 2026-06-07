package com.demons.premium.security.antitamper;

import android.content.Context;
import android.os.Debug;
import java.io.File;

/**
 * DebuggerDetection - Deteksi jika app sedang di-debug
 */
public class DebuggerDetection {
    private static final String TAG = "DebuggerDetection";
    
    private Context context;
    
    public DebuggerDetection(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Check if debugger is attached
     */
    public boolean isDebuggerAttached() {
        try {
            boolean isAttached = Debug.isDebuggerConnected();
            
            if (isAttached) {
                android.util.Log.w(TAG, "Debugger attached detected!");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking debugger: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if app is debuggable
     */
    public boolean isDebuggable() {
        try {
            int flags = context.getApplicationInfo().flags;
            boolean debuggable = (flags & android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            
            if (debuggable) {
                android.util.Log.w(TAG, "App is debuggable!");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking if debuggable: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for debugging-related files
     */
    public boolean checkDebugFiles() {
        try {
            android.util.Log.d(TAG, "Checking for debug files...");
            
            // Check for GDB
            if (new File("/system/bin/gdbserver").exists()) {
                android.util.Log.w(TAG, "GDB server found");
                return true;
            }
            
            // Check for Strace
            if (new File("/system/bin/strace").exists()) {
                android.util.Log.w(TAG, "Strace found");
                return true;
            }
            
            // Check for ltrace
            if (new File("/system/bin/ltrace").exists()) {
                android.util.Log.w(TAG, "Ltrace found");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking debug files: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check for debugging ports
     */
    public boolean checkDebugPorts() {
        try {
            android.util.Log.d(TAG, "Checking for debug ports...");
            
            // Check ADB port
            try {
                java.net.Socket socket = new java.net.Socket("localhost", 5037);
                socket.close();
                android.util.Log.w(TAG, "ADB port 5037 is open");
                return true;
            } catch (java.net.ConnectException e) {
                // Expected - port should be closed
            }
            
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking debug ports: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Full debugger check
     */
    public boolean isBeingDebugged() {
        try {
            android.util.Log.d(TAG, "\nChecking if app is being debugged...");
            
            if (isDebuggerAttached()) {
                android.util.Log.w(TAG, "Debugger is attached ⚠️");
                return true;
            }
            
            if (isDebuggable()) {
                android.util.Log.w(TAG, "App is marked as debuggable ⚠️");
                return true;
            }
            
            if (checkDebugFiles()) {
                android.util.Log.w(TAG, "Debug tools found ⚠️");
                return true;
            }
            
            if (checkDebugPorts()) {
                android.util.Log.w(TAG, "Debug ports open ⚠️");
                return true;
            }
            
            android.util.Log.d(TAG, "App is NOT being debugged ✓");
            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error in debugging check: " + e.getMessage());
            return false;
        }
    }
}
