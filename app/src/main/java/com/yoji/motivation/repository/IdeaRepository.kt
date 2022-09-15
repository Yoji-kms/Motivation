package com.yoji.motivation.repository

import androidx.paging.PagingData
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface IdeaRepository {
    fun getAll(): Flow<PagingData<IdeaWithAuthor>>
    fun getByAuthorId(authorId: Long): Flow<PagingData<IdeaWithAuthor>>
    fun getById(id: Long): Idea
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(idea: Idea)
}