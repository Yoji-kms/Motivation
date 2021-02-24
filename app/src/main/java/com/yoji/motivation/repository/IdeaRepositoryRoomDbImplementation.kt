package com.yoji.motivation.repository

import androidx.paging.*
import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.entity.IdeaEntity
import com.yoji.motivation.entity.toIdea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryRoomDbImplementation @Inject constructor(private val dao: IdeaDAO) : IdeaRepository {
    private val config = PagingConfig(
        pageSize = 7,
        prefetchDistance = 3,
        enablePlaceholders = true,
        maxSize = 13
    )

    override fun getAll(): Flow<PagingData<Idea>> = Pager(config = config) { dao.getAll() }.flow.map {
        it.map(IdeaEntity::toIdea)
    }

    override fun getByAuthor(author: String): Flow<PagingData<Idea>> =
        Pager(config = config) { dao.getByAuthor(author) }.flow.map {
            it.map(IdeaEntity::toIdea)
        }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun dislikeById(id: Long) {
        dao.dislikeById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(idea: Idea) {
        dao.save(IdeaEntity.fromIdea(idea))
    }
}