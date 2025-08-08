package com.ayaan.artignment.domain.repository

import com.ayaan.artignment.domain.model.Lesson

interface LessonRepository {
    suspend fun getLessons(): List<Lesson>
}
