package com.yoji.motivation.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yoji.motivation.converters.Converters
import com.yoji.motivation.dao.AuthorDao
import com.yoji.motivation.dao.IdeaDAO
import com.yoji.motivation.entity.AuthorEntity
import com.yoji.motivation.entity.IdeaEntity
import com.yoji.motivation.workers.Worker

@Database(entities = [IdeaEntity::class, AuthorEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class IdeaRoomDB : RoomDatabase() {
    abstract fun ideaDAO(): IdeaDAO
    abstract fun authorDAO(): AuthorDao

    companion object {
        @Volatile
        private var instance: IdeaRoomDB? = null

        fun getInstance(context: Context): IdeaRoomDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, IdeaRoomDB::class.java, "idea.db")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<Worker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
//                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}