package com.alperenturker.domain.use_case.bookmark

import com.alperenturker.domain.model.Movie
import com.alperenturker.domain.repository.BookmarkRepository
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val repo: BookmarkRepository
) {
    suspend operator fun invoke(movie: Movie) = repo.add(movie)
}