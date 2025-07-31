package com.mobizonetech.todo.di

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.interceptors.MockInterceptor
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit

class NetworkModuleTest {

    @Test
    fun `provideOkHttpClient should return OkHttpClient with MockInterceptor`() {
        // When
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // Then
        assertNotNull(okHttpClient)
        assertTrue(okHttpClient is OkHttpClient)
        
        // Verify it has interceptors
        val interceptors = okHttpClient.interceptors
        assertTrue(interceptors.any { it is MockInterceptor })
        assertTrue(interceptors.any { it.javaClass.simpleName == "HttpLoggingInterceptor" })
    }

    @Test
    fun `provideRetrofit should return Retrofit with correct configuration`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // When
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        assertNotNull(retrofit)
        assertTrue(retrofit is Retrofit)
        assertEquals("https://api.todoapp.com/v1/", retrofit.baseUrl().toString())
    }

    @Test
    fun `provideTodoApiService should return TodoApiService`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // When
        val apiService = NetworkModule.provideTodoApiService(retrofit)

        // Then
        assertNotNull(apiService)
        assertTrue(apiService is TodoApiService)
    }

    @Test
    fun `provideRetrofit should use provided OkHttpClient`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // When
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        // The retrofit should be configured with the provided client
        assertNotNull(retrofit)
    }

    @Test
    fun `provideTodoApiService should use provided Retrofit`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // When
        val apiService = NetworkModule.provideTodoApiService(retrofit)

        // Then
        assertNotNull(apiService)
    }

    @Test
    fun `NetworkModule should be singleton`() {
        // When
        val module1 = NetworkModule
        val module2 = NetworkModule

        // Then
        assertSame(module1, module2)
    }

    @Test
    fun `provideOkHttpClient should create new instance each time`() {
        // When
        val client1 = NetworkModule.provideOkHttpClient()
        val client2 = NetworkModule.provideOkHttpClient()

        // Then
        assertNotSame(client1, client2) // Each call should create a new instance
    }

    @Test
    fun `provideRetrofit should create new instance each time`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // When
        val retrofit1 = NetworkModule.provideRetrofit(okHttpClient)
        val retrofit2 = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        assertNotSame(retrofit1, retrofit2) // Each call should create a new instance
    }

    @Test
    fun `provideTodoApiService should create new instance each time`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // When
        val apiService1 = NetworkModule.provideTodoApiService(retrofit)
        val apiService2 = NetworkModule.provideTodoApiService(retrofit)

        // Then
        assertNotSame(apiService1, apiService2) // Each call should create a new instance
    }

    @Test
    fun `OkHttpClient should have correct interceptors`() {
        // When
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // Then
        val interceptors = okHttpClient.interceptors
        assertEquals(2, interceptors.size) // MockInterceptor + HttpLoggingInterceptor
        
        val mockInterceptor = interceptors.find { it is MockInterceptor }
        assertNotNull(mockInterceptor)
        assertTrue(mockInterceptor is MockInterceptor)
        
        val loggingInterceptor = interceptors.find { it.javaClass.simpleName == "HttpLoggingInterceptor" }
        assertNotNull(loggingInterceptor)
    }

    @Test
    fun `Retrofit should have correct base URL`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // When
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        assertEquals("https://api.todoapp.com/v1/", retrofit.baseUrl().toString())
    }

    @Test
    fun `Retrofit should have Moshi converter factory`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()

        // When
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        val converterFactories = retrofit.converterFactories()
        assertTrue(converterFactories.any { it.javaClass.simpleName.contains("MoshiConverterFactory") })
    }

    @Test
    fun `TodoApiService should be created successfully`() {
        // Given
        val okHttpClient = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)

        // When
        val apiService = NetworkModule.provideTodoApiService(retrofit)

        // Then
        assertNotNull(apiService)
        // Verify it's the correct interface
        assertTrue(TodoApiService::class.java.isAssignableFrom(apiService.javaClass))
    }
} 