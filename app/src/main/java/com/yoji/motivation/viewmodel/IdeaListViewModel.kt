package com.yoji.motivation.viewmodel

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.repository.IdeaRepository
import com.yoji.motivation.repository.IdeaRepositoryRoomDbImplementation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*

private val emptyIdea = Idea(
    id = 0L,
    content = "",
    author = "Me",
    published = Calendar.getInstance().time,
    likesCounter = 0,
    imageUri = Uri.parse("null"),
    link = ""
)


class IdeaListViewModel @ViewModelInject internal constructor(
    private val ideaRepository: IdeaRepository = IdeaRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).ideaDAO()
    ),
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val author: MutableStateFlow<String> = MutableStateFlow(
        savedStateHandle.get(AUTHOR_SAVED_STATE_KEY) ?: NO_AUTHOR
    )

    val data: LiveData<List<Idea>> = author.flatMapLatest { author_name ->
        if (author_name == NO_AUTHOR) {
            ideaRepository.getAll()
        } else {
            ideaRepository.getByAuthor(author_name)
        }
    }.asLiveData()

    val editingIdea = MutableLiveData(emptyIdea)

    fun likeById(id: Long) = ideaRepository.likeById(id)
    fun dislikeById(id: Long) = ideaRepository.dislikeById(id)
    fun removeById(id: Long) = ideaRepository.removeById(id)

    init {
        viewModelScope.launch {
            author.collect { newAuthor ->
                savedStateHandle.set(AUTHOR_SAVED_STATE_KEY, newAuthor)
            }
        }
    }

    fun createDemoIdeas() =
        if (data.value.isNullOrEmpty()) ideaRepository.createDemoIdeas() else null

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

    fun changeContent(newContent: String, newImageUri: Uri, newLink: String) {
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

    fun setAuthor (author_name: String){
        author.value = author_name
    }

    fun clearAuthor(){
        author.value = NO_AUTHOR
    }

    fun isFiltered() = author.value != NO_AUTHOR

    companion object {
        private const val NO_AUTHOR = "null"
        private const val AUTHOR_SAVED_STATE_KEY = "AUTHOR_SAVED_STATE_KEY"
    }
}