package com.practicum.taskmanager.data.remote

import com.practicum.taskmanager.data.remote.dto.AuthResponseDto
import com.practicum.taskmanager.data.remote.dto.LoginRequest
import com.practicum.taskmanager.data.remote.dto.RegisterRequest
import com.practicum.taskmanager.data.remote.dto.TaskDto
import com.practicum.taskmanager.data.remote.dto.TaskRequestDto
import com.practicum.taskmanager.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskFlowApiService {

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponseDto

    @GET("api/users/me")
    suspend fun me(): UserDto

    @GET("api/tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("api/tasks/starred")
    suspend fun getStarredTasks(): List<TaskDto>

    @GET("api/tasks/{id}")
    suspend fun getTask(@Path("id") id: Long): TaskDto

    @POST("api/tasks")
    suspend fun createTask(@Body body: TaskRequestDto): TaskDto

    @PUT("api/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body body: TaskRequestDto): TaskDto

    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long)

    @PATCH("api/tasks/{id}/star")
    suspend fun toggleStar(@Path("id") id: Long): TaskDto
}
