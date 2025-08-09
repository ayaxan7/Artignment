package com.ayaan.artignment.presentation.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.presentation.detail.LessonDetailUiState
import com.ayaan.artignment.presentation.detail.LessonDetailViewModel

@Composable
 fun LessonContent(
    lesson: Lesson,
    uiState: LessonDetailUiState,
    paddingValues: PaddingValues,
    viewModel: LessonDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Hero Section with title and mentor
        HeroSection(lesson = lesson)

        // Video Player with enhanced styling
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            VideoPlayer(
                lesson = lesson,
                isPlaying = uiState.isVideoPlaying,
                onTogglePlayback = { viewModel.toggleVideoPlayback() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }

        // Scrollable content with enhanced styling
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            LessonNotesSection(
                uiState = uiState,
                onRetryGenerateNotes = { viewModel.retryGenerateNotes() }
            )
        }

        // Enhanced Submit Practice Button
        SubmitPracticeButton(
            onClick = { viewModel.showSubmitBottomSheet() }
        )
    }
}
