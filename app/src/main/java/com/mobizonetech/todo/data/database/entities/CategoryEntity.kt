package com.mobizonetech.todo.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) 