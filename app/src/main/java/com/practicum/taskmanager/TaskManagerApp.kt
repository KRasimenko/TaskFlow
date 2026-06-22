package com.practicum.taskmanager

import android.app.Application
import com.practicum.taskmanager.data.TaskRepository

class TaskManagerApp : Application() {
    lateinit var repository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = TaskRepository.getInstance(this)
    }
}
