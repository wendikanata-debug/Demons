# Demons Premium - AAR Library

Android Archive (AAR) library dengan paket `com.demons.premium`.

## Supported Platforms

- **Android Versions**: Android 10 (API 29) - Android 16 (API 35)
- **Architecture**: 64-bit only (arm64-v8a)
- **Java Version**: Java 17
- **Requirements**: No root required

## Supported Features

### Social Login
- ✅ Facebook Login
- ✅ Twitter (X) Login
- ✅ Google Play Services Integration

### Game Integration
- ✅ Game Analytics (Firebase)
- ✅ In-App Billing (Google Play Billing)
- ✅ Game Leaderboards (Google Play Games Services)
- ✅ Achievement System
- ✅ Cloud Save/Sync
- ✅ Real-time Multiplayer (Firebase Realtime DB)
- ✅ Game Events Tracking
- ✅ Ad Integration (AdMob)
- ✅ Crash Reporting
- ✅ Performance Monitoring

### Project Structure

```
Demons/
├── app/                           # Main application module
│   ├── build.gradle
│   └── src/
├── demons-premium/                # Demons Premium Library Module
│   ├── build.gradle
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/demons/premium/
│   │   │   │       ├── DemonsPremium.java
│   │   │   │       ├── auth/
│   │   │   │       │   ├── AuthManager.java
│   │   │   │       │   ├── FacebookAuth.java
│   │   │   │       │   ├── TwitterAuth.java
│   │   │   │       │   └── GooglePlayAuth.java
│   │   │   │       ├── models/
│   │   │   │       │   └── User.java
│   │   │   │       └── game/
│   │   │   │           ├── GameManager.java
│   │   │   │           ├── GameAnalytics.java
│   │   │   │           ├── GameBilling.java
│   │   │   │           ├── GameLeaderboard.java
│   │   │   │           ├── GameAchievement.java
│   │   │   │           ├── GameMultiplayer.java
│   │   │   │           ├── GameAds.java
│   │   │   │           └── models/
│   │   │   │               ├── GamePlayer.java
│   │   │   │               ├── GameScore.java
│   │   │   │               ├── GameProduct.java
│   │   │   │               └── GameEvent.java
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build/outputs/aar/         # Generated AAR files
├── build.gradle                   # Project-level build config
├── settings.gradle
└── README.md
```

## Features

- Core functionality untuk Demons Premium
- Social Media Authentication (Facebook, Twitter, Google)
- **Game Integration Suite**
  - Analytics & Events Tracking
  - In-App Purchasing
  - Leaderboards & Rankings
  - Achievements System
  - Multiplayer Real-time Sync
  - Cloud Save Support
  - Ad Network Integration
  - Crash & Performance Monitoring
- Resource handling
- Android integration
- 64-bit architecture support
- No root access required
- Java 17 support

## Requirements

- **Java**: Java 17 or higher
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 35 (Android 16)
- **Compile SDK**: 35
- **Architecture**: arm64-v8a (64-bit)
- **Gradle**: 8.2.0 or higher
- **Android Gradle Plugin**: 8.2.0 or higher
- **Firebase Project** (for analytics, realtime DB, crash reporting)
- **Google Play Console Project** (for billing, leaderboards, achievements)

## Build

Generate AAR library:
```bash
./gradlew demons-premium:assembleRelease
```

AAR file location:
```
demons-premium/build/outputs/aar/demons-premium-release.aar
```

## Usage - Game Manager

```java
GameManager gameManager = GameManager.getInstance(context);
gameManager.initialize(new GameCallback() {
    @Override
    public void onSuccess() {
        // Game services initialized
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

## Usage - Game Analytics

```java
GameAnalytics analytics = gameManager.getGameAnalytics();

// Track game event
GameEvent event = new GameEvent("level_completed");
event.addParameter("level", 5);
event.addParameter("score", 1000);
analytics.trackEvent(event);

// Track player action
analytics.trackPlayerAction("weapon_purchased", "rifle");
```

## Usage - In-App Billing

```java
GameBilling billing = gameManager.getGameBilling();

// Purchase game item
GameProduct product = new GameProduct("premium_pack_01");
billing.purchaseProduct(activity, product, new BillingCallback() {
    @Override
    public void onPurchaseSuccess(String orderId) {
        // Handle successful purchase
    }
    
    @Override
    public void onPurchaseError(String error) {
        // Handle purchase error
    }
});
```

## Usage - Leaderboards

```java
GameLeaderboard leaderboard = gameManager.getGameLeaderboard();

// Submit score
GameScore score = new GameScore("global_leaderboard", 5000);
leaderboard.submitScore(score, new LeaderboardCallback() {
    @Override
    public void onSuccess() {
        // Score submitted
    }
    
    @Override
    public void onError(String error) {}
});

// Get top scores
leaderboard.getTopScores("global_leaderboard", 10, new ScoresCallback() {
    @Override
    public void onScoresRetrieved(List<GameScore> scores) {
        // Display scores
    }
    
    @Override
    public void onError(String error) {}
});
```

## Usage - Achievements

```java
GameAchievement achievement = gameManager.getGameAchievement();

// Unlock achievement
achievement.unlock("first_win", new AchievementCallback() {
    @Override
    public void onSuccess() {
        // Achievement unlocked
    }
    
    @Override
    public void onError(String error) {}
});

// Get player achievements
achievement.getAchievements(new AchievementsCallback() {
    @Override
    public void onAchievementsRetrieved(List<String> achievements) {
        // Display achievements
    }
    
    @Override
    public void onError(String error) {}
});
```

## Usage - Multiplayer

```java
GameMultiplayer multiplayer = gameManager.getGameMultiplayer();

// Create game room
multiplayer.createRoom(2, 4, new RoomCallback() {
    @Override
    public void onRoomCreated(String roomId) {
        // Room created, share roomId with other players
    }
    
    @Override
    public void onError(String error) {}
});

// Send real-time message to other players
multiplayer.sendMessage(roomId, new GameMessage(
    "player_action", 
    "shot", 
    position
));
```

## Usage - Ads

```java
GameAds ads = gameManager.getGameAds();
ads.loadInterstitialAd(new AdCallback() {
    @Override
    public void onAdLoaded() {
        ads.showInterstitialAd();
    }
    
    @Override
    public void onError(String error) {}
});

// Show rewarded ad
ads.showRewardedAd(new RewardCallback() {
    @Override
    public void onRewardEarned(String rewardType, int amount) {
        // Give reward to player
    }
    
    @Override
    public void onError(String error) {}
});
```

## Package Name

- **Base Package**: `com.demons.premium`
- **Auth Package**: `com.demons.premium.auth`
- **Game Package**: `com.demons.premium.game`
- **Models Package**: `com.demons.premium.models`

## Versioning

- Version: 1.0.0
- Min SDK: 29 (Android 10)
- Target SDK: 35 (Android 16)
- Compile SDK: 35
- Architectures: arm64-v8a (64-bit only)
- Java Version: 17

## Supported Games

Library ini dirancang untuk mendukung game-game besar seperti:
- PUBG Mobile
- Call of Duty Mobile
- Fortnite Mobile
- Genshin Impact
- Garena Free Fire
- Mobile Legends
- VALORANT Mobile
- Diablo Immortal
- Dan game mobile lainnya yang memerlukan analytics, billing, leaderboards, dan social features.
