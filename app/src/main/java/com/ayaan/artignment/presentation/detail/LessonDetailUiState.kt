package com.ayaan.artignment.presentation.detail

import android.net.Uri
import com.ayaan.artignment.domain.model.Lesson

data class LessonDetailUiState(
    val lesson: Lesson? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVideoPlaying: Boolean = false,
    val lessonNotes: String = "",
    val isNotesLoading: Boolean = false,
    val notesError: String? = null
)

sealed class UploadState {
    object Idle : UploadState()
    data class InProgress(val progress: Int) : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

data class UploadUiState(
    val uploadState: UploadState = UploadState.Idle,
    val selectedFileName: String? = null,
    val selectedFileUri: Uri? = null,
    val isBottomSheetVisible: Boolean = false
)
