package com.alperenturker.core.network.data.remote.dto

data class Message(
    val role: String,          // "system" | "user" | "assistant"
    val content: String
)