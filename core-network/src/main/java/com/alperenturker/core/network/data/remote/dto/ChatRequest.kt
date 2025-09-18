package com.alperenturker.core.network.data.remote.dto

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double? = null,
    val max_tokens: Int? = null,
    val response_format: ResponseFormat? = null
)

data class ResponseFormat(val type: String) // "json_object"
