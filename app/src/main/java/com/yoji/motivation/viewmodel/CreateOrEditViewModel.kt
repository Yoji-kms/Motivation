package com.yoji.motivation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.repository.IdeaRepository
import com.yoji.motivation.repository.IdeaRepositoryRoomDbImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

private val emptyIdea = Idea(
    id = 0L,
    content = "",
    author = "Me",
    published = Calendar.getInstance().time,
    likesCounter = 0,
    imageUri = Uri.parse("null"),
    link = ""
)

@HiltViewModel
class CreateOrEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ideaRepository: IdeaRepository = IdeaRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).ideaDAO()
    )
) : ViewModel() {

    val editingIdea = MutableLiveData(emptyIdea)

    fun clear() {
        editingIdea.value = emptyIdea
    }

    fun edit(ideaId: Long) {
        editingIdea.value = ideaRepository.getById(ideaId)
    }

    fun save() {
        editingIdea.value?.let { ideaRepository.save(it) }
        editingIdea.value = emptyIdea
    }

    fun changeContent(
        newContent: String,
        newImageImgView: ShapeableImageView,
        context: Context,
        newLink: String
    ) {
        deletePrevImageFile()
        val file = createFileFromImgView(newImageImgView, context)
        val newImageUri = if (file != null) Uri.fromFile(file) else Uri.parse("null")
        val newContentTrimmed = newContent.trim()
        if (editingIdea.value?.content == newContentTrimmed
            && editingIdea.value?.imageUri == newImageUri
            && editingIdea.value?.link == newLink
        ) {
            return
        }
        editingIdea.value = editingIdea.value?.copy(
            content = newContentTrimmed,
            imageUri = newImageUri,
            link = newLink,
            published = Calendar.getInstance().time
        )
    }

    private fun deletePrevImageFile() = with(editingIdea.value?.imageUri) {
        if (this.toString() != "null"
            && this != null
        ) toFile().delete()
    }

    private fun createFileFromImgView(imageView: ShapeableImageView, context: Context) =
        if (imageView.isVisible)
            context
                .filesDir
                .resolve(IdeaListViewModel.IMAGE_DIR)
                .also {
                    if (!it.exists()) it.mkdir()
                }
                .resolve(Calendar.getInstance().timeInMillis.toString() + ".jpeg")
                .also { file ->
                    FileOutputStream(file).use {
                        viewToBitmap(imageView).compress(
                            Bitmap.CompressFormat.JPEG,
                            50,
                            it
                        )
                    }
                } else null


    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }
}