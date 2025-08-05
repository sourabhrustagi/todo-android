package com.mobizonetech.todo.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.core.error.NetworkException
import com.mobizonetech.todo.core.error.TimeoutException
import com.mobizonetech.todo.core.error.ServerException
import com.mobizonetech.todo.core.error.ValidationException
import com.mobizonetech.todo.core.error.AuthenticationException
import com.mobizonetech.todo.core.validation.ValidationUtils
import com.mobizonetech.todo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(phoneNumber: String) {
        // Validate phone number first
        val validation = ValidationUtils.validatePhoneNumber(phoneNumber)
        if (!validation.isSuccess()) {
            _uiState.value = _uiState.value.copy(
                error = validation.getErrorMessage()
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.login(phoneNumber).fold(
                onSuccess = { message ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        otpRequested = true,
                        message = message
                    )
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is NetworkException -> "Please check your internet connection and try again"
                        is TimeoutException -> "Request timed out. Please try again"
                        is ServerException -> "Server error. Please try again later"
                        is ValidationException -> exception.message ?: "Invalid phone number"
                        else -> "Login failed. Please try again"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            )
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String) {
        // Validate inputs first
        val phoneValidation = ValidationUtils.validatePhoneNumber(phoneNumber)
        val otpValidation = ValidationUtils.validateOtp(otp)
        
        if (!phoneValidation.isSuccess()) {
            _uiState.value = _uiState.value.copy(
                error = phoneValidation.getErrorMessage()
            )
            return
        }
        
        if (!otpValidation.isSuccess()) {
            _uiState.value = _uiState.value.copy(
                error = otpValidation.getErrorMessage()
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.verifyOtp(phoneNumber, otp).fold(
                onSuccess = { _ ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is NetworkException -> "Please check your internet connection and try again"
                        is TimeoutException -> "Request timed out. Please try again"
                        is ServerException -> "Server error. Please try again later"
                        is AuthenticationException -> "Invalid OTP. Please try again"
                        is ValidationException -> exception.message ?: "Invalid OTP"
                        else -> "OTP verification failed. Please try again"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val otpRequested: Boolean = false,
    val isAuthenticated: Boolean = false
) 