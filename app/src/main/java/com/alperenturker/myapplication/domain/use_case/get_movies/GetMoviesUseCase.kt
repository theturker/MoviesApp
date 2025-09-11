package com.alperenturker.myapplication.domain.use_case.get_movies

import com.alperenturker.myapplication.data.remote.dto.toMovieList
import com.alperenturker.myapplication.domain.model.Movie
import com.alperenturker.myapplication.domain.repository.MovieRepository
import com.alperenturker.myapplication.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError

class GetMoviesUseCase @Inject constructor(private val repository: MovieRepository) {

    fun executeGetMovies(search : String): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading())
            val movieList = repository.getMovies(search)
            if (movieList.Response == "True") {
                emit(Resource.Success(movieList.toMovieList()))
            } else {
                emit(Resource.Error("Movie not found"))
            }
        } catch (e: IOError) {
            emit(Resource.Error("No internet connection"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }
}