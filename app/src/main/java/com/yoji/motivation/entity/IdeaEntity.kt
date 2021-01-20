package com.yoji.motivation.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yoji.motivation.dto.Idea
import java.util.*

@Entity(tableName = "ideas")
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "published") val published: Date,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "imageUri") val imageUri: Uri,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "likesCounter") var likesCounter: Int
) {
    companion object {
        fun fromIdea(idea: Idea) = with(idea) {
            IdeaEntity(
                id, author, published, content, imageUri, link, likesCounter
            )
        }
    }
}

fun IdeaEntity.toIdea(): Idea = Idea(
    id, author, published, content, imageUri, link, likesCounter
)