package com.alperenturker.myapplication.analytics.di


import com.alperenturker.myapplication.analytics.AnalyticsLogger
import com.alperenturker.myapplication.analytics.FirebaseAnalyticsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.analytics.ktx.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsBindingModule {
    @Binds @Singleton
    abstract fun bindAnalyticsLogger(impl: FirebaseAnalyticsLogger): AnalyticsLogger
}

