package com.demons.premium.game;

import android.content.Context;
import com.demons.premium.game.models.GamePlayer;
import com.demons.premium.security.SecurityManager;
import com.demons.premium.security.SecurityConfig;
import com.demons.premium.security.antitamper.AntiReversingManager;

/**
 * GameManager - Final Version dengan Security + Anti-Reversing
 */
public class GameManager {
    private static final String TAG = "GameManager";
    private static GameManager instance;
    
    private Context context;
    private GameAnalytics gameAnalytics;
    private GameBilling gameBilling;
    private GameLeaderboard gameLeaderboard;
    private GameAchievement gameAchievement;
    private GameMultiplayer gameMultiplayer;
    private GameAds gameAds;
    
    private GamePlayer currentPlayer;
    private boolean isInitialized;
    
    // Security
    private SecurityManager securityManager;
    private boolean securityValidated;
    
    // Anti-Reversing
    private AntiReversingManager antiReversingManager;
    private boolean antiReversingValidated;
    
    /**
     * Get singleton instance
     */
    public static synchronized GameManager getInstance(Context context) {
        if (instance == null) {
            instance = new GameManager(context);
        }
        return instance;
    }
    
    /**
     * Private constructor
     */
    private GameManager(Context context) {
        this.context = context.getApplicationContext();
        this.isInitialized = false;
        this.securityValidated = false;
        this.antiReversingValidated = false;
        initializeComponents();
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        this.gameAnalytics = new GameAnalytics();
        this.gameBilling = new GameBilling();
        this.gameLeaderboard = new GameLeaderboard();
        this.gameAchievement = new GameAchievement();
        this.gameMultiplayer = new GameMultiplayer();
        this.gameAds = new GameAds();
        this.securityManager = SecurityManager.getInstance(context);
        this.antiReversingManager = AntiReversingManager.getInstance(context);
    }
    
    /**
     * Initialize GameManager dengan Security + Anti-Reversing
     */
    public void initialize(SecurityConfig securityConfig, String expectedAPKSignature, GameCallback callback) {
        try {
            android.util.Log.d(TAG, "\n==================== INITIALIZATION START ====================");
            
            // Step 1: Validate Security
            android.util.Log.d(TAG, "\nStep 1: Security Validation");
            securityManager.initialize(securityConfig);
            
            if (!securityManager.validate()) {
                String error = "Security validation failed: " + securityManager.getSecurityError();
                android.util.Log.e(TAG, error);
                if (callback != null) callback.onError(error);
                return;
            }
            
            securityValidated = true;
            android.util.Log.d(TAG, "✓ Security validation passed!");
            
            // Step 2: Validate Anti-Reversing
            android.util.Log.d(TAG, "\nStep 2: Anti-Reversing Protection");
            
            if (!antiReversingManager.runFullProtection(expectedAPKSignature)) {
                String error = "Anti-reversing protection failed: " + antiReversingManager.getProtectionError();
                android.util.Log.e(TAG, error);
                if (callback != null) callback.onError(error);
                return;
            }
            
            antiReversingValidated = true;
            android.util.Log.d(TAG, "✓ Anti-reversing protection passed!");
            
            // Step 3: Initialize game features
            android.util.Log.d(TAG, "\nStep 3: Initializing Game Features");
            gameBilling.initialize(null);
            
            isInitialized = true;
            android.util.Log.d(TAG, "\n✓ GameManager initialized successfully!");
            android.util.Log.d(TAG, "==================== INITIALIZATION COMPLETE ====================");
            
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            String error = "Initialization failed: " + e.getMessage();
            android.util.Log.e(TAG, error);
            if (callback != null) callback.onError(error);
        }
    }
    
    /**
     * Check if security and protection are validated
     */
    public boolean isSecurityValidated() {
        return securityValidated;
    }
    
    public boolean isAntiReversingValidated() {
        return antiReversingValidated;
    }
    
    /**
     * Get security manager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }
    
    /**
     * Get anti-reversing manager
     */
    public AntiReversingManager getAntiReversingManager() {
        return antiReversingManager;
    }
    
    /**
     * Get security status
     */
    public String getSecurityStatus() {
        return securityManager.getSecurityStatus();
    }
    
    /**
     * Get protection status
     */
    public String getProtectionStatus() {
        return antiReversingManager.getProtectionStatus();
    }
    
    // Getters for game features
    public GameAnalytics getGameAnalytics() { 
        checkValidation();
        return gameAnalytics; 
    }
    
    public GameBilling getGameBilling() { 
        checkValidation();
        return gameBilling; 
    }
    
    public GameLeaderboard getGameLeaderboard() { 
        checkValidation();
        return gameLeaderboard; 
    }
    
    public GameAchievement getGameAchievement() { 
        checkValidation();
        return gameAchievement; 
    }
    
    public GameMultiplayer getGameMultiplayer() { 
        checkValidation();
        return gameMultiplayer; 
    }
    
    public GameAds getGameAds() { 
        checkValidation();
        return gameAds; 
    }
    
    public GamePlayer getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(GamePlayer player) { this.currentPlayer = player; }
    
    public boolean isInitialized() { return isInitialized; }
    
    /**
     * Check if all validations passed
     */
    private void checkValidation() {
        if (!securityValidated) {
            throw new SecurityException("Security validation required! \nError: " + 
                securityManager.getSecurityError());
        }
        
        if (!antiReversingValidated) {
            throw new SecurityException("Anti-reversing protection required! \nError: " + 
                antiReversingManager.getProtectionError());
        }
    }
    
    /**
     * Shutdown game manager
     */
    public void shutdown() {
        gameAnalytics.clearEventQueue();
        gameMultiplayer.leaveRoom(gameMultiplayer.getCurrentRoom() != null ? 
            gameMultiplayer.getCurrentRoom().getRoomId() : null, null);
        gameAds.hideAllAds();
        isInitialized = false;
        securityValidated = false;
        antiReversingValidated = false;
        android.util.Log.d(TAG, "GameManager shutdown");
    }
    
    /**
     * Get full status
     */
    public String getStatus() {
        return "GameManager{" +
                "initialized=" + isInitialized +
                ", securityValidated=" + securityValidated +
                ", antiReversingValidated=" + antiReversingValidated +
                ", security='" + securityManager.getSecurityError() + '\'' +
                ", protection='" + antiReversingManager.getProtectionError() + '\'' +
                '}';
    }
    
    // Callback
    public interface GameCallback {
        void onSuccess();
        void onError(String error);
    }
}
