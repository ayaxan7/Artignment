package com.ayaan.artignment.presentation.detail.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayaan.artignment.presentation.detail.UploadState
import com.ayaan.artignment.presentation.detail.UploadUiState
import com.ayaan.artignment.presentation.detail.bottomsheet.components.ErrorContent
import com.ayaan.artignment.presentation.detail.bottomsheet.components.IdleContent
import com.ayaan.artignment.presentation.detail.bottomsheet.components.SuccessContent
import com.ayaan.artignment.presentation.detail.bottomsheet.components.UploadProgressContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitPracticeBottomSheet(
    uploadUiState: UploadUiState,
    onDismiss: () -> Unit,
    onSelectFile: () -> Unit,
    onUploadFile: () -> Unit,
    onRetry: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = bottomSheetState, dragHandle = {
            Surface(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(width = 32.dp, height = 4.dp)
                )
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 16.dp), // Extra bottom padding for navigation bar
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Submit Practice",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (uploadUiState.uploadState) {
                is UploadState.Idle -> {
                    IdleContent(
                        selectedFileName = uploadUiState.selectedFileName,
                        onSelectFile = onSelectFile,
                        onUploadFile = onUploadFile,
                        onDismiss = onDismiss
                    )
                }

                is UploadState.InProgress -> {
                    UploadProgressContent(progress = uploadUiState.uploadState.progress)
                }

                is UploadState.Success -> {
                    SuccessContent()
                }

                is UploadState.Error -> {
                    ErrorContent(
                        message = uploadUiState.uploadState.message,
                        onRetry = onRetry,
                        onDismiss = onDismiss
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubmitPracticeBottomSheetPreview() {
    MaterialTheme {
        SubmitPracticeBottomSheet(
            uploadUiState = UploadUiState(
            uploadState = UploadState.Idle,
            selectedFileName = "practice_submission.pdf",
            isBottomSheetVisible = true
        ), onDismiss = {}, onSelectFile = {}, onUploadFile = {}, onRetry = {})
    }
}
