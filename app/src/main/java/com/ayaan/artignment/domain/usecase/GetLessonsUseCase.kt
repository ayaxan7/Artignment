package com.ayaan.artignment.domain.usecase

import com.ayaan.artignment.domain.model.Lesson
import com.ayaan.artignment.domain.repository.LessonRepository
import javax.inject.Inject

class GetLessonsUseCase @Inject constructor(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(): List<Lesson> {
        return repository.getLessons()
    }
}
