package com.mobizonetech.todo.presentation.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.models.FeedbackCategory
import com.mobizonetech.todo.domain.usecases.feedback.SubmitFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val submitFeedbackUseCase: SubmitFeedbackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun submitFeedback(
        rating: Int,
        comment: String?,
        category: FeedbackCategory
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            
            println("Submitting feedback: rating=$rating, comment=$comment, category=$category")
            
            submitFeedbackUseCase(rating, comment, category).fold(
                onSuccess = { feedback ->
                    println("Feedback submitted successfully: $feedback")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        error = null
                    )
                },
                onFailure = { exception ->
                    println("Feedback submission failed: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to submit feedback",
                        isSuccess = false
                    )
                }
            )
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}

data class FeedbackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) 