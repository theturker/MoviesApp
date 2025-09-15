package com.alperenturker.myapplication.presentation.movie_detail

import com.alperenturker.domain.model.MovieDetail


data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: String = ""
)