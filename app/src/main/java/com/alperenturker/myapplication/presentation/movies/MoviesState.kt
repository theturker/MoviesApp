package com.alperenturker.myapplication.presentation.movies

import com.alperenturker.domain.model.Movie


data class MoviesState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String = "",
    val search: String = "batman"
)
