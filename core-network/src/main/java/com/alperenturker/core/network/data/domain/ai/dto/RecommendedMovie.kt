package com.alperenturker.core.network.data.domain.ai.dto

data class RecommendedMovie(
    val title: String,
    val year: String? = null,
    val reason: String? = null
)