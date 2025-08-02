package com.mobizonetech.todo.presentation.auth

import com.mobizonetech.todo.domain.repository.AuthRepository
import com.mobizonetech.todo.navigation.NavRoutes
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthStateManagerTest {

    private lateinit var authStateManager: AuthStateManager
    private lateinit var authRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        authStateManager = AuthStateManager(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        
        val initialState = authStateManager.authState.value
        assertEquals(AuthState.Loading, initialState)
    }

    @Test
    fun `when user is logged in, state should be authenticated`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        assertEquals(AuthState.Authenticated, state)
    }

    @Test
    fun `when user is not logged in, state should be unauthenticated`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(false)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `getStartDestination should return Main route when authenticated`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val destination = authStateManager.getStartDestination()
        assertEquals(NavRoutes.Main.route, destination)
    }

    @Test
    fun `getStartDestination should return Login route when unauthenticated`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(false)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val destination = authStateManager.getStartDestination()
        assertEquals(NavRoutes.Login.route, destination)
    }

    @Test
    fun `getStartDestination should return Login route when loading`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        
        // Don't advance the dispatcher to keep it in loading state
        val destination = authStateManager.getStartDestination()
        assertEquals(NavRoutes.Login.route, destination)
    }

    @Test
    fun `state should update when auth status changes`() = runTest {
        val authFlow = flowOf(false, true, false)
        coEvery { authRepository.isLoggedIn() } returns authFlow
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `multiple state changes should be handled correctly`() = runTest {
        val authFlow = flowOf(true, false, true, false)
        coEvery { authRepository.isLoggedIn() } returns authFlow
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `repository error should be handled gracefully`() = runTest {
        coEvery { authRepository.isLoggedIn() } throws Exception("Network error")
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        // Should default to unauthenticated on error
        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `navigation destination should be consistent with auth state`() = runTest {
        // Test authenticated state
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val authenticatedDestination = authStateManager.getStartDestination()
        assertEquals(NavRoutes.Main.route, authenticatedDestination)
        
        // Test unauthenticated state
        coEvery { authRepository.isLoggedIn() } returns flowOf(false)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val unauthenticatedDestination = authStateManager.getStartDestination()
        assertEquals(NavRoutes.Login.route, unauthenticatedDestination)
    }

    @Test
    fun `auth state should be observable`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(false)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = authStateManager.authState.value
        assertEquals(AuthState.Unauthenticated, state)
    }

    @Test
    fun `loading state should be transient`() = runTest {
        coEvery { authRepository.isLoggedIn() } returns flowOf(true)
        
        // Initial state should be loading
        val initialState = authStateManager.authState.value
        assertEquals(AuthState.Loading, initialState)
        
        // After processing, should be authenticated
        testDispatcher.scheduler.advanceUntilIdle()
        val finalState = authStateManager.authState.value
        assertEquals(AuthState.Authenticated, finalState)
    }
} 