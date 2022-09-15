package com.yoji.motivation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Author
import com.yoji.motivation.repository.AuthorRepository
import com.yoji.motivation.repository.AuthorRepositoryRoomDbImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val savedStateHandle: SavedStateHandle,
    private val authorRepository: AuthorRepository = AuthorRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).authorDAO()
    )
) : ViewModel() {

    private val emptyAuthor = Author(0L, "")
    private val newAuthor = MutableLiveData(emptyAuthor)

    suspend fun saveAuthor(authorName: String): Long {
        newAuthor.value = newAuthor.value?.copy(name = authorName.trim())
        newAuthor.value?.let {
            return authorRepository.save(it)
        }
        return 0
    }
}