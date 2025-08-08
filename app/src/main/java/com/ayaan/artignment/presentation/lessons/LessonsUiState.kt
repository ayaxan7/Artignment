package com.ayaan.artignment.presentation.lessons

import com.ayaan.artignment.domain.model.Lesson

sealed class LessonsUiState {
    object Loading : LessonsUiState()
    data class Success(val lessons: List<Lesson>) : LessonsUiState()
    data class Error(val message: String) : LessonsUiState()
}
