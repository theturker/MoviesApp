package com.alperenturker.core.network.data.domain.use_case

import com.alperenturker.core.network.data.domain.ai.AiRecommendationRepository
import javax.inject.Inject

class RecommendMoviesUseCase @Inject constructor(
    private val repo: AiRecommendationRepository
) {
    suspend operator fun invoke(query: String) = repo.recommend(query)
}