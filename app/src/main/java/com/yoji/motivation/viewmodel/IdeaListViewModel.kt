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
import com.yoji.motivation.dto.Author
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.repository.AuthorRepository
import com.yoji.motivation.repository.AuthorRepositoryRoomDbImplementation
import com.yoji.motivation.repository.IdeaRepository
import com.yoji.motivation.repository.IdeaRepositoryRoomDbImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    ),
    private val authorRepository: AuthorRepository = AuthorRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).authorDAO()
    )
) : ViewModel() {

    private val emptyAuthor: Flow<Author> = MutableStateFlow(Author(0L, ""))

    private val authorSetted: MutableStateFlow<Long> = MutableStateFlow(
        savedStateHandle.get(AUTHOR_SAVED_STATE_KEY) ?: 0L
    )

    private val authorFilter: MutableStateFlow<Long> = MutableStateFlow(
        savedStateHandle.get(AUTHOR_SAVED_STATE_KEY) ?: NO_AUTHOR
    )

    val currentAuthor = authorSetted.flatMapLatest { author_id ->
        if (author_id == 0L) {
            emptyAuthor
        } else {
            authorRepository.getById(author_id)
        }
    }.asLiveData()

    val data: LiveData<PagingData<IdeaWithAuthor>> = authorFilter.flatMapLatest { author_id ->
        if (author_id == NO_AUTHOR) {
            ideaRepository.getAll()
        } else {
            ideaRepository.getByAuthorId(author_id)
        }
    }.asLiveData().cachedIn(viewModelScope)

    fun likeById(id: Long) = ideaRepository.likeById(id)
    fun dislikeById(id: Long) = ideaRepository.dislikeById(id)
    fun removeById(id: Long) = ideaRepository.removeById(id)
    fun share(ideaWithAuthor: IdeaWithAuthor, context: Context) {
        val idea = ideaWithAuthor.idea
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
            putExtra(Intent.EXTRA_TEXT, ideaWithAuthor.authorName + "\n" + idea.content)
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
            authorFilter.collect { newAuthor ->
                savedStateHandle.set(AUTHOR_SAVED_STATE_KEY, newAuthor)
            }
            authorSetted.collect{ settedAuthor ->
                savedStateHandle.set(AUTHOR_SETTED_KEY, settedAuthor)
            }
        }
    }

    fun changeAuthorName(id: Long, newName: String) = authorRepository.updateById(id, newName)

    fun setAuthor(authorId: Long){
        authorSetted.value = authorId
    }

    fun setAuthorFilter(author_id: Long) {
        authorFilter.value = author_id
    }

    fun clearAuthorFilter() {
        authorFilter.value = NO_AUTHOR
    }

    fun isFiltered() = authorFilter.value != NO_AUTHOR

    companion object {
        private const val NO_AUTHOR = 0L
        private const val AUTHOR_SAVED_STATE_KEY = "AUTHOR_SAVED_STATE_KEY"
        private const val AUTHOR_SETTED_KEY = "AUTHOR_SETTED_KEY"
        const val IMAGE_DIR = "images"
    }
}