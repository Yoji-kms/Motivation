package com.yoji.motivation.repository

import androidx.paging.PagingData
import com.yoji.motivation.dto.Idea
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface IdeaRepository {
    fun getAll(): Flow<PagingData<Idea>>
    fun getByAuthor(author: String): Flow<PagingData<Idea>>
    fun getById(id: Long): Idea
    fun likeById(id: Long)
    fun dislikeById(id: Long)
    fun removeById(id: Long)
    fun save(idea: Idea)
}