package com.mobizonetech.todo.domain.usecases.auth

import com.mobizonetech.todo.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<String> {
        if (phoneNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("Phone number cannot be empty"))
        }
        
        if (!isValidPhoneNumber(phoneNumber)) {
            return Result.failure(IllegalArgumentException("Invalid phone number format"))
        }
        
        return authRepository.login(phoneNumber)
    }
    
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Basic phone number validation - can be enhanced based on requirements
        return phoneNumber.matches(Regex("^\\+[1-9]\\d{1,14}$"))
    }
} 