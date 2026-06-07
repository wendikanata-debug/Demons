package com.demons.premium.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameMultiplayer untuk handle real-time multiplayer dan room management
 */
public class GameMultiplayer {
    private static final String TAG = "GameMultiplayer";
    
    private Map<String, GameRoom> rooms;
    private String currentRoomId;
    private boolean isConnected;
    
    public GameMultiplayer() {
        this.rooms = new HashMap<>();
        this.isConnected = false;
    }
    
    /**
     * Create game room
     */
    public void createRoom(int minPlayers, int maxPlayers, RoomCallback callback) {
        try {
            String roomId = "room_" + System.currentTimeMillis();
            GameRoom room = new GameRoom(roomId, minPlayers, maxPlayers);
            rooms.put(roomId, room);
            currentRoomId = roomId;
            
            android.util.Log.d(TAG, "Room created: " + roomId);
            if (callback != null) callback.onRoomCreated(roomId);
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Join room
     */
    public void joinRoom(String roomId, JoinCallback callback) {
        try {
            if (!rooms.containsKey(roomId)) {
                if (callback != null) callback.onError("Room not found");
                return;
            }
            
            GameRoom room = rooms.get(roomId);
            if (room.getPlayerCount() >= room.getMaxPlayers()) {
                if (callback != null) callback.onError("Room is full");
                return;
            }
            
            room.addPlayer();
            currentRoomId = roomId;
            
            android.util.Log.d(TAG, "Joined room: " + roomId);
            if (callback != null) callback.onRoomJoined(roomId);
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Send message to room
     */
    public void sendMessage(String roomId, GameMessage message) {
        try {
            if (!rooms.containsKey(roomId)) {
                android.util.Log.e(TAG, "Room not found: " + roomId);
                return;
            }
            
            // Firebase Realtime Database would handle this
            android.util.Log.d(TAG, "Message sent to room: " + roomId);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error sending message: " + e.getMessage());
        }
    }
    
    /**
     * Leave room
     */
    public void leaveRoom(String roomId, LeaveCallback callback) {
        try {
            if (rooms.containsKey(roomId)) {
                GameRoom room = rooms.get(roomId);
                room.removePlayer();
                
                if (room.getPlayerCount() == 0) {
                    rooms.remove(roomId);
                }
            }
            currentRoomId = null;
            
            if (callback != null) callback.onRoomLeft();
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Get room info
     */
    public GameRoom getRoomInfo(String roomId) {
        return rooms.get(roomId);
    }
    
    /**
     * Get current room
     */
    public GameRoom getCurrentRoom() {
        if (currentRoomId == null) return null;
        return rooms.get(currentRoomId);
    }
    
    /**
     * Broadcast message to all rooms
     */
    public void broadcastMessage(GameMessage message) {
        for (GameRoom room : rooms.values()) {
            android.util.Log.d(TAG, "Broadcast to room: " + room.getRoomId());
        }
    }
    
    // Inner class for GameRoom
    public static class GameRoom {
        private String roomId;
        private int playerCount;
        private int maxPlayers;
        private int minPlayers;
        private long createdAt;
        
        public GameRoom(String roomId, int minPlayers, int maxPlayers) {
            this.roomId = roomId;
            this.minPlayers = minPlayers;
            this.maxPlayers = maxPlayers;
            this.playerCount = 1;
            this.createdAt = System.currentTimeMillis();
        }
        
        public void addPlayer() { this.playerCount++; }
        public void removePlayer() { this.playerCount--; }
        
        public String getRoomId() { return roomId; }
        public int getPlayerCount() { return playerCount; }
        public int getMaxPlayers() { return maxPlayers; }
        public int getMinPlayers() { return minPlayers; }
        public long getCreatedAt() { return createdAt; }
    }
    
    // GameMessage inner class
    public static class GameMessage {
        private String type;
        private String data;
        private Object payload;
        
        public GameMessage(String type, String data, Object payload) {
            this.type = type;
            this.data = data;
            this.payload = payload;
        }
        
        public String getType() { return type; }
        public String getData() { return data; }
        public Object getPayload() { return payload; }
    }
    
    // Callbacks
    public interface RoomCallback {
        void onRoomCreated(String roomId);
        void onError(String error);
    }
    
    public interface JoinCallback {
        void onRoomJoined(String roomId);
        void onError(String error);
    }
    
    public interface LeaveCallback {
        void onRoomLeft();
        void onError(String error);
    }
}
