package com.mobizonetech.todo.domain.models

import java.time.LocalDateTime

data class Category(
    val id: String,
    val name: String,
    val color: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
) 