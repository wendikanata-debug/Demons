package com.demons.premium.auth;

import android.content.Context;
import com.demons.premium.models.User;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthManager class untuk manage semua authentication providers
 */
public class AuthManager {
    private static final String TAG = "AuthManager";
    private static AuthManager instance;
    
    private Context context;
    private FacebookAuth facebookAuth;
    private TwitterAuth twitterAuth;
    private GooglePlayAuth googlePlayAuth;
    private User currentUser;
    private Map<String, Object> authCache;
    
    /**
     * Get singleton instance
     */
    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }
    
    /**
     * Private constructor untuk singleton pattern
     */
    private AuthManager(Context context) {
        this.context = context.getApplicationContext();
        this.authCache = new HashMap<>();
        initializeProviders();
    }
    
    /**
     * Initialize all auth providers
     */
    private void initializeProviders() {
        this.facebookAuth = new FacebookAuth(context);
        this.twitterAuth = new TwitterAuth(context);
        this.googlePlayAuth = new GooglePlayAuth(context);
    }
    
    /**
     * Get Facebook Auth instance
     */
    public FacebookAuth getFacebookAuth() {
        return facebookAuth;
    }
    
    /**
     * Get Twitter Auth instance
     */
    public TwitterAuth getTwitterAuth() {
        return twitterAuth;
    }
    
     /**
     * Get Google Play Auth instance
     */
    public GooglePlayAuth getGooglePlayAuth() {
        return googlePlayAuth;
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            authCache.put("currentUser", user);
        }
    }
    
    /**
     * Clear all auth cache
     */
    public void clearCache() {
        authCache.clear();
        currentUser = null;
    }
    
    /**
     * Check if any provider is logged in
     */
    public boolean isAnyProviderLoggedIn() {
        return currentUser != null || 
               facebookAuth.isLoggedIn() || 
               twitterAuth.isLoggedIn() || 
               googlePlayAuth.isSignedIn();
    }
    
    /**
     * Logout from all providers
     */
    public void logoutAll() {
        facebookAuth.logout();
        twitterAuth.logout();
        googlePlayAuth.signOut();
        currentUser = null;
        clearCache();
    }
    
    /**
     * Get auth provider info
     */
    public Map<String, Boolean> getProvidersStatus() {
        Map<String, Boolean> status = new HashMap<>();
        status.put("facebook", facebookAuth.isLoggedIn());
        status.put("twitter", twitterAuth.isLoggedIn());
        status.put("google", googlePlayAuth.isSignedIn());
        return status;
    }
}
