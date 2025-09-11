package com.alperenturker.myapplication.presentation.movie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperenturker.myapplication.domain.use_case.get_movie_detail.GetMovieDetailUseCase
import com.alperenturker.myapplication.util.Constants.IMDB_ID
import com.alperenturker.myapplication.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.*
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf<MovieDetailState>(MovieDetailState())//mutable
    val state: State<MovieDetailState> = _state //immutable

    init {
        stateHandle.get<String>(IMDB_ID)?.let {
            getMovieDetail(it)
        }
    }

    private fun getMovieDetail(imdbId: String) {
        getMovieDetailUseCase.executeGetMovieDetails(imdbId).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = MovieDetailState(movie = it.data)
                }
                is Resource.Error -> {
                    _state.value = MovieDetailState(error = it.message ?: "Error!")
                }
                is Resource.Loading -> {
                    _state.value = MovieDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}