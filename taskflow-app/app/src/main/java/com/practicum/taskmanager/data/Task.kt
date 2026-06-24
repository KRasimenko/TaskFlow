package com.practicum.taskmanager.data

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val priority: TaskPriority,
    val status: TaskStatus,
    val dueDate: String,
    val starred: Boolean,
    val createdAt: String,
)

enum class TaskPriority { LOW, MEDIUM, HIGH }

enum class TaskStatus { TODO, IN_PROGRESS, DONE }

data class User(
    val name: String,
    val email: String,
)

data class AppNotification(
    val taskId: String,
    val title: String,
    val body: String,
    val date: String,
)
