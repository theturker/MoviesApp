package com.alperenturker.data.repository

import com.alperenturker.core.database.dao.BookmarkDao
import com.alperenturker.core.database.entity.BookmarkEntity
import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val dao: BookmarkDao
) : BookmarkRepository {

    override fun observeBookmarks(): Flow<List<Movie>> =
        dao.observeAll().map {
            it.map(BookmarkEntity::toDomain)
        }


    override suspend fun isBookmarked(imdbId: String): Boolean =
        dao.isBookmarked(imdbId) > 0

    override suspend fun add(movie: Movie) {
        dao.add(movie.toEntity())
    }

    override suspend fun remove(imdbId: String) {
        dao.remove(imdbId)
    }
}

// --- mapper'lar ---
private fun BookmarkEntity.toDomain() =
    Movie(Poster = poster.orEmpty(), Title = title, Year = year.orEmpty(), imdbID = imdbId)

private fun Movie.toEntity() =
    BookmarkEntity(imdbId = imdbID, title = Title, poster = Poster, year = Year)