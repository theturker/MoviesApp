package com.alperenturker.core.network.data.domain.ai.data.repository


import com.alperenturker.core.network.data.domain.ai.AiRecommendationRepository
import com.alperenturker.core.network.data.domain.ai.dto.AiRecommendation
import com.alperenturker.core.network.data.domain.ai.dto.RecommendedMovie
import com.alperenturker.core.network.data.remote.GroqApi
import com.alperenturker.core.network.data.remote.dto.ChatRequest
import com.alperenturker.core.network.data.remote.dto.Message
import com.alperenturker.core.network.data.remote.dto.ResponseFormat
import com.google.gson.Gson
import javax.inject.Inject

class AiRecommendationRepositoryImpl @Inject constructor(
    private val api: GroqApi,
    private val gson: Gson
) : AiRecommendationRepository {

    // Groq örnek model: "llama3-70b-8192" (yaygın ve güçlü)
    private val model = "llama-3.3-70b-versatile"

    private val systemPrompt = """
        You are a movie recommender assistant.
        Always reply with PURE JSON (no markdown, no code fences, no commentary),
        matching this schema:
        {
          "items": [
            {"title": "string", "year": "string optional", "reason": "string optional"}
          ]
        }
    """.trimIndent()


    override suspend fun recommend(query: String): AiRecommendation {
        val body = ChatRequest(
            model = model,
            messages = listOf(
                Message("system", systemPrompt),
                Message("user", "Recommend 5 movies for this request and return ONLY JSON: $query")
            ),
            temperature = 0.3,
            max_tokens = 512, // bazı modellerde vermek 400'ü engeller
            response_format = ResponseFormat(type = "json_object") // JSON-only; sorun çıkarırsa kaldır
        )

        val resp = try {
            api.chatCompletions(body)
        } catch (e: retrofit2.HttpException) {
            val err = e.response()?.errorBody()?.string()
            throw IllegalStateException("Groq ${e.code()}: ${err ?: "Bad Request"}")
        }

        val content = resp.choices.firstOrNull()?.message?.content
            ?: throw IllegalStateException("Empty AI response")

        val json = content.trim().let { raw ->
            if (raw.startsWith("{")) raw
            else raw.substringAfter("{", missingDelimiterValue = "{")
                .substringBeforeLast("}", missingDelimiterValue = "}")
                .let { "{$it}" }
        }

        return try {
            gson.fromJson(json, AiRecommendation::class.java)
        } catch (_: Throwable) {
            AiRecommendation(items = listOf(RecommendedMovie(title = content.take(120))))
        }
    }

}
