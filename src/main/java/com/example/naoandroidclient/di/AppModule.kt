package com.example.naoandroidclient.di

import android.app.Application
import android.content.Context
import com.example.naoandroidclient.MainApplication
import com.example.naoandroidclient.data.repository.InMemoryAppRepository
import com.example.naoandroidclient.data.repository.InMemoryDefaultAppRepository
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideApplication(@ApplicationContext context: Context): MainApplication {
        return context as MainApplication
    }

    @Provides
    fun provideLifecycle(application: Application): Lifecycle =
        AndroidLifecycle.ofApplicationForeground(application)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Singleton
    @Provides
    fun providesAppRepository() = InMemoryAppRepository()

    @Singleton
    @Provides
    fun providesDefaultAppRepository() = InMemoryDefaultAppRepository()

}