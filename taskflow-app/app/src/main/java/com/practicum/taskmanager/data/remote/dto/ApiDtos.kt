package com.practicum.taskmanager.data.remote.dto

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String?,
)

data class AuthResponseDto(val token: String, val user: UserDto)

data class UserDto(val id: Long?, val email: String, val name: String?)

data class TaskDto(
    val id: Long,
    val title: String,
    val description: String?,
    val category: String?,
    val priority: String,
    val status: String,
    val dueDate: String,
    val starred: Boolean,
    val createdAt: String?,
)

data class TaskRequestDto(
    val title: String,
    val description: String?,
    val category: String?,
    val priority: String,
    val status: String,
    val dueDate: String,
    val starred: Boolean?,
)
