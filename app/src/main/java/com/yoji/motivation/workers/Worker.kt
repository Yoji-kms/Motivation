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
import com.yoji.motivation.viewmodel.IdeaListViewModel
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
        ),
        Idea(
            id = 0L,
            content = "4",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 0, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "5",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 2, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "6",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 3, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "7",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 4, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "8",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 5, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "9",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 6, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "10",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 7, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "11",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 8, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "12",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 9, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "13",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "14",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "15",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "16",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "17",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "18",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "19",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "20",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "21",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "22",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "23",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),
        Idea(
            id = 0L,
            content = "24",
            author = "...and Action!",
            published = Calendar.getInstance().apply { set(2020, 10, 20) }.time,
            likesCounter = 0,
            imageUri = Uri.parse("null"),
            link = ""
        ),

    )

    private fun resToUri(resId: Int): Uri {
        App.appContext().filesDir
            .resolve(IdeaListViewModel.IMAGE_DIR)
            .also {
                if (!it.exists()) it.mkdir()
            }
            .resolve("$resId.jpeg")
            .also { file ->
            FileOutputStream(file).use {
                BitmapFactory.decodeResource(App.appContext().resources, resId).compress(
                    Bitmap.CompressFormat.JPEG,
                    20,
                    it
                )
            }
            return Uri.fromFile(file)
        }
    }
}