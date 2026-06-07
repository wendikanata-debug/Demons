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
│   │   │   │       └── models/
│   │   │   │           └── User.java
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
- Social Media Authentication (Facebook, Twitter)
- Google Play Services Integration
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

## Build

Generate AAR library:
```bash
./gradlew demons-premium:assembleRelease
```

AAR file location:
```
demons-premium/build/outputs/aar/demons-premium-release.aar
```

## Usage - Facebook Login

```java
FacebookAuth facebookAuth = new FacebookAuth(context);
facebookAuth.login(new AuthCallback() {
    @Override
    public void onSuccess(User user) {
        // Handle success
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

## Usage - Twitter Login

```java
TwitterAuth twitterAuth = new TwitterAuth(context);
twitterAuth.login(new AuthCallback() {
    @Override
    public void onSuccess(User user) {
        // Handle success
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

## Usage - Google Play Services

```java
GooglePlayAuth googlePlayAuth = new GooglePlayAuth(context);
googlePlayAuth.initialize(new AuthCallback() {
    @Override
    public void onSuccess(User user) {
        // Google Play Services ready
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

## Package Name

- **Base Package**: `com.demons.premium`
- **Auth Package**: `com.demons.premium.auth`
- **Models Package**: `com.demons.premium.models`

## Versioning

- Version: 1.0.0
- Min SDK: 29 (Android 10)
- Target SDK: 35 (Android 16)
- Compile SDK: 35
- Architectures: arm64-v8a (64-bit only)
- Java Version: 17
