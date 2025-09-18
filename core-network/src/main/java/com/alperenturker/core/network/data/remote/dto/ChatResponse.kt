package com.alperenturker.core.network.data.remote.dto

data class ChatResponse(
    val id: String?,
    val choices: List<Choice>
)