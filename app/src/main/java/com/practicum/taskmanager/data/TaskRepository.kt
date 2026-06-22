package com.practicum.taskmanager.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.taskmanager.util.DateUtils
import java.util.UUID

class TaskRepository(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private var tasks: MutableList<Task> = loadTasks()
    private var user: User? = loadUser()

    val currentUser: User? get() = user
    fun getTasks(): List<Task> = tasks.toList()

    fun login(email: String, name: String? = null) {
        user = User(
            name = name?.takeIf { it.isNotBlank() } ?: email.substringBefore("@").ifBlank { "Пользователь" },
            email = email,
        )
        saveUser()
    }

    fun logout() {
        user = null
        prefs.edit().remove(KEY_USER).apply()
    }

    fun isLoggedIn(): Boolean = user != null

    fun addTask(task: Task) {
        tasks.add(0, task)
        saveTasks()
    }

    fun updateTask(id: String, patch: (Task) -> Task) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index >= 0) {
            tasks[index] = patch(tasks[index])
            saveTasks()
        }
    }

    fun deleteTask(id: String) {
        tasks.removeAll { it.id == id }
        saveTasks()
    }

    fun getTask(id: String): Task? = tasks.find { it.id == id }

    fun toggleStar(id: String) {
        updateTask(id) { it.copy(starred = !it.starred) }
    }

    fun setStatus(id: String, status: TaskStatus) {
        updateTask(id) { it.copy(status = status) }
    }

    fun createTask(
        title: String,
        description: String,
        category: String,
        priority: TaskPriority,
        status: TaskStatus,
        dueDate: String,
        starred: Boolean = false,
    ): Task {
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            category = category,
            priority = priority,
            status = status,
            dueDate = dueDate,
            starred = starred,
            createdAt = DateUtils.todayIso(),
        )
        addTask(task)
        return task
    }

    fun getNotifications(): List<AppNotification> {
        val today = DateUtils.todayIso()
        return tasks
            .filter { it.status != TaskStatus.DONE && it.dueDate <= today }
            .map { task ->
                AppNotification(
                    taskId = task.id,
                    title = if (task.dueDate < today) "Просрочено" else "Срок сегодня",
                    body = task.title,
                    date = DateUtils.formatDateRu(task.dueDate),
                )
            }
    }

    private fun loadTasks(): MutableList<Task> {
        val json = prefs.getString(KEY_TASKS, null) ?: return SeedData.tasks.toMutableList()
        return try {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            gson.fromJson(json, type) ?: SeedData.tasks.toMutableList()
        } catch (_: Exception) {
            SeedData.tasks.toMutableList()
        }
    }

    private fun loadUser(): User? {
        val json = prefs.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(json, User::class.java)
        } catch (_: Exception) {
            null
        }
    }

    private fun saveTasks() {
        prefs.edit().putString(KEY_TASKS, gson.toJson(tasks)).apply()
    }

    private fun saveUser() {
        val u = user ?: return
        prefs.edit().putString(KEY_USER, gson.toJson(u)).apply()
    }

    companion object {
        private const val PREFS_NAME = "taskflow_prefs"
        private const val KEY_TASKS = "tasks"
        private const val KEY_USER = "user"

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                instance ?: TaskRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}
