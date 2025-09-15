package com.alperenturker.domain.use_case.get_movie_detail

import com.alperenturker.core.common.util.Resource
import com.alperenturker.domain.model.MovieDetail
import com.alperenturker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun executeGetMovieDetails(imdbId: String): Flow<Resource<MovieDetail>> = flow {
        try {
            emit(Resource.Loading())
            val detail = repository.getMovieDetail(imdbId)
            emit(Resource.Success(detail))
        } catch (e: IOException) {
            emit(Resource.Error("No internet connection"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }
}
