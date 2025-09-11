package com.alperenturker.myapplication.data.remote

import com.alperenturker.myapplication.data.remote.dto.MovieDetailDto
import com.alperenturker.myapplication.data.remote.dto.MoviesDto
import com.alperenturker.myapplication.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    //Query'deki "s" parametresi, arama sorgusunu temsil eder.
    //https://www.omdbapi.com/?apikey=b10ba4d0&s&i=tt0372784

    @GET(".")
    suspend fun getMovies(
        @Query("s") searchString: String,
        @Query("apikey") apiKey: String = API_KEY
    ) : MoviesDto

    @GET(".")
    suspend fun getMovieDetail(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = API_KEY
    ) : MovieDetailDto
}