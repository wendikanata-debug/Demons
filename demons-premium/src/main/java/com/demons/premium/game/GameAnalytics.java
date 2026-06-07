package com.demons.premium.game;

import com.demons.premium.game.models.GameEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * GameAnalytics untuk tracking game events dan player actions
 */
public class GameAnalytics {
    private static final String TAG = "GameAnalytics";
    
    private List<GameEvent> eventQueue;
    private boolean isEnabled;
    
    public GameAnalytics() {
        this.eventQueue = new ArrayList<>();
        this.isEnabled = true;
    }
    
    /**
     * Track game event
     */
    public void trackEvent(GameEvent event) {
        if (!isEnabled) return;
        
        try {
            eventQueue.add(event);
            // Send to Firebase Analytics
            sendToAnalytics(event);
            android.util.Log.d(TAG, "Event tracked: " + event.getEventName());
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error tracking event: " + e.getMessage());
        }
    }
    
    /**
     * Track player action
     */
    public void trackPlayerAction(String action, String details) {
        GameEvent event = new GameEvent("player_action");
        event.addParameter("action", action);
        event.addParameter("details", details);
        trackEvent(event);
    }
    
    /**
     * Track level completion
     */
    public void trackLevelCompleted(int level, long score, long timeTaken) {
        GameEvent event = new GameEvent("level_completed");
        event.addParameter("level", level);
        event.addParameter("score", score);
        event.addParameter("time_taken", timeTaken);
        trackEvent(event);
    }
    
    /**
     * Track purchase
     */
    public void trackPurchase(String productId, double price, String currency) {
        GameEvent event = new GameEvent("in_app_purchase");
        event.addParameter("product_id", productId);
        event.addParameter("price", price);
        event.addParameter("currency", currency);
        trackEvent(event);
    }
    
    /**
     * Track multiplayer match
     */
    public void trackMultiplayerMatch(int playersCount, boolean won, long duration) {
        GameEvent event = new GameEvent("multiplayer_match");
        event.addParameter("players", playersCount);
        event.addParameter("won", won);
        event.addParameter("duration", duration);
        trackEvent(event);
    }
    
    /**
     * Send event to Firebase
     */
    private void sendToAnalytics(GameEvent event) {
        // Firebase Analytics integration would go here
        // FirebaseAnalytics.getInstance(context).logEvent(event.getEventName(), bundle);
    }
    
    /**
     * Enable/Disable analytics
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
    
    /**
     * Get pending events
     */
    public List<GameEvent> getPendingEvents() {
        return new ArrayList<>(eventQueue);
    }
    
    /**
     * Clear event queue
     */
    public void clearEventQueue() {
        eventQueue.clear();
    }
}
