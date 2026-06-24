package com.practicum.taskmanager.data.mapper

import com.practicum.taskmanager.data.Task
import com.practicum.taskmanager.data.TaskPriority
import com.practicum.taskmanager.data.TaskStatus
import com.practicum.taskmanager.data.User
import com.practicum.taskmanager.data.remote.dto.TaskDto
import com.practicum.taskmanager.data.remote.dto.TaskRequestDto
import com.practicum.taskmanager.data.remote.dto.UserDto
import com.practicum.taskmanager.util.DateUtils

object TaskMapper {

    fun toDomain(dto: TaskDto): Task = Task(
        id = dto.id.toString(),
        title = dto.title,
        description = dto.description.orEmpty(),
        category = dto.category.orEmpty().ifBlank { "Работа" },
        priority = parsePriority(dto.priority),
        status = parseStatus(dto.status),
        dueDate = dto.dueDate,
        starred = dto.starred,
        createdAt = dto.createdAt ?: DateUtils.todayIso(),
    )

    fun toRequest(task: Task): TaskRequestDto = TaskRequestDto(
        title = task.title,
        description = task.description.ifBlank { null },
        category = task.category.ifBlank { null },
        priority = task.priority.name,
        status = task.status.name,
        dueDate = task.dueDate,
        starred = task.starred,
    )

    fun toRequest(
        title: String,
        description: String,
        category: String,
        priority: TaskPriority,
        status: TaskStatus,
        dueDate: String,
        starred: Boolean,
    ): TaskRequestDto = TaskRequestDto(
        title = title,
        description = description.ifBlank { null },
        category = category.ifBlank { null },
        priority = priority.name,
        status = status.name,
        dueDate = dueDate,
        starred = starred,
    )

    fun toUser(dto: UserDto): User = User(
        name = dto.name?.takeIf { it.isNotBlank() }
            ?: dto.email.substringBefore("@").ifBlank { "Пользователь" },
        email = dto.email,
    )

    private fun parsePriority(value: String): TaskPriority =
        runCatching { TaskPriority.valueOf(value.uppercase()) }.getOrDefault(TaskPriority.MEDIUM)

    private fun parseStatus(value: String): TaskStatus =
        runCatching { TaskStatus.valueOf(value.uppercase()) }.getOrDefault(TaskStatus.TODO)
}
