package com.yoji.motivation.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.paging.PagingSource
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.entity.IdeaEntity
import com.yoji.motivation.entity.IdeaWithAuthorEntity

@Dao
interface IdeaDAO {
    @Query(
        """SELECT ideas.*, authors.name AS authorName FROM ideas INNER JOIN authors
        ON ideas.authorId = authors.id ORDER BY likesCounter DESC"""
    )
    fun getAllWithAuthors(): PagingSource<Int, IdeaWithAuthorEntity>

    @Query(
        """SELECT ideas.*, authors.name AS authorName FROM ideas INNER JOIN authors
         ON ideas.authorId = authors.id WHERE authorId = :authorId ORDER BY likesCounter DESC""")
    fun getByAuthorId(authorId: Long): PagingSource<Int, IdeaWithAuthorEntity>

    @Query("SELECT * FROM ideas WHERE id = :id")
    fun getById(id: Long): Idea

    @Query("UPDATE ideas SET likesCounter = likesCounter + 1 WHERE id = :id")
    fun likeById(id: Long)

    @Query("UPDATE ideas SET likesCounter = likesCounter - 1 WHERE id = :id")
    fun dislikeById(id: Long)

    @Insert
    fun insert(idea: IdeaEntity)

    @Query("DELETE FROM ideas WHERE id = :id")
    fun removeById(id: Long)

    @Query(
        """UPDATE ideas SET
        content = :content,
        imageUri = :imageUri,
        link = :link
        WHERE id = :id"""
    )
    fun updateContentById(id: Long, content: String, imageUri: Uri?, link: String?)

    fun save(idea: IdeaEntity) =
        if (idea.id == 0L) insert(idea) else updateContentById(
            idea.id,
            idea.content,
            idea.imageUri,
            idea.link
        )
}