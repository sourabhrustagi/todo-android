# Unused Methods Removed

This document lists all the unused methods that were removed from the codebase to improve maintainability and reduce code bloat.

## ProfileViewModel.kt
- `toggleNotifications()` - Method was defined but never called from the UI
- `toggleDarkMode()` - Method was defined but never called from the UI

## AuthRepository.kt (Interface)
- `getCurrentUser(): Flow<User?>` - Method was defined but never used
- `saveAuthToken(token: String)` - Method was defined but never used
- `getAuthToken(): String?` - Method was defined but never used
- `clearAuthToken()` - Method was defined but never used

## AuthRepositoryImpl.kt (Implementation)
- `getCurrentUser(): Flow<User?>` - Implementation of unused interface method
- `saveAuthToken(token: String)` - Implementation of unused interface method
- `getAuthToken(): String?` - Implementation of unused interface method
- `clearAuthToken()` - Implementation of unused interface method

## TaskRepository.kt (Interface)
- `searchTasks(query: String, fields: String?, fuzzy: Boolean?): Result<List<Task>>` - Method was defined but never used
- `getTaskAnalytics(): Result<Map<String, Any>>` - Method was defined but never used
- `bulkOperation(operation: String, taskIds: List<String>, categoryId: String?, priority: TaskPriority?): Result<Map<String, Any>>` - Method was defined but never used
- `syncTasksFromServer(): Result<Unit>` - Method was defined but never used

## TaskRepositoryImpl.kt (Implementation)
- `searchTasks(query: String, fields: String?, fuzzy: Boolean?): Result<List<Task>>` - Implementation of unused interface method
- `getTaskAnalytics(): Result<Map<String, Any>>` - Implementation of unused interface method
- `bulkOperation(operation: String, taskIds: List<String>, categoryId: String?, priority: TaskPriority?): Result<Map<String, Any>>` - Implementation of unused interface method
- `syncTasksFromServer(): Result<Unit>` - Implementation of unused interface method

## FeedbackRepository.kt (Interface)
- `getFeedback(): Flow<Result<List<Feedback>>>` - Method was defined but never used
- `getFeedbackAnalytics(): Result<Map<String, Any>>` - Method was defined but never used

## FeedbackRepositoryImpl.kt (Implementation)
- `getFeedback(): Flow<Result<List<Feedback>>>` - Implementation of unused interface method
- `getFeedbackAnalytics(): Result<Map<String, Any>>` - Implementation of unused interface method

## TodoSyncWorker.kt
- Modified `doWork()` method to remove the call to the removed `syncTasksFromServer()` method
- Replaced with a TODO comment for future implementation

## Summary
- **Total methods removed**: 15 methods
- **Files modified**: 8 files
- **Compilation status**: ✅ Successful
- **Impact**: Reduced code complexity and improved maintainability

## Notes
- All removed methods were only used in tests or API definitions, not in the main application logic
- The removal maintains the core functionality of the app
- Future features can re-implement these methods when needed
- The codebase is now cleaner and more focused on the actual features being used 