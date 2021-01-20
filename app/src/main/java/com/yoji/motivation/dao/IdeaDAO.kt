package com.yoji.motivation.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yoji.motivation.entity.IdeaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDAO {
    @Query("SELECT * FROM ideas ORDER BY likesCounter DESC")
    fun getAll(): Flow<List<IdeaEntity>>

    @Query("SELECT * FROM ideas WHERE author = :author ORDER BY likesCounter DESC")
    fun getByAuthor(author: String): Flow<List<IdeaEntity>>

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