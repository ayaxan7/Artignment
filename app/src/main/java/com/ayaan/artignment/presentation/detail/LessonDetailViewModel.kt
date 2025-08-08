package com.ayaan.artignment.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.artignment.domain.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LessonDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState: StateFlow<LessonDetailUiState> = _uiState.asStateFlow()

    private val _uploadUiState = MutableStateFlow(UploadUiState())
    val uploadUiState: StateFlow<UploadUiState> = _uploadUiState.asStateFlow()

    fun setLesson(lesson: Lesson) {
        _uiState.value = _uiState.value.copy(lesson = lesson)
    }

    fun toggleVideoPlayback() {
        _uiState.value = _uiState.value.copy(
            isVideoPlaying = !_uiState.value.isVideoPlaying
        )
    }

    fun showSubmitBottomSheet() {
        _uploadUiState.value = _uploadUiState.value.copy(
            isBottomSheetVisible = true,
            uploadState = UploadState.Idle
        )
    }

    fun hideSubmitBottomSheet() {
        _uploadUiState.value = _uploadUiState.value.copy(
            isBottomSheetVisible = false,
            uploadState = UploadState.Idle,
            selectedFileName = null
        )
    }

    fun selectFile() {
        // Mock file selection
        val mockFileName = "practice_submission_${System.currentTimeMillis()}.pdf"
        _uploadUiState.value = _uploadUiState.value.copy(selectedFileName = mockFileName)
    }

    fun uploadFile() {
        viewModelScope.launch {
            _uploadUiState.value = _uploadUiState.value.copy(uploadState = UploadState.InProgress(0))

            // Simulate upload progress
            for (progress in 0..100 step 10) {
                delay(200) // Simulate upload time
                _uploadUiState.value = _uploadUiState.value.copy(
                    uploadState = UploadState.InProgress(progress)
                )
            }

            // Simulate random success/failure
            val isSuccess = Random.nextBoolean()
            if (isSuccess) {
                _uploadUiState.value = _uploadUiState.value.copy(uploadState = UploadState.Success)
                delay(1500) // Show success message
                hideSubmitBottomSheet()
            } else {
                _uploadUiState.value = _uploadUiState.value.copy(
                    uploadState = UploadState.Error("Upload failed. Please try again.")
                )
            }
        }
    }

    fun retryUpload() {
        uploadFile()
    }
}
