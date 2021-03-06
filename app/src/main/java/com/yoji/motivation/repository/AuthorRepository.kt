package com.yoji.motivation.repository

import com.yoji.motivation.dto.Author
import javax.inject.Singleton

@Singleton
interface AuthorRepository {
    fun updateById(id: Long, newName: String): Long
    fun getById(id: Long): Author
    fun save(author: Author): Long
}