package com.alperenturker.myapplication.data.repository

import com.alperenturker.myapplication.data.remote.MovieApi
import com.alperenturker.myapplication.data.remote.dto.MovieDetailDto
import com.alperenturker.myapplication.data.remote.dto.MoviesDto
import com.alperenturker.myapplication.domain.repository.MovieRepository
import jakarta.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api : MovieApi) : MovieRepository {
    override suspend fun getMovies(search: String): MoviesDto {
        return api.getMovies(search)
    }
    override suspend fun getMovieDetail(imdbId: String): MovieDetailDto {
        return api.getMovieDetail(imdbId)
    }
}