package com.alperenturker.core.network.data.remote

import com.alperenturker.core.network.data.remote.dto.ChatRequest
import com.alperenturker.core.network.data.remote.dto.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GroqApi {
    @POST("openai/v1/chat/completions")
    suspend fun chatCompletions(@Body body: ChatRequest): ChatResponse
}
