package com.alperenturker.domain.use_case.bookmark

import com.alperenturker.domain.repository.BookmarkRepository
import javax.inject.Inject

class RemoveBookmarkUseCase @Inject constructor(
    private val repo: BookmarkRepository
) {
    suspend operator fun invoke(imdbId: String) = repo.remove(imdbId)
}