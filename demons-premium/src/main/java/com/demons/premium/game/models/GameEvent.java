package com.demons.premium.game.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * GameEvent model untuk analytics tracking
 */
public class GameEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String eventName;
    private Map<String, Object> parameters;
    private long timestamp;
    
    public GameEvent() {
        this.parameters = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    public GameEvent(String eventName) {
        this();
        this.eventName = eventName;
    }
    
    public GameEvent(String eventName, Map<String, Object> parameters) {
        this();
        this.eventName = eventName;
        this.parameters = parameters;
    }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    
    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return "GameEvent{" +
                "eventName='" + eventName + '\'' +
                ", parameters=" + parameters +
                ", timestamp=" + timestamp +
                '}';
    }
}
