package com.practicum.taskmanager.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.taskmanager.data.mapper.TaskMapper
import com.practicum.taskmanager.data.remote.RetrofitClient
import com.practicum.taskmanager.data.remote.TaskFlowApiService
import com.practicum.taskmanager.data.remote.TokenManager
import com.practicum.taskmanager.data.remote.dto.LoginRequest
import com.practicum.taskmanager.data.remote.dto.RegisterRequest
import com.practicum.taskmanager.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class TaskRepository(context: Context) {

    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val api: TaskFlowApiService = RetrofitClient.makeApiService(appContext)
    private val tokenManager: TokenManager = RetrofitClient.getTokenManager(appContext)

    private var tasks: MutableList<Task> = loadTasks()
    private var user: User? = loadUser()

    val currentUser: User? get() = user
    val isOnlineMode: Boolean get() = tokenManager.hasToken()

    fun getTasks(): List<Task> = tasks.toList()

    fun getStarredTasks(): List<Task> = tasks.filter { it.starred }

    fun getTasksForDate(isoDate: String): List<Task> = tasks.filter { it.dueDate == isoDate }

    fun loginLocal(email: String, name: String? = null) {
        val resolvedName = name?.takeIf { it.isNotBlank() }
            ?: email.substringBefore("@").ifBlank { "Пользователь" }
        user = User(name = resolvedName, email = email)
        saveUser()
    }

    fun registerLocal(name: String, email: String) {
        val resolvedName = name.ifBlank { email.substringBefore("@").ifBlank { "Пользователь" } }
        user = User(name = resolvedName, email = email)
        saveUser()
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.login(LoginRequest(email, password))
            tokenManager.saveToken(response.token)
            user = TaskMapper.toUser(response.user)
            saveUser()
            runCatching { syncFromServer() }
        } catch (e: HttpException) {
            throw e
        } catch (_: IOException) {
            loginLocal(email)
        }
    }

    suspend fun register(name: String, email: String, password: String) = withContext(Dispatchers.IO) {
        val resolvedName = name.ifBlank { email.substringBefore("@").ifBlank { "Пользователь" } }
        runCatching {
            val response = api.register(RegisterRequest(email, password, resolvedName))
            tokenManager.saveToken(response.token)
            user = TaskMapper.toUser(response.user)
            saveUser()
            syncFromServer()
        }
    }

    suspend fun syncFromServer() = withContext(Dispatchers.IO) {
        if (!tokenManager.hasToken()) return@withContext
        tasks = api.getTasks().map { TaskMapper.toDomain(it) }.toMutableList()
        saveTasks()
    }

    fun isLoggedIn(): Boolean = user != null

    fun logout() {
        user = null
        tokenManager.clear()
        prefs.edit().remove(KEY_USER).apply()
    }

    suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        if (tokenManager.hasToken()) {
            runCatching {
                val created = api.createTask(TaskMapper.toRequest(task))
                tasks.add(0, TaskMapper.toDomain(created))
            }.onFailure {
                tasks.add(0, task)
            }
        } else {
            tasks.add(0, task)
        }
        saveTasks()
    }

    suspend fun updateTask(id: String, patch: (Task) -> Task) = withContext(Dispatchers.IO) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index < 0) return@withContext
        val updated = patch(tasks[index])
        if (tokenManager.hasToken() && id.isRemoteId()) {
            runCatching {
                val remote = api.updateTask(id.toLong(), TaskMapper.toRequest(updated))
                tasks[index] = TaskMapper.toDomain(remote)
            }.onFailure {
                tasks[index] = updated
            }
        } else {
            tasks[index] = updated
        }
        saveTasks()
    }

    suspend fun deleteTask(id: String) = withContext(Dispatchers.IO) {
        if (tokenManager.hasToken() && id.isRemoteId()) {
            runCatching { api.deleteTask(id.toLong()) }
        }
        tasks.removeAll { it.id == id }
        saveTasks()
    }

    fun getTask(id: String): Task? = tasks.find { it.id == id }

    suspend fun toggleStar(id: String) = withContext(Dispatchers.IO) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index < 0) return@withContext
        if (tokenManager.hasToken() && id.isRemoteId()) {
            runCatching {
                val remote = api.toggleStar(id.toLong())
                tasks[index] = TaskMapper.toDomain(remote)
            }.onFailure {
                tasks[index] = tasks[index].copy(starred = !tasks[index].starred)
            }
        } else {
            tasks[index] = tasks[index].copy(starred = !tasks[index].starred)
        }
        saveTasks()
    }

    suspend fun setStatus(id: String, status: TaskStatus) {
        updateTask(id) { it.copy(status = status) }
    }

    suspend fun createTask(
        title: String,
        description: String,
        category: String,
        priority: TaskPriority,
        status: TaskStatus,
        dueDate: String,
        starred: Boolean = false,
    ): Task {
        val request = TaskMapper.toRequest(title, description, category, priority, status, dueDate, starred)
        return if (tokenManager.hasToken()) {
            val dto = withContext(Dispatchers.IO) { api.createTask(request) }
            val task = TaskMapper.toDomain(dto)
            tasks.add(0, task)
            saveTasks()
            task
        } else {
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
            tasks.add(0, task)
            saveTasks()
            task
        }
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

    private fun String.isRemoteId(): Boolean = toLongOrNull() != null

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
