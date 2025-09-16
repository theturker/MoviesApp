package com.alperenturker.myapplication.presentation.movie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperenturker.myapplication.util.Constants.IMDB_ID
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.*
import com.alperenturker.core.common.util.Resource
import com.alperenturker.domain.use_case.bookmark.AddBookmarkUseCase
import com.alperenturker.domain.use_case.bookmark.IsBookmarkedUseCase
import com.alperenturker.domain.use_case.bookmark.RemoveBookmarkUseCase
import com.alperenturker.domain.use_case.get_movie_detail.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val stateHandle: SavedStateHandle,
    private val isBookmarked: IsBookmarkedUseCase,
    private val addBookmark: AddBookmarkUseCase,
    private val removeBookmark: RemoveBookmarkUseCase,
) : ViewModel() {

    private val _state = mutableStateOf<MovieDetailState>(MovieDetailState())//mutable
    val state: State<MovieDetailState> = _state //immutable

    init {
        stateHandle.get<String>(IMDB_ID)?.let { id ->
            getMovieDetail(id)
            //Room
            viewModelScope.launch {
                _state.value = _state.value.copy(isBookmarked = isBookmarked(id))
            }
        }
    }

    fun onToggleBookmark() {
        val curr = _state.value
        val m = curr.movie ?: return
        viewModelScope.launch {
            if (curr.isBookmarked) {
                removeBookmark(m.imdbID)
                _state.value = curr.copy(isBookmarked = false)
            } else {
                // MovieDetail → Movie (bookmark için yeterli alanlar)
                val asMovie = com.alperenturker.domain.model.Movie(
                    Poster = m.Poster,
                    Title = m.Title,
                    Year = m.Year,
                    imdbID = m.imdbID
                )
                addBookmark(asMovie)
                _state.value = curr.copy(isBookmarked = true)
            }
        }
    }

    private fun getMovieDetail(imdbId: String) {
        getMovieDetailUseCase.executeGetMovieDetails(imdbId).onEach {
            when (it) {
                is Resource.Success -> _state.value = _state.value.copy(movie = it.data, isLoading = false)
                is Resource.Error   -> _state.value = _state.value.copy(error = it.message ?: "Error!", isLoading = false)
                is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
            }
        }.launchIn(viewModelScope)
    }
}