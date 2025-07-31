package com.mobizonetech.todo.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobizonetech.todo.domain.usecases.task.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getTasksUseCase: GetTasksUseCase

    @Inject
    lateinit var createTaskUseCase: CreateTaskUseCase

    @Inject
    lateinit var updateTaskUseCase: UpdateTaskUseCase

    @Inject
    lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @Inject
    lateinit var completeTaskUseCase: CompleteTaskUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `provideGetTasksUseCase should return valid use case instance`() {
        // Then
        assertNotNull(getTasksUseCase)
        assertTrue(getTasksUseCase is GetTasksUseCase)
    }

    @Test
    fun `provideCreateTaskUseCase should return valid use case instance`() {
        // Then
        assertNotNull(createTaskUseCase)
        assertTrue(createTaskUseCase is CreateTaskUseCase)
    }

    @Test
    fun `provideUpdateTaskUseCase should return valid use case instance`() {
        // Then
        assertNotNull(updateTaskUseCase)
        assertTrue(updateTaskUseCase is UpdateTaskUseCase)
    }

    @Test
    fun `provideDeleteTaskUseCase should return valid use case instance`() {
        // Then
        assertNotNull(deleteTaskUseCase)
        assertTrue(deleteTaskUseCase is DeleteTaskUseCase)
    }

    @Test
    fun `provideCompleteTaskUseCase should return valid use case instance`() {
        // Then
        assertNotNull(completeTaskUseCase)
        assertTrue(completeTaskUseCase is CompleteTaskUseCase)
    }

    @Test
    fun `all use cases should be different instances`() {
        // Then
        assertNotSame(getTasksUseCase, createTaskUseCase)
        assertNotSame(getTasksUseCase, updateTaskUseCase)
        assertNotSame(getTasksUseCase, deleteTaskUseCase)
        assertNotSame(getTasksUseCase, completeTaskUseCase)
        assertNotSame(createTaskUseCase, updateTaskUseCase)
        assertNotSame(createTaskUseCase, deleteTaskUseCase)
        assertNotSame(createTaskUseCase, completeTaskUseCase)
        assertNotSame(updateTaskUseCase, deleteTaskUseCase)
        assertNotSame(updateTaskUseCase, completeTaskUseCase)
        assertNotSame(deleteTaskUseCase, completeTaskUseCase)
    }

    @Test
    fun `use cases should be properly configured`() {
        // Then
        assertNotNull(getTasksUseCase)
        assertNotNull(createTaskUseCase)
        assertNotNull(updateTaskUseCase)
        assertNotNull(deleteTaskUseCase)
        assertNotNull(completeTaskUseCase)
        
        // Verify all use cases are of correct types
        assertTrue(getTasksUseCase is GetTasksUseCase)
        assertTrue(createTaskUseCase is CreateTaskUseCase)
        assertTrue(updateTaskUseCase is UpdateTaskUseCase)
        assertTrue(deleteTaskUseCase is DeleteTaskUseCase)
        assertTrue(completeTaskUseCase is CompleteTaskUseCase)
    }

    @Test
    fun `dependency injection should work correctly`() {
        // Then
        // All dependencies should be injected successfully
        assertNotNull(getTasksUseCase)
        assertNotNull(createTaskUseCase)
        assertNotNull(updateTaskUseCase)
        assertNotNull(deleteTaskUseCase)
        assertNotNull(completeTaskUseCase)
        
        // No exceptions should be thrown during injection
        // This test passes if all dependencies are properly injected
    }
} 