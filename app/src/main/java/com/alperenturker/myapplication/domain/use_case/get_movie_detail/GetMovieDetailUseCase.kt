package com.alperenturker.myapplication.domain.use_case.get_movie_detail

import com.alperenturker.myapplication.data.remote.dto.toMovieDetail
import com.alperenturker.myapplication.domain.model.MovieDetail
import com.alperenturker.myapplication.domain.repository.MovieRepository
import com.alperenturker.myapplication.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError


class GetMovieDetailUseCase @Inject constructor(private val repository: MovieRepository) {

    fun executeGetMovieDetails(imdbId: String): Flow<Resource<MovieDetail>> = flow {
        try {
            emit(Resource.Loading())
            val movieDetailDto = repository.getMovieDetail(imdbId)
            emit(Resource.Success(movieDetailDto.toMovieDetail()))
        } catch (e: IOError) {
            emit(Resource.Error("No internet connection"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }
}