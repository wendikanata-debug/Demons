package com.demons.premium.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameAchievement untuk handle achievements
 */
public class GameAchievement {
    private static final String TAG = "GameAchievement";
    
    private Map<String, Boolean> achievements;
    private List<String> unlockedAchievements;
    private boolean isInitialized;
    
    public GameAchievement() {
        this.achievements = new HashMap<>();
        this.unlockedAchievements = new ArrayList<>();
        this.isInitialized = true;
        initializeAchievements();
    }
    
    /**
     * Initialize default achievements
     */
    private void initializeAchievements() {
        achievements.put("first_win", false);
        achievements.put("10_wins", false);
        achievements.put("100_wins", false);
        achievements.put("headshot_master", false);
        achievements.put("sniper_expert", false);
        achievements.put("survival_king", false);
    }
    
    /**
     * Unlock achievement
     */
    public void unlock(String achievementId, AchievementCallback callback) {
        try {
            if (!achievements.containsKey(achievementId)) {
                if (callback != null) callback.onError("Achievement not found");
                return;
            }
            
            if (!achievements.get(achievementId)) {
                achievements.put(achievementId, true);
                unlockedAchievements.add(achievementId);
                
                // Report to Google Play Games
                android.util.Log.d(TAG, "Achievement unlocked: " + achievementId);
                if (callback != null) callback.onSuccess();
            } else {
                if (callback != null) callback.onError("Achievement already unlocked");
            }
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Check if achievement is unlocked
     */
    public boolean isUnlocked(String achievementId) {
        return achievements.getOrDefault(achievementId, false);
    }
    
    /**
     * Get all achievements
     */
    public void getAchievements(AchievementsCallback callback) {
        try {
            List<String> allAchievements = new ArrayList<>(achievements.keySet());
            if (callback != null) callback.onAchievementsRetrieved(allAchievements);
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Get unlocked achievements
     */
    public List<String> getUnlockedAchievements() {
        return new ArrayList<>(unlockedAchievements);
    }
    
    /**
     * Get achievement progress
     */
    public float getProgress() {
        if (achievements.isEmpty()) return 0;
        return (float) unlockedAchievements.size() / achievements.size() * 100;
    }
    
    // Callbacks
    public interface AchievementCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface AchievementsCallback {
        void onAchievementsRetrieved(List<String> achievements);
        void onError(String error);
    }
}
