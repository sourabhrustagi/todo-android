package com.mobizonetech.todo.core.validation

import com.mobizonetech.todo.core.constants.AppConstants
import com.mobizonetech.todo.core.error.ValidationException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object ValidationUtils {
    
    fun validateTaskTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult.Error("Title cannot be empty")
            title.length < AppConstants.MIN_TITLE_LENGTH -> ValidationResult.Error("Title must be at least ${AppConstants.MIN_TITLE_LENGTH} character")
            title.length > AppConstants.MAX_TITLE_LENGTH -> ValidationResult.Error("Title cannot exceed ${AppConstants.MAX_TITLE_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateTaskDescription(description: String?): ValidationResult {
        return when {
            description == null -> ValidationResult.Success
            description.length > AppConstants.MAX_DESCRIPTION_LENGTH -> ValidationResult.Error("Description cannot exceed ${AppConstants.MAX_DESCRIPTION_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        return when {
            phoneNumber.isBlank() -> ValidationResult.Error("Phone number cannot be empty")
            !phoneNumber.matches(Regex(AppConstants.RegexPatterns.PHONE_NUMBER)) -> ValidationResult.Error("Invalid phone number format")
            else -> ValidationResult.Success
        }
    }
    
    fun validateOtp(otp: String): ValidationResult {
        return when {
            otp.isBlank() -> ValidationResult.Error("OTP cannot be empty")
            !otp.matches(Regex(AppConstants.RegexPatterns.OTP)) -> ValidationResult.Error("OTP must be ${AppConstants.OTP_LENGTH} digits")
            else -> ValidationResult.Success
        }
    }
    
    fun validateDueDate(dueDate: String?): ValidationResult {
        return when {
            dueDate == null -> ValidationResult.Success
            else -> {
                try {
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val parsedDate = LocalDateTime.parse(dueDate, formatter)
                    if (parsedDate.isBefore(LocalDateTime.now())) {
                        ValidationResult.Error("Due date cannot be in the past")
                    } else {
                        ValidationResult.Success
                    }
                } catch (e: DateTimeParseException) {
                    ValidationResult.Error("Invalid date format")
                }
            }
        }
    }
    
    fun validateRating(rating: Int): ValidationResult {
        return when {
            rating < AppConstants.MIN_RATING -> ValidationResult.Error("Rating must be at least ${AppConstants.MIN_RATING}")
            rating > AppConstants.MAX_RATING -> ValidationResult.Error("Rating cannot exceed ${AppConstants.MAX_RATING}")
            else -> ValidationResult.Success
        }
    }
    
    fun validateFeedbackComment(comment: String?): ValidationResult {
        return when {
            comment == null -> ValidationResult.Success
            comment.length > AppConstants.MAX_FEEDBACK_COMMENT_LENGTH -> ValidationResult.Error("Comment cannot exceed ${AppConstants.MAX_FEEDBACK_COMMENT_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateCategoryName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Category name cannot be empty")
            name.length > AppConstants.MAX_CATEGORY_NAME_LENGTH -> ValidationResult.Error("Category name cannot exceed ${AppConstants.MAX_CATEGORY_NAME_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateColorHex(color: String): ValidationResult {
        return when {
            color.isBlank() -> ValidationResult.Error("Color cannot be empty")
            !color.matches(Regex(AppConstants.RegexPatterns.COLOR_HEX)) -> ValidationResult.Error("Invalid color format (use #RRGGBB)")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    
    fun getErrorMessage(): String? = when (this) {
        is Success -> null
        is Error -> message
    }
}

fun ValidationResult.throwIfError() {
    if (this is ValidationResult.Error) {
        throw ValidationException(message)
    }
} 