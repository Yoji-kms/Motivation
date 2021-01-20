package com.yoji.motivation.repository

import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.entity.IdeaEntity
import com.yoji.motivation.entity.toIdea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryRoomDbImplementation @Inject constructor(private val dao: IdeaDAO) {
    fun getAll(): Flow<List<Idea>> = dao.getAll().map {
        it.map(IdeaEntity::toIdea)
    }

    fun getByAuthor(author: String): Flow<List<Idea>> = dao.getByAuthor(author).map {
        it.map(IdeaEntity::toIdea)
    }

    fun likeById(id: Long) {
        dao.likeById(id)
    }

    fun dislikeById(id: Long) {
        dao.dislikeById(id)
    }

    fun removeById(id: Long) {
        dao.removeById(id)
    }

    fun save(idea: Idea) {
        dao.save(IdeaEntity.fromIdea(idea))
    }
}