package com.ayaan.artignment.domain.usecase

import com.ayaan.artignment.data.ai.GeminiAiService
import javax.inject.Inject

class GenerateLessonNotesUseCase @Inject constructor(
    private val geminiAiService: GeminiAiService
) {
    suspend operator fun invoke(mentorName: String, lessonTitle: String): String {
        return geminiAiService.generateLessonNotes(mentorName, lessonTitle)
    }
}
