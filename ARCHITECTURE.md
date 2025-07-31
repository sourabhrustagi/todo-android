# Architecture Documentation

## Project Overview

This Android Todo application follows Clean Architecture principles and is developed as a complete task management application with mock API integration:

- **Complete Task Management Application**: Full-featured task management with mock API integration for development

## Tech Stack

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

## Application Structure

```
app/src/main/java/com/mobizonetech/todo/
├── TodoActivity.kt                    # Main activity entry point
├── TodoApplication.kt                 # Application class with Hilt setup
├── di/                               # Dependency injection modules
│   ├── AppModule.kt                  # Application-level dependencies
│   ├── NetworkModule.kt              # Network-related dependencies
│   └── DatabaseModule.kt             # Database dependencies
├── data/                             # Data layer
│   ├── api/                          # API interfaces and models
│   │   ├── TodoApiService.kt         # Retrofit API service interface
│   │   ├── models/                   # API request/response models
│   │   │   ├── TaskApiModel.kt       # Task API data models
│   │   │   ├── AuthApiModel.kt       # Authentication API data models
│   │   │   ├── FeedbackApiModel.kt   # Feedback API data models
│   │   │   └── ApiResponse.kt        # Generic API response wrapper
│   │   └── interceptors/             # HTTP interceptors
│   │       ├── AuthInterceptor.kt    # Authentication interceptor
│   │       ├── LoggingInterceptor.kt # Request/response logging
│   │       └── MockInterceptor.kt    # Mock API responses
│   ├── database/                     # Local database
│   │   ├── TodoDatabase.kt           # Room database class
│   │   ├── entities/                 # Database entities
│   │   │   ├── TaskEntity.kt         # Task database entity
│   │   │   ├── CategoryEntity.kt     # Category database entity
│   │   │   ├── UserEntity.kt         # User database entity
│   │   │   └── FeedbackEntity.kt     # Feedback database entity
│   │   └── dao/                      # Data Access Objects
│   │       ├── TaskDao.kt            # Task database operations
│   │       ├── CategoryDao.kt        # Category database operations
│   │       ├── UserDao.kt            # User database operations
│   │       └── FeedbackDao.kt        # Feedback database operations
│   ├── repository/                   # Repository layer
│   │   ├── TaskRepository.kt         # Task repository implementation
│   │   ├── AuthRepository.kt         # Authentication repository implementation
│   │   └── FeedbackRepository.kt     # Feedback repository implementation
│   └── datasource/                   # Data sources
│       ├── remote/                   # Remote data sources
│       │   ├── TaskRemoteDataSource.kt # API data source for tasks
│       │   ├── AuthRemoteDataSource.kt # API data source for auth
│       │   └── FeedbackRemoteDataSource.kt # API data source for feedback
│       └── local/                    # Local data sources
│           ├── TaskLocalDataSource.kt # Local cache for tasks
│           ├── UserLocalDataSource.kt # Local cache for user data
│           └── FeedbackLocalDataSource.kt # Local cache for feedback
├── domain/                           # Domain layer (business logic)
│   ├── models/                       # Domain models
│   │   ├── Task.kt                   # Task domain model
│   │   ├── Category.kt               # Category domain model
│   │   ├── User.kt                   # User domain model
│   │   └── Feedback.kt               # Feedback domain model
│   ├── usecases/                     # Use cases (business logic)
│   │   ├── auth/                     # Authentication use cases
│   │   │   ├── LoginUseCase.kt       # Login with OTP use case
│   │   │   ├── LogoutUseCase.kt      # Logout use case
│   │   │   └── VerifyOtpUseCase.kt   # OTP verification use case
│   │   ├── task/                     # Task-related use cases
│   │   │   ├── GetTasksUseCase.kt    # Get all tasks use case
│   │   │   ├── CreateTaskUseCase.kt  # Create task use case
│   │   │   ├── UpdateTaskUseCase.kt  # Update task use case
│   │   │   ├── DeleteTaskUseCase.kt  # Delete task use case
│   │   │   └── CompleteTaskUseCase.kt # Complete task use case
│   │   └── feedback/                 # Feedback use cases
│   │       ├── SubmitFeedbackUseCase.kt # Submit feedback use case
│   │       └── GetFeedbackUseCase.kt # Get feedback use case
│   └── repository/                   # Repository interfaces
│       ├── TaskRepository.kt         # Task repository interface
│       ├── AuthRepository.kt         # Authentication repository interface
│       └── FeedbackRepository.kt     # Feedback repository interface
├── presentation/                      # Presentation layer (UI)
│   ├── auth/                         # Authentication UI
│   │   ├── AuthScreen.kt             # Login screen with OTP
│   │   ├── AuthViewModel.kt          # Authentication view model
│   │   └── components/               # Auth UI components
│   │       ├── OtpInput.kt           # OTP input component
│   │       └── LoginForm.kt          # Login form component
│   ├── tasks/                        # Task-related UI
│   │   ├── TasksScreen.kt            # Main tasks screen composable
│   │   ├── TasksViewModel.kt         # Tasks screen view model
│   │   ├── components/               # Task UI components
│   │   │   ├── TaskItem.kt           # Individual task item composable
│   │   │   ├── TaskList.kt           # Task list composable
│   │   │   ├── AddTaskDialog.kt      # Add task dialog composable
│   │   │   └── TaskFilter.kt         # Task filter composable
│   │   └── state/                    # UI state models
│   │       ├── TasksUiState.kt       # Tasks screen UI state
│   │       └── TaskItemState.kt      # Task item UI state
│   ├── feedback/                     # Feedback UI
│   │   ├── FeedbackScreen.kt         # Feedback screen composable
│   │   ├── FeedbackViewModel.kt      # Feedback view model
│   │   └── components/               # Feedback UI components
│   │       ├── RatingComponent.kt    # Rating component
│   │       └── FeedbackForm.kt       # Feedback form component
│   ├── navigation/                   # Navigation
│   │   ├── NavGraph.kt               # Navigation graph
│   │   └── Screen.kt                 # Screen destinations
│   └── common/                       # Common UI components
│       ├── LoadingSpinner.kt         # Loading indicator
│       ├── ErrorView.kt              # Error display component
│       └── EmptyState.kt             # Empty state component
├── ui/theme/                         # UI theming
│   ├── Color.kt                      # Color definitions
│   ├── Theme.kt                      # Material theme setup
│   └── Type.kt                       # Typography definitions
├── util/                             # Utility classes
│   ├── ComposeUtils.kt               # Compose utility functions
│   ├── DateUtils.kt                  # Date formatting utilities
│   └── NetworkUtils.kt               # Network connectivity utilities
└── core/                             # Core application components
    ├── network/                      # Network utilities
    │   ├── NetworkResult.kt          # Network result wrapper
    │   └── ApiException.kt           # API exception handling
    └── extensions/                   # Kotlin extensions
        ├── StringExtensions.kt        # String utility extensions
        └── DateExtensions.kt          # Date utility extensions
```

## Application Architecture

### Complete Task Management Application

#### **Core Components**
- **AuthScreen**: Login screen with OTP functionality
- **AuthViewModel**: Manages authentication state and OTP verification
- **TasksScreen**: Main composable for task list display
- **TasksViewModel**: Manages UI state and coordinates with domain layer
- **TaskRepository**: Implements repository pattern for tasks
- **TaskDao**: Room DAO for local database operations
- **TodoApiService**: Retrofit interface for API calls
- **MockInterceptor**: Mock API responses for development
- **TaskFilter**: Advanced filtering and search functionality
- **TaskCategories**: Category management system
- **PriorityManager**: Priority level handling
- **DateUtils**: Due date management utilities
- **ThemeManager**: Dark/light theme switching
- **FeedbackManager**: User feedback and rating system

#### **Data Flow with Mock API**
```
UI (Compose) → ViewModel → UseCase → Repository → RemoteDataSource → MockInterceptor → Mock API Response
                                    ↓
                              LocalDataSource → Room Database
                                    ↓
                              CacheManager → Offline Support
```

#### **Key Features Implementation**
- **Authentication**: Login via OTP and secure logout functionality
- **Task CRUD Operations**: Complete create, read, update, delete functionality
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

## Clean Architecture Layers

### **1. Presentation Layer (`presentation/`)**
**Purpose**: Handles UI logic and user interactions

**Key Classes**:
- **`TasksScreen.kt`**: Main composable for task list display
- **`TasksViewModel.kt`**: Manages UI state and coordinates with domain layer
- **`TaskItem.kt`**: Individual task item composable
- **`TaskFilter.kt`**: Task filtering and search functionality

**State Management**: Uses StateFlow for reactive UI state management with proper lifecycle handling

### **2. Domain Layer (`domain/`)**
**Purpose**: Contains business logic and use cases

**Key Classes**:
- **`GetTasksUseCase.kt`**: Retrieves tasks from repository
- **`CreateTaskUseCase.kt`**: Creates new task
- **`UpdateTaskUseCase.kt`**: Updates existing task
- **`DeleteTaskUseCase.kt`**: Deletes task
- **`CompleteTaskUseCase.kt`**: Completes task

**Domain Models**: Clean domain models with proper data types and validation

### **3. Data Layer (`data/`)**
**Purpose**: Manages data operations and API communication

**Key Classes**:
- **`TaskRepository.kt`**: Implements repository pattern for tasks
- **`TodoApiService.kt`**: Retrofit interface for API calls
- **`TaskDao.kt`**: Room DAO for local database operations
- **`TaskRemoteDataSource.kt`**: Handles remote API data
- **`TaskLocalDataSource.kt`**: Manages local cache

**Repository Implementation**: Implements repository pattern with proper error handling and offline-first architecture

## Dependency Injection Structure

### **Hilt Modules**:
- **`AppModule.kt`**: Application-level dependencies
- **`NetworkModule.kt`**: Retrofit, OkHttp, and API services
- **`DatabaseModule.kt`**: Room database and DAOs

**Network Module**: Provides Retrofit, OkHttp, and API services with proper interceptors and error handling

## Data Flow Architecture

### **1. API-First Data Flow**:
```
UI (Compose) → ViewModel → UseCase → Repository → RemoteDataSource → API
                                    ↓
                              LocalDataSource → Room Database
```

### **2. State Management Flow**:
```
API Response → Repository → UseCase → ViewModel → StateFlow → UI Update
```

### **3. Error Handling Flow**:
```
API Error → Repository → UseCase → ViewModel → Error State → UI Error Display
```

## Key Component Interactions

### **ViewModel-Repository Pattern**: Implements MVVM with proper state management and lifecycle handling

### **Repository-DataSource Pattern**: Abstracts data sources with proper error handling and offline support

## UI Component Hierarchy

### **Screen Structure**:
```
TasksScreen
├── TopAppBar
├── TaskFilter
├── TaskList
│   ├── TaskItem
│   ├── TaskItem
│   └── TaskItem
├── FloatingActionButton
└── AddTaskDialog
```

### **Composable Function Structure**: Uses modern Compose patterns with proper state hoisting and lifecycle management

## Database Schema

### **Database Entities**: Properly structured Room entities with relationships and data validation

## Network Layer Structure

### **API Service Interface**: Type-safe Retrofit interface with proper annotations and error handling

## Error Handling Architecture

### **Resource Wrapper**: Sealed class for handling API responses with proper error states

### **Exception Handling**: Comprehensive exception hierarchy for different error scenarios

## Design Patterns Used

### **1. Clean Architecture**
- **Separation of Concerns**: Clear boundaries between layers
- **Dependency Rule**: Dependencies point inward toward domain layer
- **Testability**: Each layer can be tested independently

### **2. MVVM (Model-View-ViewModel)**
- **View**: Compose UI components
- **ViewModel**: Manages UI state and business logic
- **Model**: Domain models and data layer

### **3. Repository Pattern**
- **Abstraction**: Hides data source complexity
- **Single Source of Truth**: Centralized data access
- **Offline-First**: Local cache with remote sync

### **4. Use Case Pattern**
- **Business Logic**: Encapsulates specific business rules
- **Single Responsibility**: Each use case has one purpose
- **Testability**: Easy to unit test business logic

### **5. Dependency Injection (Hilt)**
- **Loose Coupling**: Components don't create dependencies
- **Testability**: Easy to mock dependencies
- **Lifecycle Management**: Proper scoping and lifecycle

### **6. Observer Pattern**
- **Reactive Programming**: StateFlow and LiveData
- **Event-Driven**: UI responds to data changes
- **Lifecycle-Aware**: Automatic lifecycle management

## Architecture Benefits

### **1. Scalability**
- **Modular Design**: Easy to add new features
- **Separation of Concerns**: Clear responsibilities
- **Loose Coupling**: Components are independent

### **2. Maintainability**
- **Clean Code**: Well-structured and readable
- **Consistent Patterns**: Standardized approach
- **Documentation**: Clear architecture documentation

### **3. Testability**
- **Unit Testing**: Each layer can be tested independently
- **Mocking**: Easy to mock dependencies
- **Test Coverage**: Comprehensive testing strategy

### **4. Performance**
- **Efficient Data Flow**: Optimized state management
- **Memory Management**: Proper lifecycle handling
- **Caching**: Offline-first with smart caching

### **5. User Experience**
- **Responsive UI**: Reactive state management
- **Offline Support**: Works without internet
- **Error Handling**: Graceful error states

## Best Practices

### **1. Code Organization**
- **Package Structure**: Clear separation by feature and layer
- **Naming Conventions**: Consistent naming across the app
- **File Organization**: Logical grouping of related files

### **2. Error Handling**
- **Resource Wrapper**: Consistent error handling
- **User-Friendly Messages**: Clear error messages
- **Graceful Degradation**: App works even with errors

### **3. State Management**
- **Single Source of Truth**: Centralized state
- **Immutable State**: State objects are immutable
- **Predictable Updates**: Clear state update flow

### **4. Performance**
- **Lazy Loading**: Load data only when needed
- **Caching**: Smart caching strategies
- **Memory Management**: Proper lifecycle handling

## Development Guidelines

### **1. Adding New Features**
1. **Domain Layer**: Define use cases and models
2. **Data Layer**: Implement repository and data sources
3. **Presentation Layer**: Create UI components and ViewModels
4. **Testing**: Add comprehensive tests

### **2. Code Review Checklist**
- [ ] Follows Clean Architecture principles
- [ ] Proper error handling
- [ ] Unit tests included
- [ ] Documentation updated
- [ ] Performance considerations

### **3. Testing Strategy**
- **Unit Tests**: Test individual components
- **Integration Tests**: Test layer interactions
- **UI Tests**: Test user interactions

This architecture provides a solid foundation for building a scalable, maintainable, and testable Android application following modern development practices. 