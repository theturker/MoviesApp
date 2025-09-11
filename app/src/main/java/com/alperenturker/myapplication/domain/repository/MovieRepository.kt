package com.alperenturker.myapplication.domain.repository

import com.alperenturker.myapplication.data.remote.dto.MovieDetailDto
import com.alperenturker.myapplication.data.remote.dto.MoviesDto


interface MovieRepository {
    suspend fun getMovies(search: String): MoviesDto
    suspend fun getMovieDetail(imdbId: String): MovieDetailDto

}