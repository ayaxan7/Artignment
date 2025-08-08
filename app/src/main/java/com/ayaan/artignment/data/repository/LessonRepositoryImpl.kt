package com.ayaan.artignment.data.repository

import com.ayaan.artignment.data.mapper.toDomain
import com.ayaan.artignment.data.remote.api.LessonApiService
import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.domain.repository.LessonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val apiService: LessonApiService
) : LessonRepository {

    override suspend fun getLessons(): List<Lesson> {
        val response = apiService.getLessons()
        return response.lessons.map { it.toDomain() }
    }
}
