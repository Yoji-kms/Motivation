package com.yoji.motivation.repository

import com.yoji.motivation.dao.AuthorDao
import com.yoji.motivation.dto.Author
import com.yoji.motivation.entity.AuthorEntity
import com.yoji.motivation.entity.toAuthor
import javax.inject.Inject
import javax.inject.Singleton
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class AuthorRepositoryRoomDbImplementation @Inject constructor(private val dao: AuthorDao) :
    AuthorRepository {
    private val config = PagingConfig(
        pageSize = 10,
        prefetchDistance = 5,
        enablePlaceholders = false,
        maxSize = 20
    )

    override fun getAll(): Flow<PagingData<Author>> = Pager(config = config) {
        dao.getAll() }.flow.map {
        it.map(AuthorEntity::toAuthor)
    }

    override suspend fun updateById(id: Long, newName: String) = dao.updateById(id, newName)
    override fun getById(id: Long): Flow<Author> = dao.getById(id)
    override suspend fun save(author: Author): Long = dao.save(AuthorEntity.fromAuthor(author))
    override suspend fun remove(author: Author) = dao.remove(AuthorEntity.fromAuthor(author))
}