# Demons Premium - AAR Library

Android Archive (AAR) library dengan paket `com.demons.premium`.

## Supported Platforms

- **Android Versions**: Android 10 (API 29) - Android 16 (API 35)
- **Architecture**: 64-bit only (arm64-v8a)
- **Java Version**: Java 17
- **Requirements**: No root required

## Project Structure

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

## Usage

1. Copy `demons-premium-release.aar` ke folder `libs/` project Anda
2. Tambahkan ke `build.gradle`:
```groovy
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    implementation(name: 'demons-premium-release', ext: 'aar')
}
```

## Package Name

- **Base Package**: `com.demons.premium`

## Versioning

- Version: 1.0.0
- Min SDK: 29 (Android 10)
- Target SDK: 35 (Android 16)
- Compile SDK: 35
- Architectures: arm64-v8a (64-bit only)
- Java Version: 17
