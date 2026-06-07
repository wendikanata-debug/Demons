package com.demons.premium.game.models;

import java.io.Serializable;

/**
 * GameProduct model untuk in-app billing
 */
public class GameProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String productId;
    private String productName;
    private String description;
    private double price;
    private String currency;
    private String type; // "inapp" atau "subscription"
    
    public GameProduct() {}
    
    public GameProduct(String productId) {
        this.productId = productId;
    }
    
    public GameProduct(String productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.type = "inapp";
    }
    
    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    @Override
    public String toString() {
        return "GameProduct{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
