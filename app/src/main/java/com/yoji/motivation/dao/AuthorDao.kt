package com.yoji.motivation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yoji.motivation.dto.Author
import com.yoji.motivation.entity.AuthorEntity

@Dao
interface AuthorDao {
    @Insert
    fun insert(author: AuthorEntity): Long

    @Query("SELECT * FROM authors WHERE id = :id")
    fun getById(id: Long): Author

    @Query("UPDATE authors SET name = :newName WHERE id = :id")
    fun updateById(id: Long, newName: String): Long = id

    @Query("DELETE FROM authors WHERE id = :id")
    fun removeById(id: Long)

    fun save(author: AuthorEntity): Long =
        if (author.id == 0L) insert(author) else updateById(
            author.id,
            author.name
        )
}