package com.yoji.motivation.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.yoji.motivation.dto.IdeaWithAuthor

data class IdeaWithAuthorEntity(
    @Embedded
    val ideaEntity: IdeaEntity,

    @ColumnInfo(name = "authorName")
    val authorName: String
)

fun IdeaWithAuthorEntity.toIdeaWithAuthor(): IdeaWithAuthor = IdeaWithAuthor(
    ideaEntity.toIdea(),
    authorName
)