package com.ayaan.artignment.presentation.detail

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayaan.artignment.domain.model.Lesson

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
                title = { Text("Lesson Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.setLesson(lesson) }) {
                        Text("Retry")
                    }
                }
            }
            uiState.lesson != null -> {
                val lesson = uiState.lesson!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Header with lesson title and mentor
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "By ${lesson.mentor}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Video Player
                    VideoPlayer(
                        lesson = lesson,
                        isPlaying = uiState.isVideoPlaying,
                        onTogglePlayback = { viewModel.toggleVideoPlayback() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 16.dp)
                    )

                    // Scrollable content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Lesson Notes",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic lesson notes content
                        when {
                            uiState.isNotesLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Generating lesson notes with AI...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            uiState.notesError != null -> {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Failed to generate notes",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = uiState.notesError ?: "Unknown error",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = { viewModel.retryGenerateNotes() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text("Retry")
                                        }
                                    }
                                }
                            }
                            uiState.lessonNotes.isNotBlank() -> {
                                Text(
                                    text = uiState.lessonNotes,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
                                )
                            }
                            else -> {
                                Text(
                                    text = "No lesson notes available.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Submit Practice Button
                    Button(
                        onClick = { viewModel.showSubmitBottomSheet() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Submit Practice")
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(
    lesson: Lesson,
    isPlaying: Boolean,
    onTogglePlayback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var showThumbnail by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(lesson.videoUrl))
            prepare()
        }
        onDispose {
            exoPlayer?.release()
        }
    }

    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp))
    ) {
        if (showThumbnail) {
            // Show thumbnail initially
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(lesson.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Video thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Play button overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        showThumbnail = false
                        onTogglePlayback()
                        exoPlayer?.playWhenReady = true
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Color.White.copy(alpha = 0.8f),
                            RoundedCornerShape(32.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )
                }
            }
        } else {
            // Show video player
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
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
                title = "Sample Lesson",
                mentor = "John Doe",
                videoUrl = "https://www.example.com/video.mp4",
                thumbnailUrl = "https://www.example.com/thumbnail.jpg",
                description = "These are sample notes for the lesson."
            ),
            onNavigateBack = {}
        )
    }
}
