package com.demons.premium.game.models;

import java.io.Serializable;

/**
 * GameScore model untuk leaderboard
 */
public class GameScore implements Serializable, Comparable<GameScore> {
    private static final long serialVersionUID = 1L;
    
    private String leaderboardId;
    private String playerId;
    private String playerName;
    private long score;
    private int rank;
    private long timestamp;
    
    public GameScore() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public GameScore(String leaderboardId, long score) {
        this();
        this.leaderboardId = leaderboardId;
        this.score = score;
    }
    
    public GameScore(String leaderboardId, String playerId, String playerName, long score) {
        this();
        this.leaderboardId = leaderboardId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }
    
    // Getters and Setters
    public String getLeaderboardId() { return leaderboardId; }
    public void setLeaderboardId(String leaderboardId) { this.leaderboardId = leaderboardId; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public long getScore() { return score; }
    public void setScore(long score) { this.score = score; }
    
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    @Override
    public int compareTo(GameScore other) {
        return Long.compare(other.score, this.score); // Descending order
    }
    
    @Override
    public String toString() {
        return "GameScore{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }
}
