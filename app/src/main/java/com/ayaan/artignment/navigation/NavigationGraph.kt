package com.ayaan.artignment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.presentation.detail.LessonDetailScreen
import com.ayaan.artignment.presentation.lessons.LessonsScreen

sealed class Routes(val route: String) {
    object Lessons : Routes("lessons")
    object LessonDetail : Routes("lesson_detail")
}
@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Lessons.route
    ) {
        composable(Routes.Lessons.route) {
            LessonsScreen(
                onLessonClick = { lesson ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("lesson", lesson)
                    navController.navigate(Routes.LessonDetail.route)
                }
            )
        }

        composable(Routes.LessonDetail.route) { backStackEntry ->
            val lesson = navController.previousBackStackEntry?.savedStateHandle?.get<Lesson>("lesson")

            if (lesson != null) {
                LessonDetailScreen(
                    lesson = lesson,
                    onNavigateBack = { navController.navigateUp() }
                )
            } else {
                // Handle case where lesson is null - navigate back
                LaunchedEffect(Unit) {
                    navController.navigateUp()
                }
            }
        }
    }
}
