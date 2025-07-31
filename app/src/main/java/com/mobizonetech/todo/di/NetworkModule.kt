package com.mobizonetech.todo.di

import com.mobizonetech.todo.data.api.interceptors.ApiLoggingInterceptor
import com.mobizonetech.todo.data.api.interceptors.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockInterceptor()) // Mock API responses for development
            .addInterceptor(ApiLoggingInterceptor()) // Custom API logging
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC // Reduced logging since we have custom interceptor
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.todoapp.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoApiService(retrofit: Retrofit): com.mobizonetech.todo.data.api.TodoApiService {
        return retrofit.create(com.mobizonetech.todo.data.api.TodoApiService::class.java)
    }
} 