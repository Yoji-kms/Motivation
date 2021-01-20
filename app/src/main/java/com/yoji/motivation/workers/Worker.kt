package com.yoji.motivation.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yoji.motivation.R
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.entity.IdeaEntity
import java.io.FileOutputStream
import java.util.*

class Worker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        demoIdeas.forEach {
            IdeaRoomDB
                .getInstance(applicationContext)
                .ideaDAO()
                .save(IdeaEntity.fromIdea(it))
        }
        return Result.success()
    }


    //Demo ideas
    private val demoIdeas = listOf(
        Idea(
            id = 0L,
            content = "Mat Zo - Illusions of Depth",
            author = "Me",
            published = Calendar.getInstance().apply { set(2020, 12, 6) }.time,
            likesCounter = 0,
            imageUri = resToUri(R.drawable.mat_zo_illuzions),
            link = "https://music.youtube.com/playlist?list=OLAK5uy_mIliKxL826Prghzu5yQiQLjK3feL7ZVR4"
        ),
        Idea(
            id = 0L,
            content = "Sandman. Dream Catchers",
            author = "Me",
            published = Calendar.getInstance().apply { set(2020, 6, 6) }.time,
            likesCounter = 0,
            imageUri = resToUri(R.drawable.sandman),
            link = "https://www.wildberries.ru/catalog/8539158/detail.aspx"
        ),
        Idea(
            id = 0L,
            content = "Blade Runner 2049. Art of scale and depth",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 1, 20) }.time,
            likesCounter = 0,
            imageUri = resToUri(R.drawable.blade_runner_2049),
            link = "https://youtu.be/S34y1-CNBhE"
        )
    )

    private fun resToUri(resId: Int): Uri {
        App.appContext().filesDir.resolve("$resId.jpeg").also { file ->
            FileOutputStream(file).use {
                BitmapFactory.decodeResource(App.appContext().resources, resId).compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    it
                )
            }
            return Uri.fromFile(file)
        }
    }
}