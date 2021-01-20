package com.yoji.motivation.repository

import com.yoji.motivation.dto.Idea
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {
    fun getAll(): Flow<List<Idea>>
    fun getByAuthor(author: String): Flow<List<Idea>>
    fun likeById(id: Long)
    fun dislikeById(id: Long)
    fun removeById(id: Long)
    fun save(idea: Idea)
}