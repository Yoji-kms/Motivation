package com.yoji.motivation.repository

import com.yoji.motivation.dto.Author
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AuthorRepository {
    fun updateById(id: Long, newName: String)
    fun getById(id: Long): Flow<Author>
    fun save(author: Author): Long
}