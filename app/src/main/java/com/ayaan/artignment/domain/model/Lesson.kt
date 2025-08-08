package com.ayaan.artignment.domain.model

data class Lesson(
    val id: String,
    val title: String,
    val mentor: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val description: String
)
