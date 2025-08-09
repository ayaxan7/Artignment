package com.ayaan.artignment.presentation.detail

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.presentation.detail.bottomsheet.SubmitPracticeBottomSheet
import com.ayaan.artignment.presentation.detail.components.LessonContent
import com.ayaan.artignment.presentation.detail.state.ErrorState
import com.ayaan.artignment.presentation.detail.state.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    onNavigateBack: () -> Unit,
    viewModel: LessonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uploadUiState by viewModel.uploadUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(context, it)
            viewModel.onFileSelected(it, fileName)
        }
    }

    // Set the callback in the ViewModel
    LaunchedEffect(filePickerLauncher) {
        viewModel.setFilePickerCallback { filePickerLauncher.launch("*/*") }
    }

    LaunchedEffect(lesson) {
        viewModel.setLesson(lesson)
    }

    if (uploadUiState.isBottomSheetVisible) {
        SubmitPracticeBottomSheet(
            uploadUiState = uploadUiState,
            onDismiss = { viewModel.hideSubmitBottomSheet() },
            onSelectFile = { viewModel.selectFile() },
            onUploadFile = { viewModel.uploadFile() },
            onRetry = { viewModel.retryUpload() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lesson Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            if (uiState.lesson != null) {
                FloatingActionButton(
                    onClick = { viewModel.showSubmitBottomSheet() },
                    containerColor = Color.Blue.copy(alpha = 0.7f),
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Upload,
                        contentDescription = "Submit Practice"
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState(paddingValues)
                }

                uiState.error != null -> {
                    ErrorState(
                        error = uiState.error!!,
                        paddingValues = paddingValues,
                        onRetry = { viewModel.setLesson(lesson) }
                    )
                }

                uiState.lesson != null -> {
                    LessonContent(
                        lesson = uiState.lesson!!,
                        uiState = uiState,
                        paddingValues = paddingValues,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}



private fun getFileName(context: Context, uri: Uri): String {
    var fileName = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            fileName = it.getString(nameIndex)
        }
    }
    return fileName
}

@Preview(showBackground = true)
@Composable
fun LessonDetailScreenPreview() {
    MaterialTheme {
        LessonDetailScreen(
            lesson = Lesson(
                id = "1",
                title = "Advanced Digital Art Techniques",
                mentor = "Sarah Johnson",
                videoUrl = "https://www.example.com/video.mp4",
                thumbnailUrl = "https://www.example.com/thumbnail.jpg",
                description = "Learn advanced digital art techniques including shading, lighting, and composition."
            ),
            onNavigateBack = {}
        )
    }
}