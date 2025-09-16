package com.alperenturker.domain.repository

import com.alperenturker.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeBookmarks(): Flow<List<Movie>>
    suspend fun isBookmarked(imdbId: String): Boolean
    suspend fun add(movie: Movie)
    suspend fun remove(imdbId: String)
}