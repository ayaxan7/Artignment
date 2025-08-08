package com.ayaan.artignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.presentation.detail.LessonDetailScreen
import com.ayaan.artignment.presentation.lessons.LessonsScreen

object Routes {
    const val LESSONS = "lessons"
    const val LESSON_DETAIL = "lesson_detail/{lessonId}/{title}/{mentor}/{thumbnailUrl}/{videoUrl}/{description}"
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    lessons: List<Lesson>
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LESSONS
    ) {
        composable(Routes.LESSONS) {
            LessonsScreen(
                onLessonClick = { lesson ->
                    navController.navigate(
                        "lesson_detail/${lesson.id}/${lesson.title}/${lesson.mentor}/" +
                                "${lesson.thumbnailUrl}/${lesson.videoUrl}/${lesson.description}"
                    )
                }
            )
        }

        composable(
            route = Routes.LESSON_DETAIL,
            arguments = listOf(
                navArgument("lessonId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("mentor") { type = NavType.StringType },
                navArgument("thumbnailUrl") { type = NavType.StringType },
                navArgument("videoUrl") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val mentor = backStackEntry.arguments?.getString("mentor") ?: ""
            val thumbnailUrl = backStackEntry.arguments?.getString("thumbnailUrl") ?: ""
            val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""

            val lesson = Lesson(
                id = lessonId,
                title = title,
                mentor = mentor,
                thumbnailUrl = thumbnailUrl,
                videoUrl = videoUrl,
                description = description
            )

            LessonDetailScreen(
                lesson = lesson,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
