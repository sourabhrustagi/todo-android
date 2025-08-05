# Testing Best Practices Guide

## Overview

This guide outlines the testing strategy and best practices for the Android Todo app, ensuring high code quality and reliability.

## Testing Strategy

### 1. **Unit Testing**
- **Purpose**: Test individual components in isolation
- **Coverage**: Business logic, use cases, repositories, utilities
- **Tools**: JUnit, MockK, Coroutines Test

### 2. **Integration Testing**
- **Purpose**: Test component interactions
- **Coverage**: Repository implementations, API integrations
- **Tools**: JUnit, MockK, TestCoroutineDispatcher

### 3. **UI Testing**
- **Purpose**: Test user interactions and UI behavior
- **Coverage**: Compose UI components, screen flows
- **Tools**: Compose Testing, Espresso

## Testing Structure

```
app/src/test/java/com/mobizonetech/todo/
├── domain/
│   ├── usecases/
│   │   ├── task/
│   │   │   ├── GetTasksUseCaseTest.kt
│   │   │   ├── CreateTaskUseCaseTest.kt
│   │   │   ├── UpdateTaskUseCaseTest.kt
│   │   │   ├── DeleteTaskUseCaseTest.kt
│   │   │   └── CompleteTaskUseCaseTest.kt
│   │   └── auth/
│   │       ├── LoginUseCaseTest.kt
│   │       └── LogoutUseCaseTest.kt
│   ├── validation/
│   │   └── TaskValidatorTest.kt
│   └── models/
│       └── TaskParamsTest.kt
├── data/
│   ├── repository/
│   │   ├── TaskRepositoryImplTest.kt
│   │   ├── AuthRepositoryImplTest.kt
│   │   └── FeedbackRepositoryImplTest.kt
│   ├── api/
│   │   ├── TodoApiServiceTest.kt
│   │   └── models/
│   │       ├── TaskApiModelTest.kt
│   │       └── ApiResponseTest.kt
│   └── database/
│       ├── dao/
│       │   ├── TaskDaoTest.kt
│       │   └── CategoryDaoTest.kt
│       └── entities/
│           └── TaskEntityTest.kt
├── presentation/
│   ├── tasks/
│   │   ├── TasksViewModelTest.kt
│   │   └── TasksUiStateTest.kt
│   ├── auth/
│   │   └── LoginViewModelTest.kt
│   └── common/
│       └── ErrorHandlerTest.kt
└── core/
    ├── result/
    │   └── ResultTest.kt
    ├── error/
    │   └── ErrorHandlerTest.kt
    └── validation/
        └── ValidationUtilsTest.kt
```

## Unit Testing Examples

### 1. **Use Case Testing**

```kotlin
@RunWith(MockitoJUnitRunner::class)
class CreateTaskUseCaseTest {
    
    @Mock
    private lateinit var taskRepository: TaskRepository
    
    @Mock
    private lateinit var taskValidator: TaskValidator
    
    private lateinit var useCase: CreateTaskUseCase
    
    @Before
    fun setup() {
        useCase = CreateTaskUseCase(taskRepository, taskValidator)
    }
    
    @Test
    fun `createTask should validate input before creating`() = runTest {
        // Given
        val params = CreateTaskParams(
            title = "",
            description = null,
            priority = TaskPriority.MEDIUM,
            categoryId = null,
            dueDate = null
        )
        val validationResult = ValidationResult(
            isValid = false,
            errors = listOf("Title cannot be empty")
        )
        
        whenever(taskValidator.validateCreateTask(params)).thenReturn(validationResult)
        
        // When
        val result = useCase(params)
        
        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ValidationException)
        verify(taskRepository, never()).createTask(any(), any(), any(), any(), any())
    }
    
    @Test
    fun `createTask should create task when validation passes`() = runTest {
        // Given
        val params = CreateTaskParams(
            title = "Test Task",
            description = "Test Description",
            priority = TaskPriority.HIGH,
            categoryId = "category1",
            dueDate = LocalDateTime.now().plusDays(1)
        )
        val validationResult = ValidationResult(isValid = true)
        val createdTask = Task(
            id = "task1",
            title = "Test Task",
            description = "Test Description",
            priority = TaskPriority.HIGH,
            completed = false
        )
        
        whenever(taskValidator.validateCreateTask(params)).thenReturn(validationResult)
        whenever(taskRepository.createTask(
            title = params.title,
            description = params.description,
            priority = params.priority,
            categoryId = params.categoryId,
            dueDate = params.dueDate?.toString()
        )).thenReturn(Result.success(createdTask))
        
        // When
        val result = useCase(params)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(createdTask, (result as Result.Success).data)
        verify(taskRepository).createTask(
            title = params.title,
            description = params.description,
            priority = params.priority,
            categoryId = params.categoryId,
            dueDate = params.dueDate?.toString()
        )
    }
}
```

### 2. **Repository Testing**

```kotlin
@RunWith(MockitoJUnitRunner::class)
class TaskRepositoryImplTest {
    
    @Mock
    private lateinit var apiService: TodoApiService
    
    @Mock
    private lateinit var taskDao: TaskDao
    
    private lateinit var repository: TaskRepositoryImpl
    
    @Before
    fun setup() {
        repository = TaskRepositoryImpl(apiService, taskDao)
    }
    
    @Test
    fun `getTasks should return cached data first then remote data`() = runTest {
        // Given
        val cachedTasks = listOf(
            TaskEntity(id = "1", title = "Cached Task 1"),
            TaskEntity(id = "2", title = "Cached Task 2")
        )
        val remoteTasks = listOf(
            TaskApiModel(id = "3", title = "Remote Task 1"),
            TaskApiModel(id = "4", title = "Remote Task 2")
        )
        val domainTasks = listOf(
            Task(id = "3", title = "Remote Task 1"),
            Task(id = "4", title = "Remote Task 2")
        )
        
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(cachedTasks))
        whenever(apiService.getTasks(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(Result.success(remoteTasks))
        
        // When
        val result = repository.getTasks().first()
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(domainTasks.size, (result as Result.Success).data.size)
        verify(taskDao).getAllTasks()
        verify(apiService).getTasks(any(), any(), any(), any(), any(), any(), any(), any(), any())
    }
    
    @Test
    fun `getTasks should handle network errors gracefully`() = runTest {
        // Given
        val cachedTasks = listOf(TaskEntity(id = "1", title = "Cached Task"))
        val networkError = NetworkException("Network error")
        
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(cachedTasks))
        whenever(apiService.getTasks(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(Result.failure(networkError))
        
        // When
        val result = repository.getTasks().first()
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        // Should still return cached data even when network fails
    }
}
```

### 3. **ViewModel Testing**

```kotlin
@RunWith(MockitoJUnitRunner::class)
class TasksViewModelTest {
    
    @Mock
    private lateinit var getTasksUseCase: GetTasksUseCase
    
    @Mock
    private lateinit var createTaskUseCase: CreateTaskUseCase
    
    @Mock
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    
    @Mock
    private lateinit var completeTaskUseCase: CompleteTaskUseCase
    
    @Mock
    private lateinit var errorHandler: ErrorHandler
    
    private lateinit var viewModel: TasksViewModel
    
    @Before
    fun setup() {
        viewModel = TasksViewModel(
            getTasksUseCase,
            createTaskUseCase,
            mock(),
            deleteTaskUseCase,
            completeTaskUseCase,
            mock()
        )
    }
    
    @Test
    fun `loadTasks should update state with loading then success`() = runTest {
        // Given
        val tasks = listOf(
            Task(id = "1", title = "Test Task 1"),
            Task(id = "2", title = "Test Task 2")
        )
        val result = Result.success(tasks)
        
        whenever(getTasksUseCase()).thenReturn(flowOf(result))
        
        // When
        viewModel.loadTasks()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(tasks, uiState.tasks)
        assertEquals(tasks, uiState.filteredTasks)
        assertNull(uiState.error)
    }
    
    @Test
    fun `loadTasks should handle errors properly`() = runTest {
        // Given
        val error = NetworkException("Network error")
        val result = Result.error<List<Task>>(error)
        
        whenever(getTasksUseCase()).thenReturn(flowOf(result))
        whenever(errorHandler.handle(error)).thenReturn("Network error occurred")
        
        // When
        viewModel.loadTasks()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.tasks.isEmpty())
        assertEquals("Network error occurred", uiState.error)
    }
    
    @Test
    fun `createTask should validate input and create task`() = runTest {
        // Given
        val title = "New Task"
        val description = "New Description"
        val priority = TaskPriority.HIGH
        val createdTask = Task(id = "new1", title = title, description = description, priority = priority)
        
        whenever(createTaskUseCase(title, description, priority))
            .thenReturn(Result.success(createdTask))
        whenever(getTasksUseCase()).thenReturn(flowOf(Result.success(listOf(createdTask))))
        
        // When
        viewModel.createTask(title, description, priority)
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isCreatingTask)
        assertEquals("Task created successfully", uiState.snackbarMessage)
        assertNull(uiState.error)
    }
    
    @Test
    fun `updateSearchQuery should filter tasks`() = runTest {
        // Given
        val tasks = listOf(
            Task(id = "1", title = "Task One"),
            Task(id = "2", title = "Task Two"),
            Task(id = "3", title = "Another Task")
        )
        whenever(getTasksUseCase()).thenReturn(flowOf(Result.success(tasks)))
        
        // Load initial tasks
        viewModel.loadTasks()
        
        // When
        viewModel.updateSearchQuery("One")
        
        // Then
        val uiState = viewModel.uiState.value
        assertEquals("One", uiState.searchQuery)
        assertEquals(1, uiState.filteredTasks.size)
        assertEquals("Task One", uiState.filteredTasks[0].title)
    }
}
```

### 4. **UI State Testing**

```kotlin
class TasksUiStateTest {
    
    @Test
    fun `isEmpty should return true when no tasks and not loading`() {
        // Given
        val uiState = TasksUiState(
            tasks = emptyList(),
            filteredTasks = emptyList(),
            isLoading = false
        )
        
        // When & Then
        assertTrue(uiState.isEmpty)
    }
    
    @Test
    fun `isEmpty should return false when loading`() {
        // Given
        val uiState = TasksUiState(
            tasks = emptyList(),
            filteredTasks = emptyList(),
            isLoading = true
        )
        
        // When & Then
        assertFalse(uiState.isEmpty)
    }
    
    @Test
    fun `hasError should return true when error exists`() {
        // Given
        val uiState = TasksUiState(error = "Test error")
        
        // When & Then
        assertTrue(uiState.hasError)
    }
    
    @Test
    fun `isAnyLoading should return true when any loading state is true`() {
        // Given
        val uiState = TasksUiState(
            isLoading = false,
            isCreatingTask = true,
            isDeletingTask = false
        )
        
        // When & Then
        assertTrue(uiState.isAnyLoading)
    }
    
    @Test
    fun `copyWithLoading should clear error when loading`() {
        // Given
        val uiState = TasksUiState(error = "Test error")
        
        // When
        val result = uiState.copyWithLoading(true)
        
        // Then
        assertTrue(result.isLoading)
        assertNull(result.error)
    }
}
```

## Integration Testing

### 1. **Repository Integration Test**

```kotlin
@RunWith(AndroidJUnit4::class)
class TaskRepositoryIntegrationTest {
    
    private lateinit var database: TodoDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var apiService: TodoApiService
    
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
        apiService = mock()
        repository = TaskRepositoryImpl(apiService, taskDao)
    }
    
    @After
    fun cleanup() {
        database.close()
    }
    
    @Test
    fun `should save and retrieve tasks from database`() = runTest {
        // Given
        val task = TaskEntity(
            id = "test1",
            title = "Test Task",
            description = "Test Description",
            priority = TaskPriority.HIGH,
            completed = false
        )
        
        // When
        taskDao.insertTask(task)
        val retrievedTasks = taskDao.getAllTasks().first()
        
        // Then
        assertEquals(1, retrievedTasks.size)
        assertEquals(task.title, retrievedTasks[0].title)
    }
}
```

## UI Testing

### 1. **Compose UI Testing**

```kotlin
@RunWith(AndroidJUnit4::class)
class TasksScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `should display tasks when loaded`() {
        // Given
        val tasks = listOf(
            Task(id = "1", title = "Task 1"),
            Task(id = "2", title = "Task 2")
        )
        val uiState = TasksUiState(
            tasks = tasks,
            filteredTasks = tasks,
            isLoading = false
        )
        
        // When
        composeTestRule.setContent {
            TasksScreen(
                uiState = uiState,
                onTaskClick = {},
                onCreateTask = {},
                onDeleteTask = {},
                onCompleteTask = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("Task 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task 2").assertIsDisplayed()
    }
    
    @Test
    fun `should show loading indicator when loading`() {
        // Given
        val uiState = TasksUiState(isLoading = true)
        
        // When
        composeTestRule.setContent {
            TasksScreen(
                uiState = uiState,
                onTaskClick = {},
                onCreateTask = {},
                onDeleteTask = {},
                onCompleteTask = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
    
    @Test
    fun `should show error when error occurs`() {
        // Given
        val uiState = TasksUiState(error = "Network error")
        
        // When
        composeTestRule.setContent {
            TasksScreen(
                uiState = uiState,
                onTaskClick = {},
                onCreateTask = {},
                onDeleteTask = {},
                onCompleteTask = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }
}
```

## Testing Best Practices

### 1. **Test Organization**
- Use descriptive test names that explain the scenario
- Follow the Given-When-Then pattern
- Group related tests in the same class
- Use `@Before` for common setup

### 2. **Mocking Strategy**
- Mock external dependencies (API, Database)
- Don't mock the class under test
- Use realistic mock data
- Verify important interactions

### 3. **Test Data**
- Create test data factories
- Use meaningful test data
- Avoid hardcoded values
- Create reusable test utilities

### 4. **Assertions**
- Use specific assertions
- Test both positive and negative cases
- Verify state changes
- Check error conditions

### 5. **Performance**
- Use `runTest` for coroutines
- Avoid blocking operations in tests
- Use in-memory databases for testing
- Mock network calls

## Running Tests

### **Unit Tests**
```bash
./gradlew test
```

### **Instrumented Tests**
```bash
./gradlew connectedAndroidTest
```

### **Specific Test Class**
```bash
./gradlew test --tests "com.mobizonetech.todo.domain.usecases.task.CreateTaskUseCaseTest"
```

### **Test Coverage**
```bash
./gradlew testDebugUnitTestCoverage
```

## Continuous Integration

### **GitHub Actions Example**
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: ./gradlew test
      - run: ./gradlew connectedAndroidTest
```

This testing strategy ensures high code quality, reliability, and maintainability of the Android Todo app. 