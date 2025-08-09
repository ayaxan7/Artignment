package com.ayaan.artignment.presentation.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.presentation.lessons.components.LessonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    onLessonClick: (Lesson) -> Unit, viewModel: LessonsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artium Lessons") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        when (uiState) {
            is LessonsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LessonsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((uiState as LessonsUiState.Success).lessons) { lesson ->
                        LessonItem(
                            lesson = lesson, onItemClick = { onLessonClick(lesson) })
                    }
                }
            }

            is LessonsUiState.Error -> {
                val errorMessage = (uiState as LessonsUiState.Error).message
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadLessons() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LessonItemPreview() {
    MaterialTheme {
        LessonItem(
            lesson = Lesson(
                id = "1",
                title = "Introduction to Android Development",
                mentor = "John Doe",
                thumbnailUrl = "https://example.com/thumbnail.jpg",
                videoUrl = "https://example.com/video.mp4",
                description = "Learn the basics of Android development with Kotlin"
            ), onItemClick = {})
    }
}
