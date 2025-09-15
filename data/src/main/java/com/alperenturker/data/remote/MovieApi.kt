package com.alperenturker.data.remote

import com.alperenturker.core.network.BuildConfig
import com.alperenturker.data.remote.dto.MovieDetailDto
import com.alperenturker.data.remote.dto.MoviesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(".")
    suspend fun getMovies(
        @Query("s") searchString: String,
        @Query("apikey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): MoviesDto

    @GET(".")
    suspend fun getMovieDetail(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): MovieDetailDto
}
