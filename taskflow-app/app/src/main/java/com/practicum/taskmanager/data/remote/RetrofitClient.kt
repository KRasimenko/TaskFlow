package com.practicum.taskmanager.data.remote

import android.content.Context
import com.google.gson.GsonBuilder
import com.practicum.taskmanager.util.ApiConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var tokenManager: TokenManager? = null

    fun init(context: Context) {
        val appContext = context.applicationContext
        if (tokenManager == null) {
            synchronized(this) {
                if (tokenManager == null) {
                    tokenManager = TokenManager(appContext)
                }
            }
        }
        if (retrofit == null) {
            synchronized(this) {
                if (retrofit == null) {
                    retrofit = buildRetrofit(appContext)
                }
            }
        }
    }

    fun getTokenManager(context: Context): TokenManager {
        init(context)
        return tokenManager!!
    }

    fun makeApiService(context: Context): TaskFlowApiService {
        init(context)
        return retrofit!!.create(TaskFlowApiService::class.java)
    }

    private fun buildRetrofit(context: Context): Retrofit {
        val manager = TokenManager(context)
        tokenManager = manager

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(manager))
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().serializeNulls().create()

        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
