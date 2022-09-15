package com.yoji.motivation.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yoji.motivation.application.App
import com.yoji.motivation.db.IdeaRoomDB
import com.yoji.motivation.dto.Author
import com.yoji.motivation.repository.AuthorRepository
import com.yoji.motivation.repository.AuthorRepositoryRoomDbImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authorRepository: AuthorRepository = AuthorRepositoryRoomDbImplementation(
        IdeaRoomDB.getInstance(App.appContext()).authorDAO()
    )
) : ViewModel() {

    val data: LiveData<PagingData<Author>> =
        authorRepository.getAll().asLiveData().cachedIn(viewModelScope)

    suspend fun remove(author: Author) = authorRepository.remove(author)
}