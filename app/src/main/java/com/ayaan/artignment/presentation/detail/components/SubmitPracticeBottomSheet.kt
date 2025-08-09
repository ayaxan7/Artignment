package com.ayaan.artignment.presentation.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayaan.artignment.presentation.detail.UploadState
import com.ayaan.artignment.presentation.detail.UploadUiState

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
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        dragHandle = {
            Surface(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                )
            }
        }
    ) {
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

@Composable
private fun IdleContent(
    selectedFileName: String?,
    onSelectFile: () -> Unit,
    onUploadFile: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onSelectFile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AttachFile, contentDescription = "Select file")
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (selectedFileName == null) "Select File" else "Change File")
        }

        if (selectedFileName != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Selected: $selectedFileName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onUploadFile,
                enabled = selectedFileName != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Upload")
            }
        }
    }
}

@Composable
private fun UploadProgressContent(progress: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Uploading...",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
        progress = { progress / 100f },
        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$progress%",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SuccessContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = "Success",
            modifier = Modifier.size(64.dp),
            tint = Color.Green
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Upload Successful!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Green
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your practice submission has been uploaded successfully.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Upload Failed",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onRetry,
                modifier = Modifier.weight(1f)
            ) {
                Text("Retry")
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
            ),
            onDismiss = {},
            onSelectFile = {},
            onUploadFile = {},
            onRetry = {}
        )
    }
}
