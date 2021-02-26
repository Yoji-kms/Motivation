package com.yoji.motivation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class IdeaListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ideaRepository: IdeaRepository = IdeaRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).ideaDAO()
    )
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

//    val editingIdea = MutableLiveData(emptyIdea)

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

    fun setAuthor(author_name: String) {
        author.value = author_name
    }

    fun clearAuthor() {
        author.value = NO_AUTHOR
    }

    fun isFiltered() = author.value != NO_AUTHOR

    companion object {
        private const val NO_AUTHOR = "null"
        private const val AUTHOR_SAVED_STATE_KEY = "AUTHOR_SAVED_STATE_KEY"
        const val IMAGE_DIR = "images"
    }
}