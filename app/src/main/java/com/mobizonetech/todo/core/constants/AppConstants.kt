package com.mobizonetech.todo.core.constants

object AppConstants {
    
    // API Configuration
    const val BASE_URL = "https://api.todoapp.com/v1/"
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
    
    // Mock API Configuration
    const val USE_MOCK_API = true // Set to false to use real API
    
    // Validation Constants
    const val MIN_TITLE_LENGTH = 1
    const val MAX_TITLE_LENGTH = 100
    const val MAX_DESCRIPTION_LENGTH = 500
    const val MAX_FEEDBACK_COMMENT_LENGTH = 1000
    const val MAX_CATEGORY_NAME_LENGTH = 50
    const val OTP_LENGTH = 6
    const val MIN_RATING = 1
    const val MAX_RATING = 5
    
    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100
    
    // Cache Configuration
    const val CACHE_SIZE = 100
    const val CACHE_EXPIRY_HOURS = 24L
    
    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L
    
    // Debounce Delays
    const val SEARCH_DEBOUNCE_DELAY = 300L
    const val CLICK_DEBOUNCE_DELAY = 300L
    
    // Network Retry
    const val MAX_RETRY_ATTEMPTS = 3
    const val RETRY_DELAY_MS = 1000L
    
    // Security
    const val TOKEN_EXPIRY_HOURS = 24L
    const val REFRESH_TOKEN_EXPIRY_DAYS = 30L
    
    // UI Constants
    const val SNACKBAR_DURATION_SHORT = 2000L
    const val SNACKBAR_DURATION_LONG = 4000L
    
    // Error Messages
    object ErrorMessages {
        const val NETWORK_ERROR = "Please check your internet connection and try again"
        const val TIMEOUT_ERROR = "Request timed out. Please try again"
        const val SERVER_ERROR = "Server error. Please try again later"
        const val UNAUTHORIZED_ERROR = "Please log in again"
        const val VALIDATION_ERROR = "Invalid data provided"
        const val UNKNOWN_ERROR = "An error occurred. Please try again"
        const val DATA_NOT_FOUND = "Data not found"
    }
    
    // Success Messages
    object SuccessMessages {
        const val TASK_CREATED = "Task created successfully"
        const val TASK_UPDATED = "Task updated successfully"
        const val TASK_DELETED = "Task deleted successfully"
        const val TASK_COMPLETED = "Task completed successfully"
        const val FEEDBACK_SUBMITTED = "Feedback submitted successfully"
        const val LOGIN_SUCCESS = "Login successful"
        const val LOGOUT_SUCCESS = "Logged out successfully"
    }
    
    // Regex Patterns
    object RegexPatterns {
        const val PHONE_NUMBER = "^\\d{7,15}$"
        const val OTP = "^\\d{6}$"
        const val COLOR_HEX = "^#[0-9A-Fa-f]{6}$"
        const val EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    }
    
    // Date Formats
    object DateFormats {
        const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
        const val DISPLAY_DATE = "MMM dd, yyyy"
        const val DISPLAY_TIME = "HH:mm"
        const val DISPLAY_DATE_TIME = "MMM dd, yyyy HH:mm"
    }
    
    // Priority Colors
    object PriorityColors {
        const val HIGH = "#FF5722"
        const val MEDIUM = "#FF9800"
        const val LOW = "#4CAF50"
    }
    
    // Default Categories
    object DefaultCategories {
        const val WORK = "Work"
        const val PERSONAL = "Personal"
        const val SHOPPING = "Shopping"
        const val HEALTH = "Health"
        const val EDUCATION = "Education"
    }
} 