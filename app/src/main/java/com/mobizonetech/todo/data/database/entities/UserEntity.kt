package com.mobizonetech.todo.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val phoneNumber: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) 