package com.yoji.motivation.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.material.imageview.ShapeableImageView
import com.yoji.motivation.R
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.repository.IdeaRepository
import com.yoji.motivation.repository.IdeaRepositoryRoomDbImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.io.File
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
class IdeaListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ideaRepository: IdeaRepository = IdeaRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).ideaDAO()
    ),
) : ViewModel() {

    private val author: MutableStateFlow<String> = MutableStateFlow(
        savedStateHandle.get(AUTHOR_SAVED_STATE_KEY) ?: NO_AUTHOR
    )

    val data: LiveData<PagingData<Idea>> = author.flatMapLatest { author_name ->
        if (author_name == NO_AUTHOR) {
            ideaRepository.getAll()
        } else {
            ideaRepository.getByAuthor(author_name)
        }
    }.asLiveData().cachedIn(viewModelScope)

    val editingIdea = MutableLiveData(emptyIdea)

    fun likeById(id: Long) = ideaRepository.likeById(id)
    fun dislikeById(id: Long) = ideaRepository.dislikeById(id)
    fun removeById(id: Long) = ideaRepository.removeById(id)
    fun share(idea: Idea, context: Context) {
        val intent = Intent().apply {
            val shareImageUri = with(idea.imageUri.path) {
                if (this == null) return@with null
                else {
                    getUriForFile(
                        context,
                        "com.yoji.motivation.fileprovider",
                        File(this)
                    )
                }
            }
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, idea.author + "\n" + idea.content)
            if (shareImageUri != null) putExtra(Intent.EXTRA_STREAM, shareImageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "*/*"
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            startActivity(
                context,
                Intent.createChooser(
                    intent,
                    context.getString(R.string.share_idea)
                ),
                null
            )
        }
    }

    fun link(idea: Idea, context: Context) {
        if (idea.link.isNotBlank()) {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(idea.link)
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                startActivity(context, intent, null)
            }
        }
    }

    init {
        viewModelScope.launch {
            author.collect { newAuthor ->
                savedStateHandle.set(AUTHOR_SAVED_STATE_KEY, newAuthor)
            }
        }
    }

    fun edit(idea: Idea) {
        editingIdea.value = idea
    }

    fun clear() {
        editingIdea.value = emptyIdea
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

    fun setAuthor(author_name: String) {
        author.value = author_name
    }

    fun clearAuthor() {
        author.value = NO_AUTHOR
    }

    fun isFiltered() = author.value != NO_AUTHOR

    private fun deletePrevImageFile() = with(editingIdea.value?.imageUri) {
        if (this.toString() != "null"
            && this != null
        ) toFile().delete()
    }

    private fun createFileFromImgView(imageView: ShapeableImageView, context: Context) =
        if (imageView.isVisible)
            context
                .filesDir
                .resolve(IMAGE_DIR)
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

    companion object {
        private const val NO_AUTHOR = "null"
        private const val AUTHOR_SAVED_STATE_KEY = "AUTHOR_SAVED_STATE_KEY"
        const val IMAGE_DIR = "images"
    }
}