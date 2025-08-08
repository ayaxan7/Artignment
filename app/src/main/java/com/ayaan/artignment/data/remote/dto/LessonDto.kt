package com.ayaan.artignment.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LessonsResponseDto(
    val lessons: List<LessonDto>
)

@JsonClass(generateAdapter = true)
data class LessonDto(
    val mentor_name: String,
    val lesson_title: String,
    val video_thumbnail_url: String,
    val lesson_image_url: String,
    val video_url: String
)
