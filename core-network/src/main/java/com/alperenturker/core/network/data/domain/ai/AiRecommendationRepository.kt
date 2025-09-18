package com.alperenturker.core.network.data.domain.ai

import com.alperenturker.core.network.data.domain.ai.dto.AiRecommendation

interface AiRecommendationRepository {
    suspend fun recommend(query: String): AiRecommendation
}