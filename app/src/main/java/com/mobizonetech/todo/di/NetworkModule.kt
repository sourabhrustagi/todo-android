package com.mobizonetech.todo.di

import com.mobizonetech.todo.core.config.ApiConfig
import com.mobizonetech.todo.core.config.MockApiManager
import com.mobizonetech.todo.data.api.interceptors.ApiLoggingInterceptor
import com.mobizonetech.todo.data.api.interceptors.MockInterceptor
import com.mobizonetech.todo.data.api.interceptors.RetryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        mockApiManager: MockApiManager,
        retryInterceptor: RetryInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            // Add retry interceptor first (before other interceptors)
            addInterceptor(retryInterceptor)
            
            // Conditionally add MockInterceptor based on configuration
            if (mockApiManager.isMockApiEnabled) {
                addInterceptor(MockInterceptor()) // Mock API responses for development
            }
            
            // Conditionally add API logging based on configuration
            if (ApiConfig.enableApiLogging) {
                addInterceptor(ApiLoggingInterceptor()) // Custom API logging
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC // Reduced logging since we have custom interceptor
                })
            }
        }
        .connectTimeout(ApiConfig.connectTimeout, TimeUnit.SECONDS)
        .readTimeout(ApiConfig.readTimeout, TimeUnit.SECONDS)
        .writeTimeout(ApiConfig.writeTimeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.baseUrl)
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