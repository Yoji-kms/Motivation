package com.yoji.motivation.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yoji.motivation.dto.Idea
import java.util.*

@Entity(
    tableName = "ideas",
    foreignKeys = [ForeignKey(
        entity = AuthorEntity::class,
        parentColumns = ["id"],
        childColumns = ["authorId"]
    )]
)
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "authorId") val authorId: Long,
    @ColumnInfo(name = "published") val published: Date,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "imageUri") val imageUri: Uri,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "likesCounter") var likesCounter: Int
) {
    companion object {
        fun fromIdea(idea: Idea) = with(idea) {
            IdeaEntity(
                id, authorId, published, content, imageUri, link, likesCounter
            )
        }
    }
}

fun IdeaEntity.toIdea(): Idea = Idea(
    id, authorId, published, content, imageUri, link, likesCounter
)