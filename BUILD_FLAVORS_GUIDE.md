# Build Flavors Guide

## Overview

The Android Todo app now supports three distinct build flavors for different environments:

- **Mock**: Uses simulated API responses for development
- **Development**: Connects to development API server
- **Production**: Connects to production API server

## Build Flavors Configuration

### 1. **Mock Flavor**
- **Purpose**: Development with simulated API responses
- **Application ID**: `com.mobizonetech.todo.mock`
- **Version Suffix**: `-mock`
- **Mock API**: Enabled
- **API Logging**: Enabled
- **Base URL**: `https://api.todoapp.com/v1/` (but uses mock responses)

### 2. **Development Flavor**
- **Purpose**: Development with real API server
- **Application ID**: `com.mobizonetech.todo.dev`
- **Version Suffix**: `-dev`
- **Mock API**: Disabled
- **API Logging**: Enabled
- **Base URL**: `https://dev-api.todoapp.com/v1/`

### 3. **Production Flavor**
- **Purpose**: Production release
- **Application ID**: `com.mobizonetech.todo`
- **Version Suffix**: None
- **Mock API**: Disabled
- **API Logging**: Disabled
- **Base URL**: `https://api.todoapp.com/v1/`

## Building Different Flavors

### Using Android Studio
1. Open **Build Variants** panel (View → Tool Windows → Build Variants)
2. Select the desired flavor:
   - `mockDebug` - Mock environment with debug features
   - `mockRelease` - Mock environment for release
   - `developmentDebug` - Development environment with debug features
   - `developmentRelease` - Development environment for release
   - `productionDebug` - Production environment with debug features
   - `productionRelease` - Production environment for release

### Using Command Line
```bash
# Build Mock Debug
./gradlew assembleMockDebug

# Build Mock Release
./gradlew assembleMockRelease

# Build Development Debug
./gradlew assembleDevelopmentDebug

# Build Development Release
./gradlew assembleDevelopmentRelease

# Build Production Debug
./gradlew assembleProductionDebug

# Build Production Release
./gradlew assembleProductionRelease

# Build All Variants
./gradlew assemble
```

## Flavor-Specific Features

### 1. **App Names**
- **Mock**: "Todo Mock"
- **Development**: "Todo Dev"
- **Production**: "Todo"

### 2. **Environment Indicators**
- **Mock**: Orange indicator with "MOCK" label
- **Development**: Blue indicator with "DEV" label
- **Production**: Green indicator with "PROD" label

### 3. **Settings Screen**
- **Debug builds**: Show environment information and mock API toggle
- **Release builds**: Hide development settings

### 4. **API Configuration**
Each flavor has different API configurations:

```kotlin
// Mock Flavor
buildConfigField("String", "ENVIRONMENT", "\"MOCK\"")
buildConfigField("boolean", "USE_MOCK_API", "true")
buildConfigField("boolean", "ENABLE_API_LOGGING", "true")

// Development Flavor
buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
buildConfigField("boolean", "USE_MOCK_API", "false")
buildConfigField("boolean", "ENABLE_API_LOGGING", "true")

// Production Flavor
buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
buildConfigField("boolean", "USE_MOCK_API", "false")
buildConfigField("boolean", "ENABLE_API_LOGGING", "false")
```

## File Structure

```
app/src/
├── main/                    # Common code and resources
├── mock/                    # Mock flavor specific
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml  # "Todo Mock"
│   │   │   └── colors.xml   # Orange theme
├── development/             # Development flavor specific
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml  # "Todo Dev"
│   │   │   └── colors.xml   # Blue theme
└── production/              # Production flavor specific
    ├── res/
    │   ├── values/
    │   │   ├── strings.xml  # "Todo"
    │   │   └── colors.xml   # Green theme
```

## Configuration Files

### 1. **build.gradle.kts**
```kotlin
flavorDimensions += "environment"

productFlavors {
    create("mock") {
        dimension = "environment"
        applicationIdSuffix = ".mock"
        versionNameSuffix = "-mock"
        buildConfigField("String", "ENVIRONMENT", "\"MOCK\"")
        buildConfigField("boolean", "USE_MOCK_API", "true")
        buildConfigField("boolean", "ENABLE_API_LOGGING", "true")
        buildConfigField("String", "BASE_URL", "\"https://api.todoapp.com/v1/\"")
    }
    
    create("development") {
        dimension = "environment"
        applicationIdSuffix = ".dev"
        versionNameSuffix = "-dev"
        buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
        buildConfigField("boolean", "USE_MOCK_API", "false")
        buildConfigField("boolean", "ENABLE_API_LOGGING", "true")
        buildConfigField("String", "BASE_URL", "\"https://dev-api.todoapp.com/v1/\"")
    }
    
    create("production") {
        dimension = "environment"
        buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
        buildConfigField("boolean", "USE_MOCK_API", "false")
        buildConfigField("boolean", "ENABLE_API_LOGGING", "false")
        buildConfigField("String", "BASE_URL", "\"https://api.todoapp.com/v1/\"")
    }
}
```

### 2. **ApiConfig.kt**
```kotlin
object ApiConfig {
    val currentEnvironment: Environment = when (BuildConfig.ENVIRONMENT) {
        "MOCK" -> Environment.MOCK
        "DEVELOPMENT" -> Environment.DEVELOPMENT
        "PRODUCTION" -> Environment.PRODUCTION
        else -> Environment.DEVELOPMENT
    }
    
    val baseUrl: String = BuildConfig.BASE_URL
    val useMockApi: Boolean = BuildConfig.USE_MOCK_API
    val enableApiLogging: Boolean = BuildConfig.ENABLE_API_LOGGING
}
```

## Development Workflow

### 1. **Local Development**
```bash
# Use mock flavor for development
./gradlew installMockDebug
```

### 2. **Testing with Real API**
```bash
# Use development flavor to test with dev server
./gradlew installDevelopmentDebug
```

### 3. **Production Testing**
```bash
# Use production flavor for final testing
./gradlew installProductionDebug
```

### 4. **Release Builds**
```bash
# Build production release
./gradlew assembleProductionRelease
```

## Environment Indicators

The app includes visual indicators to help distinguish between environments:

### 1. **Environment Indicator Component**
```kotlin
@Composable
fun EnvironmentIndicator(
    modifier: Modifier = Modifier
) {
    // Shows colored badge with environment name
    // Only visible in debug builds
}
```

### 2. **Settings Screen**
- Shows current environment information
- Displays API configuration details
- Provides mock API toggle (debug only)

## Best Practices

### 1. **Development**
- Use **Mock** flavor for initial development
- Use **Development** flavor when testing with real API
- Always test with **Production** flavor before release

### 2. **Testing**
- Run unit tests on all flavors
- Run UI tests on mock flavor for consistency
- Test API integration on development flavor

### 3. **Release**
- Always build production flavor for release
- Verify all features work in production environment
- Check that logging is disabled in production

### 4. **Configuration**
- Keep sensitive data out of version control
- Use BuildConfig fields for environment-specific values
- Use flavor-specific resources for UI customization

## Troubleshooting

### Common Issues

1. **Build Fails**
   - Check that all flavor-specific resources exist
   - Verify BuildConfig fields are properly defined

2. **Wrong Environment**
   - Check Build Variants panel in Android Studio
   - Verify flavor selection in build.gradle.kts

3. **Mock API Not Working**
   - Ensure you're using mock flavor
   - Check BuildConfig.USE_MOCK_API value
   - Verify MockInterceptor is added to OkHttpClient

4. **Real API Not Working**
   - Check network connectivity
   - Verify API endpoints are correct
   - Ensure you're not using mock flavor

This setup provides a clean separation between different environments and makes it easy to switch between mock and real API responses during development. 