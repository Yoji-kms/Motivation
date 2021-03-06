package com.yoji.motivation.repository

import com.yoji.motivation.dao.AuthorDao
import com.yoji.motivation.dto.Author
import com.yoji.motivation.entity.AuthorEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorRepositoryRoomDbImplementation @Inject constructor(private val dao: AuthorDao) :
    AuthorRepository {
    override fun updateById(id: Long, newName: String): Long = dao.updateById(id, newName)
    override fun getById(id: Long): Author = dao.getById(id)
    override fun save(author: Author): Long = dao.save(AuthorEntity.fromAuthor(author))
}