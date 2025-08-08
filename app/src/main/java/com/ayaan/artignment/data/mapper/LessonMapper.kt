package com.ayaan.artignment.data.mapper

import com.ayaan.artignment.data.remote.dto.LessonDto
import com.ayaan.artignment.domain.model.Lesson
import java.util.UUID

fun LessonDto.toDomain(): Lesson {
    return Lesson(
        id = UUID.randomUUID().toString(), // Generate a unique ID since API doesn't provide one
        title = lesson_title,
        mentor = mentor_name,
        thumbnailUrl = video_thumbnail_url,
        videoUrl = video_url,
        description = "Learn ${lesson_title.lowercase()} with ${mentor_name}"
    )
}
