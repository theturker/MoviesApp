package com.alperenturker.core.network.data.remote.dto

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String?
)