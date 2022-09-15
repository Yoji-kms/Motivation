package com.yoji.motivation.repository

import androidx.paging.*
import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.entity.IdeaEntity
import com.yoji.motivation.entity.IdeaWithAuthorEntity
import com.yoji.motivation.entity.toIdeaWithAuthor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryRoomDbImplementation @Inject constructor(private val dao: IdeaDAO) : IdeaRepository {
    private val config = PagingConfig(
        pageSize = 10,
        prefetchDistance = 5,
        enablePlaceholders = true,
        maxSize = 20
    )

    override fun getAll(): Flow<PagingData<IdeaWithAuthor>> = Pager(config = config) {
        dao.getAllWithAuthors() }.flow.map {
        it.map(IdeaWithAuthorEntity::toIdeaWithAuthor)
    }

    override fun getByAuthorId(authorId: Long): Flow<PagingData<IdeaWithAuthor>> =
        Pager(config = config) {
            dao.getByAuthorId(authorId) }.flow.map {
            it.map(IdeaWithAuthorEntity::toIdeaWithAuthor)
        }

    override fun getById(id: Long): Idea = dao.getById(id)

    override suspend fun likeById(id: Long) = dao.likeById(id)

    override suspend fun dislikeById(id: Long) = dao.dislikeById(id)

    override suspend fun removeById(id: Long) = dao.removeById(id)

    override suspend fun save(idea: Idea) = dao.save(IdeaEntity.fromIdea(idea))
}