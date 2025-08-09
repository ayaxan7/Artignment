package com.ayaan.artignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val id: String,
    val title: String,
    val mentor: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val description: String,
    val imageUrl:String
) : Parcelable
