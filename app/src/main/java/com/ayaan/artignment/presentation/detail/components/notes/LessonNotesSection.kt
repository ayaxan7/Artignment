package com.ayaan.artignment.presentation.detail.components.notes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontStyle
import com.ayaan.artignment.presentation.detail.LessonDetailUiState
import com.ayaan.artignment.presentation.detail.components.ErrorNotesContent
import com.ayaan.artignment.presentation.detail.components.SectionCard

@Composable
fun LessonNotesSection(
    uiState: LessonDetailUiState,
    onRetryGenerateNotes: () -> Unit
) {
    SectionCard(
        icon = Icons.Default.Description,
        title = "Lesson Notes (AI Generated)",
    ) {
        when {
            uiState.isNotesLoading -> {
                LoadingNotesContent()
            }

            uiState.notesError != null -> {
                ErrorNotesContent(
                    error = uiState.notesError,
                    onRetry = onRetryGenerateNotes
                )
            }

            uiState.lessonNotes.isNotBlank() -> {
                Text(
                    text = uiState.lessonNotes.replace("*","-"),
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.6,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            else -> {
                Text(
                    text = "No lesson notes available at the moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}