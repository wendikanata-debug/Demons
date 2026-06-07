package com.demons.premium.game;

import android.content.Context;
import com.demons.premium.game.models.GamePlayer;
import com.demons.premium.security.SecurityManager;
import com.demons.premium.security.SecurityConfig;

/**
 * GameManager - Updated dengan Security Integration
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
        initializeComponents();
    }
    
    /**
     * Initialize all game components
     */
    private void initializeComponents() {
        this.gameAnalytics = new GameAnalytics();
        this.gameBilling = new GameBilling();
        this.gameLeaderboard = new GameLeaderboard();
        this.gameAchievement = new GameAchievement();
        this.gameMultiplayer = new GameMultiplayer();
        this.gameAds = new GameAds();
        this.securityManager = SecurityManager.getInstance(context);
    }
    
    /**
     * Initialize GameManager dengan Security
     */
    public void initialize(SecurityConfig securityConfig, GameCallback callback) {
        try {
            // Initialize security first
            securityManager.initialize(securityConfig);
            
            android.util.Log.d(TAG, "Running security validation...");
            
            // Validate security
            if (!securityManager.validate()) {
                String error = "Security validation failed: " + securityManager.getSecurityError();
                android.util.Log.e(TAG, error);
                if (callback != null) callback.onError(error);
                return;
            }
            
            securityValidated = true;
            android.util.Log.d(TAG, "Security validation passed!");
            
            // Initialize game features
            gameBilling.initialize(null);
            
            isInitialized = true;
            android.util.Log.d(TAG, "GameManager initialized successfully");
            
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            String error = "Initialization failed: " + e.getMessage();
            android.util.Log.e(TAG, error);
            if (callback != null) callback.onError(error);
        }
    }
    
    /**
     * Check if security is validated
     */
    public boolean isSecurityValidated() {
        return securityValidated;
    }
    
    /**
     * Get security manager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }
    
    /**
     * Get security status
     */
    public String getSecurityStatus() {
        return securityManager.getSecurityStatus();
    }
    
    // Getters for game features
    public GameAnalytics getGameAnalytics() { 
        checkSecurity();
        return gameAnalytics; 
    }
    
    public GameBilling getGameBilling() { 
        checkSecurity();
        return gameBilling; 
    }
    
    public GameLeaderboard getGameLeaderboard() { 
        checkSecurity();
        return gameLeaderboard; 
    }
    
    public GameAchievement getGameAchievement() { 
        checkSecurity();
        return gameAchievement; 
    }
    
    public GameMultiplayer getGameMultiplayer() { 
        checkSecurity();
        return gameMultiplayer; 
    }
    
    public GameAds getGameAds() { 
        checkSecurity();
        return gameAds; 
    }
    
    public GamePlayer getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(GamePlayer player) { this.currentPlayer = player; }
    
    public boolean isInitialized() { return isInitialized; }
    
    /**
     * Check if security is valid sebelum akses features
     */
    private void checkSecurity() {
        if (!securityValidated) {
            throw new SecurityException("Security validation required! \nError: " + 
                securityManager.getSecurityError());
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
        android.util.Log.d(TAG, "GameManager shutdown");
    }
    
    /**
     * Get full status
     */
    public String getStatus() {
        return "GameManager{" +
                "initialized=" + isInitialized +
                ", securityValidated=" + securityValidated +
                ", securityStatus='" + securityManager.getSecurityError() + '\'' +
                '}';
    }
    
    // Callback
    public interface GameCallback {
        void onSuccess();
        void onError(String error);
    }
}
