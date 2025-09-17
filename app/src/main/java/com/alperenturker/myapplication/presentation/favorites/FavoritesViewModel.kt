package com.alperenturker.myapplication.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: BookmarkRepository
) : ViewModel() {

    val favorites = repo.observeBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggle(movie: Movie) = viewModelScope.launch {
        if (repo.isBookmarked(movie.imdbID)) repo.remove(movie.imdbID) else repo.add(movie)
    }
}
