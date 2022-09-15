package com.yoji.motivation.repository

import androidx.paging.PagingData
import com.yoji.motivation.dto.Author
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AuthorRepository {
    fun getAll(): Flow<PagingData<Author>>
    suspend fun updateById(id: Long, newName: String)
    fun getById(id: Long): Flow<Author>
    suspend fun save(author: Author): Long
    suspend fun remove(author: Author)
}