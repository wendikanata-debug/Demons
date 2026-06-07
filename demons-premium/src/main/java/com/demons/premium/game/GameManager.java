package com.demons.premium.game;

import android.content.Context;
import com.demons.premium.game.models.GamePlayer;

/**
 * GameManager - Central manager untuk semua game features
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
    }
    
    /**
     * Initialize game manager
     */
    public void initialize(GameCallback callback) {
        try {
            // Initialize Firebase
            // FirebaseApp.initializeApp(context);
            
            // Initialize Billing
            gameBilling.initialize(null);
            
            isInitialized = true;
            android.util.Log.d(TAG, "GameManager initialized");
            
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Initialization failed: " + e.getMessage());
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    // Getters
    public GameAnalytics getGameAnalytics() { return gameAnalytics; }
    public GameBilling getGameBilling() { return gameBilling; }
    public GameLeaderboard getGameLeaderboard() { return gameLeaderboard; }
    public GameAchievement getGameAchievement() { return gameAchievement; }
    public GameMultiplayer getGameMultiplayer() { return gameMultiplayer; }
    public GameAds getGameAds() { return gameAds; }
    
    public GamePlayer getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(GamePlayer player) { this.currentPlayer = player; }
    
    public boolean isInitialized() { return isInitialized; }
    
    /**
     * Shutdown game manager
     */
    public void shutdown() {
        gameAnalytics.clearEventQueue();
        gameMultiplayer.leaveRoom(gameMultiplayer.getCurrentRoom() != null ? 
            gameMultiplayer.getCurrentRoom().getRoomId() : null, null);
        gameAds.hideAllAds();
        isInitialized = false;
        android.util.Log.d(TAG, "GameManager shutdown");
    }
    
    /**
     * Get initialization status
     */
    public String getStatus() {
        return "GameManager{" +
                "initialized=" + isInitialized +
                ", analyticsEnabled=true" +
                ", billingReady=" + gameBilling +
                ", multiplayerConnected=" + gameMultiplayer +
                '}';
    }
    
    // Callback
    public interface GameCallback {
        void onSuccess();
        void onError(String error);
    }
}
