package com.yoji.motivation.di

import android.content.Context
import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.db.IdeaRoomDB
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
}