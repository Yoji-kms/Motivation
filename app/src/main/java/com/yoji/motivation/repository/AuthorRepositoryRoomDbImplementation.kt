package com.yoji.motivation.repository

import com.yoji.motivation.dao.AuthorDao
import com.yoji.motivation.dto.Author
import com.yoji.motivation.entity.AuthorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorRepositoryRoomDbImplementation @Inject constructor(private val dao: AuthorDao) :
    AuthorRepository {
    override fun updateById(id: Long, newName: String) = dao.updateById(id, newName)
    override fun getById(id: Long): Flow<Author> = dao.getById(id)
    override fun save(author: Author): Long = dao.save(AuthorEntity.fromAuthor(author))
}