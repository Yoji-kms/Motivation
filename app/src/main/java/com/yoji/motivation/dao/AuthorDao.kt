package com.yoji.motivation.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yoji.motivation.dto.Author
import com.yoji.motivation.entity.AuthorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Insert
    suspend fun insert(author: AuthorEntity): Long

    @Query("SELECT * FROM authors")
    fun getAll(): PagingSource<Int, AuthorEntity>

    @Query("SELECT * FROM authors WHERE id = :id")
    fun getById(id: Long): Flow<Author>

    @Query("UPDATE authors SET name = :newName WHERE id = :id")
    suspend fun updateById(id: Long, newName: String)

    @Query("DELETE FROM authors WHERE id = :id")
    suspend fun removeById(id: Long)

    suspend fun save(author: AuthorEntity): Long =
        if (author.id == 0L) insert(author) else {
            updateById(
                author.id,
                author.name
            )
            0
        }

    @Delete
    suspend fun remove(author: AuthorEntity)
}