package com.ayaan.artignment.data.remote.api

import com.ayaan.artignment.data.remote.dto.LessonsResponseDto
import retrofit2.http.GET

interface LessonApiService {
    @GET("b/7JF5")
    suspend fun getLessons(): LessonsResponseDto
}
