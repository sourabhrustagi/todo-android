package com.mobizonetech.todo.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "feedback")
data class FeedbackEntity(
    @PrimaryKey
    val id: String,
    val rating: Int,
    val comment: String?,
    val category: String,
    val createdAt: LocalDateTime
) 