package com.yoji.motivation.repository

import androidx.paging.PagingData
import com.yoji.motivation.dto.Author
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AuthorRepository {
    fun getAll(): Flow<PagingData<Author>>
    fun updateById(id: Long, newName: String)
    fun getById(id: Long): Flow<Author>
    fun save(author: Author): Long
    fun remove(author: Author)
}