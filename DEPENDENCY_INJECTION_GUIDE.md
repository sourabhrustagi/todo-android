# Dependency Injection Guide

## Overview

The Android Todo app follows comprehensive dependency injection best practices using Hilt. This guide outlines the architecture, modules, and best practices implemented.

## Architecture

### 1. **Module Organization**

#### **AppModule.kt** - Main Coordinator Module
```kotlin
@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        UtilityModule::class,
        FeatureModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {
    // Coordinates all other modules
}
```

#### **NetworkModule.kt** - Network Dependencies
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        mockApiManager: MockApiManager,
        retryInterceptor: RetryInterceptor
    ): OkHttpClient

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit

    @Provides
    @Singleton
    fun provideTodoApiService(retrofit: Retrofit): TodoApiService
}
```

#### **DatabaseModule.kt** - Database Dependencies
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase

    @Provides
    @Singleton
    fun provideTaskDao(database: TodoDatabase): TaskDao

    @Provides
    @Singleton
    fun provideCategoryDao(database: TodoDatabase): CategoryDao

    @Provides
    @Singleton
    fun provideUserDao(database: TodoDatabase): UserDao

    @Provides
    @Singleton
    fun provideFeedbackDao(database: TodoDatabase): FeedbackDao
}
```

#### **RepositoryModule.kt** - Repository Bindings
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindFeedbackRepository(
        feedbackRepositoryImpl: FeedbackRepositoryImpl
    ): FeedbackRepository
}
```

#### **UseCaseModule.kt** - Use Case Dependencies
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Task Use Cases
    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: TaskRepository): GetTasksUseCase

    @Provides
    @Singleton
    fun provideCreateTaskUseCase(
        repository: TaskRepository,
        taskValidator: TaskValidator
    ): CreateTaskUseCase

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repository: TaskRepository): UpdateTaskUseCase

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase

    @Provides
    @Singleton
    fun provideCompleteTaskUseCase(repository: TaskRepository): CompleteTaskUseCase

    // Auth Use Cases
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase

    // Feedback Use Cases
    @Provides
    @Singleton
    fun provideSubmitFeedbackUseCase(repository: FeedbackRepository): SubmitFeedbackUseCase
}
```

#### **UtilityModule.kt** - Utility Dependencies
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {
    @Provides
    @Singleton
    fun provideThemeManager(@ApplicationContext context: Context): ThemeManager

    @Provides
    @Singleton
    fun provideSecurePreferences(@ApplicationContext context: Context): SecurePreferences

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager

    @Provides
    @Singleton
    fun provideErrorHandler(@ApplicationContext context: Context): ErrorHandler

    @Provides
    @Singleton
    fun provideRetryManager(): RetryManager

    @Provides
    @Singleton
    fun provideMockApiManager(@ApplicationContext context: Context): MockApiManager
}
```

#### **FeatureModule.kt** - Feature Dependencies
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {
    @Provides
    @Singleton
    fun provideDataSeeder(): DataSeeder

    @Provides
    @Singleton
    fun provideTodoSyncWorker(): TodoSyncWorker
}
```

#### **ViewModelModule.kt** - ViewModel Dependencies
```kotlin
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideTaskValidator(): TaskValidator

    @Provides
    @ViewModelScoped
    fun provideErrorHandler(): ErrorHandler

    @Provides
    @ViewModelScoped
    fun provideMockApiToggle(): MockApiToggle
}
```

## Dependency Injection Best Practices

### 1. **Module Organization**

#### **Single Responsibility Principle**
- ✅ **NetworkModule**: Only network-related dependencies
- ✅ **DatabaseModule**: Only database-related dependencies
- ✅ **RepositoryModule**: Only repository bindings
- ✅ **UseCaseModule**: Only use case dependencies
- ✅ **UtilityModule**: Only utility dependencies
- ✅ **FeatureModule**: Only feature-specific dependencies

#### **Separation of Concerns**
- ✅ **Clear boundaries**: Each module has a specific purpose
- ✅ **Minimal coupling**: Modules don't depend on each other
- ✅ **Easy testing**: Each module can be tested independently
- ✅ **Maintainable**: Easy to modify and extend

### 2. **Scoping Strategy**

#### **Singleton Scope**
```kotlin
@Singleton
// Used for:
// - Database instances
// - Network clients
// - Repository implementations
// - Utility classes
// - Configuration managers
```

#### **ViewModel Scope**
```kotlin
@ViewModelScoped
// Used for:
// - ViewModel-specific dependencies
// - Validators
// - Error handlers
// - UI-related utilities
```

#### **Activity Scope**
```kotlin
@ActivityScoped
// Used for:
// - Activity-specific dependencies
// - Navigation components
// - Activity-level utilities
```

### 3. **Dependency Injection Patterns**

#### **Constructor Injection**
```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel()
```

#### **Field Injection**
```kotlin
@AndroidEntryPoint
class TodoActivity : ComponentActivity() {
    @Inject
    lateinit var themeManager: ThemeManager
    
    @Inject
    lateinit var securePreferences: SecurePreferences
}
```

#### **Method Injection**
```kotlin
@Inject
fun injectDependencies(
    @ApplicationContext context: Context,
    taskRepository: TaskRepository
) {
    // Method-level injection
}
```

### 4. **Repository Pattern with DI**

#### **Interface-Based Design**
```kotlin
// Domain layer - Interface
interface TaskRepository {
    fun getTasks(): Flow<Result<List<Task>>>
    suspend fun createTask(title: String, description: String?, priority: TaskPriority): Result<Task>
}

// Data layer - Implementation
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val taskDao: TaskDao,
    private val retryManager: RetryManager
) : TaskRepository
```

#### **Binding in RepositoryModule**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository
}
```

### 5. **Use Case Pattern with DI**

#### **Use Case Dependencies**
```kotlin
class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator
) {
    suspend operator fun invoke(params: CreateTaskParams): Result<Task> {
        // Business logic with injected dependencies
    }
}
```

#### **Use Case Module**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideCreateTaskUseCase(
        repository: TaskRepository,
        taskValidator: TaskValidator
    ): CreateTaskUseCase = CreateTaskUseCase(repository, taskValidator)
}
```

### 6. **ViewModel Injection**

#### **HiltViewModel Annotation**
```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel()
```

#### **Compose Integration**
```kotlin
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    // Compose with injected ViewModel
}
```

### 7. **Testing with DI**

#### **Unit Testing**
```kotlin
@RunWith(MockitoJUnitRunner::class)
class TasksViewModelTest {
    @Mock
    private lateinit var getTasksUseCase: GetTasksUseCase
    
    @Mock
    private lateinit var createTaskUseCase: CreateTaskUseCase
    
    private lateinit var viewModel: TasksViewModel
    
    @Before
    fun setup() {
        viewModel = TasksViewModel(
            getTasksUseCase,
            createTaskUseCase,
            mock(),
            mock()
        )
    }
}
```

#### **Integration Testing**
```kotlin
@HiltAndroidTest
class TaskRepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var taskRepository: TaskRepository
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
}
```

### 8. **Configuration Management**

#### **Environment-Specific Configuration**
```kotlin
object ApiConfig {
    val currentEnvironment: Environment = when (BuildConfig.ENVIRONMENT) {
        "MOCK" -> Environment.MOCK
        "DEVELOPMENT" -> Environment.DEVELOPMENT
        "PRODUCTION" -> Environment.PRODUCTION
        else -> Environment.DEVELOPMENT
    }
}
```

#### **Build Flavor Configuration**
```kotlin
// build.gradle.kts
productFlavors {
    create("mock") {
        buildConfigField("String", "ENVIRONMENT", "\"MOCK\"")
        buildConfigField("boolean", "USE_MOCK_API", "true")
    }
    
    create("development") {
        buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
        buildConfigField("boolean", "USE_MOCK_API", "false")
    }
    
    create("production") {
        buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
        buildConfigField("boolean", "USE_MOCK_API", "false")
    }
}
```

## Best Practices

### 1. **Module Design**
- ✅ **Single responsibility**: Each module has one purpose
- ✅ **Clear naming**: Descriptive module names
- ✅ **Proper scoping**: Use appropriate scopes
- ✅ **Minimal dependencies**: Keep modules focused

### 2. **Dependency Management**
- ✅ **Interface segregation**: Use interfaces for abstraction
- ✅ **Dependency inversion**: Depend on abstractions, not concretions
- ✅ **Loose coupling**: Minimize dependencies between modules
- ✅ **High cohesion**: Related dependencies in same module

### 3. **Testing Strategy**
- ✅ **Mock dependencies**: Use mocks for testing
- ✅ **Test modules**: Test each module independently
- ✅ **Integration tests**: Test module interactions
- ✅ **Test coverage**: Comprehensive testing

### 4. **Performance Considerations**
- ✅ **Lazy initialization**: Use lazy loading where appropriate
- ✅ **Singleton scope**: Use singletons for expensive objects
- ✅ **Memory management**: Proper scoping prevents memory leaks
- ✅ **Efficient injection**: Minimize injection overhead

### 5. **Error Handling**
- ✅ **Graceful degradation**: Handle missing dependencies
- ✅ **Clear error messages**: Provide meaningful error information
- ✅ **Fallback mechanisms**: Provide alternatives when possible
- ✅ **Logging**: Log dependency injection issues

## Common Patterns

### 1. **Repository Pattern**
```kotlin
// Interface
interface TaskRepository {
    fun getTasks(): Flow<Result<List<Task>>>
}

// Implementation
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val taskDao: TaskDao
) : TaskRepository

// Binding
@Binds
@Singleton
abstract fun bindTaskRepository(
    taskRepositoryImpl: TaskRepositoryImpl
): TaskRepository
```

### 2. **Use Case Pattern**
```kotlin
class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<Result<List<Task>>> {
        return taskRepository.getTasks()
    }
}
```

### 3. **ViewModel Pattern**
```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel()
```

### 4. **Configuration Pattern**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig = ApiConfig()
}
```

## Troubleshooting

### **Common Issues**

**1. Circular Dependencies**
- **Cause**: Two modules depend on each other
- **Solution**: Extract common dependencies to a separate module

**2. Missing Dependencies**
- **Cause**: Dependency not provided in any module
- **Solution**: Add provider method to appropriate module

**3. Scope Issues**
- **Cause**: Using wrong scope for dependency
- **Solution**: Use appropriate scope (@Singleton, @ViewModelScoped, etc.)

**4. Memory Leaks**
- **Cause**: Holding references in wrong scope
- **Solution**: Use proper scoping and avoid static references

### **Debugging Tips**

**1. Enable Hilt Logging**
```kotlin
// In Application class
if (BuildConfig.DEBUG) {
    // Enable Hilt logging
}
```

**2. Check Module Dependencies**
```kotlin
// Verify all modules are included
@Module(includes = [...])
```

**3. Validate Scopes**
```kotlin
// Ensure proper scope usage
@Singleton // For app-level dependencies
@ViewModelScoped // For ViewModel dependencies
```

This dependency injection architecture ensures maintainable, testable, and scalable code following Android best practices. 