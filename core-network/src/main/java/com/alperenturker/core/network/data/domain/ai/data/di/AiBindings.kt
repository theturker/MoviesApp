package com.alperenturker.core.network.data.domain.ai.data.di


import com.alperenturker.core.network.data.domain.ai.AiRecommendationRepository
import com.alperenturker.core.network.data.domain.ai.data.repository.AiRecommendationRepositoryImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiBindings {
    @Binds
    @Singleton
    abstract fun bindAiRepo(impl: AiRecommendationRepositoryImpl): AiRecommendationRepository
}

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {
    @Provides @Singleton fun provideGson(): Gson = Gson()
}