package com.alperenturker.myapplication.presentation.ai.dto

import com.alperenturker.core.network.data.domain.ai.dto.AiRecommendation

data class AiState(
    val loading: Boolean = false,
    val data: AiRecommendation? = null,
    val error: String? = null
)