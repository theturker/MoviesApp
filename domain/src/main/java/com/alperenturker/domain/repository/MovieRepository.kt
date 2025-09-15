package com.alperenturker.domain.repository

import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.model.MovieDetail


interface MovieRepository {
    suspend fun getMovies(search: String): List<Movie>
    suspend fun getMovieDetail(imdbId: String): MovieDetail
}