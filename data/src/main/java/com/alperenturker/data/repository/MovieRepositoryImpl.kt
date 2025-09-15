package com.alperenturker.data.repository

import com.alperenturker.data.remote.MovieApi
import com.alperenturker.data.remote.dto.toMovieDetail
import com.alperenturker.data.remote.dto.toMovieList
import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.model.MovieDetail
import com.alperenturker.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getMovies(search: String): List<Movie> {
        val dto = api.getMovies(search)
        return if (dto.Response == "True") dto.toMovieList() else emptyList()
    }

    override suspend fun getMovieDetail(imdbId: String): MovieDetail {
        val dto = api.getMovieDetail(imdbId)
        return dto.toMovieDetail()
    }
}
