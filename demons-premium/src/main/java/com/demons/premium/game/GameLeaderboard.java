package com.demons.premium.game;

import com.demons.premium.game.models.GameScore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GameLeaderboard untuk handle leaderboards dan rankings
 */
public class GameLeaderboard {
    private static final String TAG = "GameLeaderboard";
    
    private List<GameScore> leaderboards;
    private boolean isInitialized;
    
    public GameLeaderboard() {
        this.leaderboards = new ArrayList<>();
        this.isInitialized = true;
    }
    
    /**
     * Submit score
     */
    public void submitScore(GameScore score, LeaderboardCallback callback) {
        try {
            leaderboards.add(score);
            Collections.sort(leaderboards);
            
            // Update ranks
            for (int i = 0; i < leaderboards.size(); i++) {
                leaderboards.get(i).setRank(i + 1);
            }
            
            // Submit to Google Play Games
            android.util.Log.d(TAG, "Score submitted: " + score.getScore());
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Submit score failed: " + e.getMessage());
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Get top scores
     */
    public void getTopScores(String leaderboardId, int limit, ScoresCallback callback) {
        try {
            List<GameScore> topScores = new ArrayList<>(leaderboards);
            if (topScores.size() > limit) {
                topScores = topScores.subList(0, limit);
            }
            
            if (callback != null) callback.onScoresRetrieved(topScores);
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Get player rank
     */
    public void getPlayerRank(String leaderboardId, String playerId, RankCallback callback) {
        try {
            for (GameScore score : leaderboards) {
                if (score.getPlayerId().equals(playerId)) {
                    if (callback != null) callback.onRankRetrieved(score.getRank());
                    return;
                }
            }
            if (callback != null) callback.onError("Player not found");
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Get player score
     */
    public GameScore getPlayerScore(String playerId) {
        for (GameScore score : leaderboards) {
            if (score.getPlayerId().equals(playerId)) {
                return score;
            }
        }
        return null;
    }
    
    // Callbacks
    public interface LeaderboardCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface ScoresCallback {
        void onScoresRetrieved(List<GameScore> scores);
        void onError(String error);
    }
    
    public interface RankCallback {
        void onRankRetrieved(int rank);
        void onError(String error);
    }
}
