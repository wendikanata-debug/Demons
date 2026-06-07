package com.demons.premium.game.models;

import java.io.Serializable;

/**
 * GamePlayer model untuk menyimpan informasi game player
 */
public class GamePlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String playerId;
    private String playerName;
    private String playerEmail;
    private int level;
    private long totalScore;
    private int gamesPlayed;
    private int gamesWon;
    private String country;
    private String deviceId;
    private long joinedAt;
    private long lastPlayedAt;
    
    public GamePlayer() {
        this.joinedAt = System.currentTimeMillis();
        this.lastPlayedAt = System.currentTimeMillis();
    }
    
    public GamePlayer(String playerId, String playerName) {
        this();
        this.playerId = playerId;
        this.playerName = playerName;
    }
    
    // Getters and Setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public String getPlayerEmail() { return playerEmail; }
    public void setPlayerEmail(String playerEmail) { this.playerEmail = playerEmail; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public long getTotalScore() { return totalScore; }
    public void setTotalScore(long totalScore) { this.totalScore = totalScore; }
    
    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    
    public int getGamesWon() { return gamesWon; }
    public void setGamesWon(int gamesWon) { this.gamesWon = gamesWon; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public long getJoinedAt() { return joinedAt; }
    public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
    
    public long getLastPlayedAt() { return lastPlayedAt; }
    public void setLastPlayedAt(long lastPlayedAt) { this.lastPlayedAt = lastPlayedAt; }
    
    @Override
    public String toString() {
        return "GamePlayer{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", level=" + level +
                ", totalScore=" + totalScore +
                ", gamesWon=" + gamesWon +
                '}';
    }
}
