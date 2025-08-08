package com.ayaan.artignment.presentation.detail

import android.view.ViewGroup
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
                Text(
                    text = getLessonNotes(),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
                )
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

private fun getLessonNotes(): String {
    return """
        Welcome to this comprehensive lesson on Android development fundamentals!

        In this lesson, we'll cover the following topics:

        1. Introduction to Android Architecture
        Android follows a layered architecture that consists of several key components:
        - Applications Layer: Contains all the installed apps
        - Application Framework: Provides high-level services to applications
        - Android Runtime: Includes core libraries and ART (Android Runtime)
        - Platform Libraries: Native C/C++ libraries used by Android
        - Linux Kernel: The foundation layer that handles hardware abstraction

        2. Understanding Activities and Fragments
        Activities represent a single screen with a user interface, while Fragments are reusable UI components that can be combined to create flexible layouts. Each Activity has a lifecycle with methods like onCreate(), onStart(), onResume(), onPause(), onStop(), and onDestroy().

        3. Layouts and Views
        Android provides several layout types:
        - LinearLayout: Arranges views in a single row or column
        - RelativeLayout: Positions views relative to parent or other views
        - ConstraintLayout: Flexible layout with constraint-based positioning
        - FrameLayout: Simplest layout for single child view

        4. Intent System
        Intents are messaging objects used to request actions from other components. There are two types:
        - Explicit Intents: Specify the exact component to start
        - Implicit Intents: Declare general action to perform

        5. Data Storage Options
        Android offers several data storage mechanisms:
        - Shared Preferences: For small key-value pairs
        - Internal Storage: Private app data
        - External Storage: Shared data accessible by other apps
        - SQLite Database: Structured data storage
        - Room Database: Modern abstraction over SQLite

        6. Best Practices
        - Follow Material Design guidelines
        - Implement proper error handling
        - Optimize for different screen sizes
        - Handle configuration changes gracefully
        - Use appropriate threading for background tasks

        Remember to practice these concepts by building small projects and experimenting with different components. The best way to learn Android development is through hands-on coding experience!

        Additional Resources:
        - Official Android Developer Documentation
        - Android Jetpack Components
        - Kotlin Programming Language Guide
        - Material Design Guidelines

        Keep practicing and happy coding! 🚀
    """.trimIndent()
}

@Preview(showBackground = true)
@Composable
fun LessonDetailScreenPreview() {
    MaterialTheme {
        LessonDetailScreen(
            lesson = Lesson(
                id = "1",
                title = "Introduction to Android Development",
                mentor = "John Doe",
                thumbnailUrl = "https://example.com/thumbnail.jpg",
                videoUrl = "https://example.com/video.mp4",
                description = "Learn the basics of Android development"
            ),
            onNavigateBack = {}
        )
    }
}
