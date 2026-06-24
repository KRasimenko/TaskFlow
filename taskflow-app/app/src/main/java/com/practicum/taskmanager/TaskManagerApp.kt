package com.practicum.taskmanager

import android.app.Application
import com.practicum.taskmanager.data.TaskRepository
import com.practicum.taskmanager.data.remote.RetrofitClient

class TaskManagerApp : Application() {
    lateinit var repository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(this)
        repository = TaskRepository.getInstance(this)
    }
}
