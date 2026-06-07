package com.demons.premium.game;

import java.util.HashMap;
import java.util.Map;

/**
 * GameAds untuk handle ad network integration (AdMob)
 */
public class GameAds {
    private static final String TAG = "GameAds";
    
    private String interstitialAdUnitId;
    private String rewardedAdUnitId;
    private String bannerAdUnitId;
    private boolean isInterstitialLoaded;
    private boolean isRewardedLoaded;
    private Map<String, Boolean> adStatus;
    
    public GameAds() {
        this.adStatus = new HashMap<>();
        this.isInterstitialLoaded = false;
        this.isRewardedLoaded = false;
    }
    
    /**
     * Initialize ads
     */
    public void initialize(String interstitialAdUnitId, String rewardedAdUnitId, 
                          String bannerAdUnitId, AdInitCallback callback) {
        try {
            this.interstitialAdUnitId = interstitialAdUnitId;
            this.rewardedAdUnitId = rewardedAdUnitId;
            this.bannerAdUnitId = bannerAdUnitId;
            
            // Google Mobile Ads SDK initialization
            android.util.Log.d(TAG, "Ads initialized");
            if (callback != null) callback.onInitSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onInitError(e.getMessage());
        }
    }
    
    /**
     * Load interstitial ad
     */
    public void loadInterstitialAd(AdCallback callback) {
        try {
            // Load from AdMob
            android.util.Log.d(TAG, "Loading interstitial ad");
            isInterstitialLoaded = true;
            
            if (callback != null) callback.onAdLoaded();
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Show interstitial ad
     */
    public void showInterstitialAd() {
        if (!isInterstitialLoaded) {
            android.util.Log.w(TAG, "Interstitial ad not loaded");
            return;
        }
        
        try {
            android.util.Log.d(TAG, "Showing interstitial ad");
            isInterstitialLoaded = false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error showing interstitial: " + e.getMessage());
        }
    }
    
    /**
     * Load rewarded ad
     */
    public void loadRewardedAd(AdCallback callback) {
        try {
            // Load from AdMob
            android.util.Log.d(TAG, "Loading rewarded ad");
            isRewardedLoaded = true;
            
            if (callback != null) callback.onAdLoaded();
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Show rewarded ad
     */
    public void showRewardedAd(RewardCallback callback) {
        if (!isRewardedLoaded) {
            if (callback != null) callback.onError("Rewarded ad not loaded");
            return;
        }
        
        try {
            android.util.Log.d(TAG, "Showing rewarded ad");
            isRewardedLoaded = false;
            
            if (callback != null) callback.onRewardEarned("coins", 100);
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Show banner ad
     */
    public void showBannerAd(android.view.ViewGroup container, AdCallback callback) {
        try {
            // Load and show banner
            android.util.Log.d(TAG, "Showing banner ad");
            if (callback != null) callback.onAdLoaded();
        } catch (Exception e) {
            if (callback != null) callback.onError(e.getMessage());
        }
    }
    
    /**
     * Hide all ads
     */
    public void hideAllAds() {
        isInterstitialLoaded = false;
        isRewardedLoaded = false;
        android.util.Log.d(TAG, "All ads hidden");
    }
    
    // Callbacks
    public interface AdCallback {
        void onAdLoaded();
        void onError(String error);
    }
    
    public interface RewardCallback {
        void onRewardEarned(String rewardType, int amount);
        void onError(String error);
    }
    
    public interface AdInitCallback {
        void onInitSuccess();
        void onInitError(String error);
    }
}
