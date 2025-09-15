package com.alperenturker.domain.use_case.get_movies

import com.alperenturker.core.common.util.Resource
import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun executeGetMovies(search: String): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading())
            val movies = repository.getMovies(search)
            if (movies.isEmpty()) {
                emit(Resource.Error("Movie not found"))
            } else {
                emit(Resource.Success(movies))
            }
        } catch (e: IOException) {
            emit(Resource.Error("No internet connection"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }
}
