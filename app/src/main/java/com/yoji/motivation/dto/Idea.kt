package com.yoji.motivation.dto

import android.net.Uri
import java.util.*

data class Idea(
    val id: Long,
    val authorId: Long,
    val published: Date,
    val content: String,
    val imageUri: Uri,
    val link: String,
    var likesCounter: Int
)
