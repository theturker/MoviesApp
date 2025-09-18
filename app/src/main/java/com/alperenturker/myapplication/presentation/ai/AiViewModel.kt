package com.alperenturker.myapplication.presentation.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperenturker.core.network.data.domain.use_case.RecommendMoviesUseCase
import com.alperenturker.myapplication.presentation.ai.dto.AiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val recommendMovies: RecommendMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    fun run(query: String) = viewModelScope.launch {
        _state.value = AiState(loading = true)
        try {
            val res = recommendMovies(query)
            _state.value = AiState(data = res)
        } catch (t: Throwable) {
            val msg = when (t) {
                is retrofit2.HttpException -> when (t.code()) {
                    401 -> "AI servisi yetkisiz (401). GROQ_API_KEY geçersiz/boş olabilir."
                    429 -> "Limit aşıldı (429). Biraz sonra tekrar dene."
                    else -> "AI isteği başarısız: ${t.code()}"
                }
                else -> t.message ?: "Bilinmeyen hata"
            }
            _state.value = AiState(error = msg)
        }
    }
}