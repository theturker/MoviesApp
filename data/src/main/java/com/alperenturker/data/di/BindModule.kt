package com.alperenturker.data.di

import com.alperenturker.data.repository.MovieRepositoryImpl
import com.alperenturker.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

}

//Hilt’te @Module + @InstallIn ile tanımladığın BindModule,
//manuel “çağırmasan” da derleme sırasında DI grafiğine eklenir.
//MovieRepository arandığında Hilt otomatik olarak MovieRepositoryImpl’e bağlar.