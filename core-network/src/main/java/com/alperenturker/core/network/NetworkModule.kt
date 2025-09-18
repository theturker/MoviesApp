// core-network/src/main/java/com/alperenturker/core/network/NetworkModule.kt
package com.alperenturker.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Named
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.alperenturker.core.network.data.remote.GroqApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /* ---------- MOVIE (default) ---------- */

    @Provides @Singleton @Named("movie")
    fun provideMovieOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides @Singleton @Named("movie")
    fun provideMovieRetrofit(
        @Named("movie") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // senin mevcut film API baseUrl
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /* ---------- GROQ (AI) ---------- */

    @Provides @Singleton @Named("groq")
    fun provideGroqOkHttp(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val key = BuildConfig.GROQ_API_KEY
                require(key.isNotBlank()) { "GROQ_API_KEY boş! local.properties ve buildConfigField kontrol et." }
                val req = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $key")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(req)
            }
            .addInterceptor(log)
            .build()
    }


    @Provides @Singleton @Named("groq")
    fun provideGroqRetrofit(
        @Named("groq") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.groq.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideGroqApi(
        @Named("groq") retrofit: Retrofit
    ): GroqApi = retrofit.create(GroqApi::class.java)
}
