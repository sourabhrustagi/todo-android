package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test

class UserTest {

    @Test
    fun `user should have all required properties`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When & Then
        assertEquals("user_123", user.id)
        assertEquals("+1234567890", user.phoneNumber)
        assertEquals("John Doe", user.name)
    }

    @Test
    fun `user should be equal to itself`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When & Then
        assertEquals(user, user)
    }

    @Test
    fun `users with same properties should be equal`() {
        // Given
        val user1 = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )
        val user2 = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When & Then
        assertEquals(user1, user2)
    }

    @Test
    fun `users with different properties should not be equal`() {
        // Given
        val user1 = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )
        val user2 = User(
            id = "user_456",
            phoneNumber = "+0987654321",
            name = "Jane Smith"
        )

        // When & Then
        assertNotEquals(user1, user2)
    }

    @Test
    fun `user should have correct hash code`() {
        // Given
        val user1 = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )
        val user2 = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When & Then
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `user should have meaningful string representation`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When
        val stringRepresentation = user.toString()

        // Then
        assertTrue(stringRepresentation.contains("user_123"))
        assertTrue(stringRepresentation.contains("+1234567890"))
        assertTrue(stringRepresentation.contains("John Doe"))
    }

    @Test
    fun `user should handle empty name`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = ""
        )

        // When & Then
        assertEquals("", user.name)
    }

    @Test
    fun `user should handle special characters in name`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "José María"
        )

        // When & Then
        assertEquals("José María", user.name)
    }

    @Test
    fun `user should handle different phone number formats`() {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+44 20 7946 0958",
            name = "John Doe"
        )

        // When & Then
        assertEquals("+44 20 7946 0958", user.phoneNumber)
    }

    @Test
    fun `user should handle long names`() {
        // Given
        val longName = "John Jacob Jingleheimer Schmidt"
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = longName
        )

        // When & Then
        assertEquals(longName, user.name)
    }

    @Test
    fun `user should handle special characters in id`() {
        // Given
        val user = User(
            id = "user-123_456",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        // When & Then
        assertEquals("user-123_456", user.id)
    }
} 