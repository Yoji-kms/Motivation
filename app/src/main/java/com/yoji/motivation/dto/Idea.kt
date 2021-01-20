package com.yoji.motivation.dto

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class Idea(
    val id: Long,
    val author: String,
    val published: Date,
    val content: String,
    val imageUri: Uri,
    val link: String,
    var likesCounter: Int
)

//@Entity(tableName = "ideas")
//data class Idea(
//    @PrimaryKey(autoGenerate = true) val id: Long,
//    @ColumnInfo(name = "author") val author: String,
//    @ColumnInfo(name = "published") val published: Date,
//    @ColumnInfo(name = "content") val content: String,
//    @ColumnInfo(name = "imageUri") val imageUri: Uri,
//    @ColumnInfo(name = "link") val link: String,
//    @ColumnInfo(name = "likesCounter") var likesCounter: Int
//)
