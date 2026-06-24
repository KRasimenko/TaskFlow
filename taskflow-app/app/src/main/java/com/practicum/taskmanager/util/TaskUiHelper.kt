package com.practicum.taskmanager.util

import android.content.Context
import com.practicum.taskmanager.R
import com.practicum.taskmanager.data.TaskPriority
import com.practicum.taskmanager.data.TaskStatus

object TaskUiHelper {

    fun priorityLabel(context: Context, priority: TaskPriority): String = when (priority) {
        TaskPriority.LOW -> context.getString(R.string.priority_low)
        TaskPriority.MEDIUM -> context.getString(R.string.priority_medium)
        TaskPriority.HIGH -> context.getString(R.string.priority_high)
    }

    fun statusLabel(context: Context, status: TaskStatus): String = when (status) {
        TaskStatus.TODO -> context.getString(R.string.status_todo)
        TaskStatus.IN_PROGRESS -> context.getString(R.string.status_in_progress)
        TaskStatus.DONE -> context.getString(R.string.status_done)
    }

    fun statusShortLabel(context: Context, status: TaskStatus): String = when (status) {
        TaskStatus.TODO -> context.getString(R.string.status_todo_short)
        TaskStatus.IN_PROGRESS -> context.getString(R.string.filter_in_progress)
        TaskStatus.DONE -> context.getString(R.string.filter_done)
    }
}
