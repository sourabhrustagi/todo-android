# Android Todo Application

A modern Android task management application built with Jetpack Compose, following Clean Architecture principles and modern Android development practices.

## Development Phase

### Complete Task Management Application
**Full-featured task management with mock API integration**

- **Authentication**: Login via OTP and secure logout functionality
- **Task CRUD Operations**: Create, read, update, delete tasks
- **Basic Task Properties**: Title, description, completion status
- **Task Priorities**: High, Medium, Low priority levels with visual indicators
- **Due Dates**: Calendar integration with date picker
- **Task Categories**: Organize tasks into customizable categories
- **Search & Filter**: Find tasks by title, category, or priority
- **Dark/Light Theme**: Automatic theme switching
- **Swipe Actions**: Swipe to complete or delete tasks
- **Pull-to-Refresh**: Refresh task list with swipe gesture
- **Feedback System**: User feedback and rating functionality
- **Simple UI**: Material Design 3 with modern task list display
- **Local Storage**: Room database for offline task storage
- **Basic Architecture**: MVVM with Repository pattern
- **Mock API Integration**: Mock interceptor for API responses during development
- **Offline Support**: Works without internet with local caching

## Tech Stack & Libraries

### Core Libraries
- **Kotlin**: Programming language
- **Jetpack Compose**: Modern UI toolkit
- **Material Design 3**: UI components and theming
- **Hilt**: Dependency injection
- **Retrofit + OkHttp**: Networking and API calls
- **Room**: Local database
- **Kotlin Coroutines & Flow**: Asynchronous programming

### Testing
- **JUnit**: Unit testing
- **MockK**: Mocking library
- **Espresso**: UI testing

### Development Tools
- **Gradle Kotlin DSL**: Build system
- **KSP**: Annotation processing