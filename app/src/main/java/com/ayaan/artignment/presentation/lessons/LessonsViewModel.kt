package com.ayaan.artignment.presentation.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.artignment.domain.usecase.GetLessonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val getLessonsUseCase: GetLessonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LessonsUiState>(LessonsUiState.Loading)
    val uiState: StateFlow<LessonsUiState> = _uiState.asStateFlow()

    init {
        loadLessons()
    }

    fun loadLessons() {
        viewModelScope.launch {
            _uiState.value = LessonsUiState.Loading
            try {
                val lessons = getLessonsUseCase()
                _uiState.value = LessonsUiState.Success(lessons)
            } catch (e: Exception) {
                _uiState.value = LessonsUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}
