package com.yoji.motivation.di

import android.content.Context
import com.yoji.motivation.dao.AuthorDao
import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.repository.AuthorRepository
import com.yoji.motivation.repository.AuthorRepositoryRoomDbImplementation
import com.yoji.motivation.repository.IdeaRepository
import com.yoji.motivation.repository.IdeaRepositoryRoomDbImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Singleton
    @Provides
    fun provideIdeaRoomDB(@ApplicationContext context: Context): IdeaRoomDB {
        return IdeaRoomDB.getInstance(context)
    }

    @Provides
    fun provideIdeaDAO(ideaRoomDB: IdeaRoomDB): IdeaDAO {
        return ideaRoomDB.ideaDAO()
    }

    @Provides
    fun provideIdeaRepository(dao: IdeaDAO) : IdeaRepository{
        return IdeaRepositoryRoomDbImplementation(dao)
    }

    @Provides
    fun provideAuthorDAO(ideaRoomDB: IdeaRoomDB): AuthorDao {
        return ideaRoomDB.authorDAO()
    }

    @Provides
    fun provideAuthorRepository(dao: AuthorDao) : AuthorRepository{
        return AuthorRepositoryRoomDbImplementation(dao)
    }
}