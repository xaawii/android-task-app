package com.example.taskapp.core.data.remote.interceptors

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AccessTokenInterceptor(private val dataStoreManager: DataStoreManager) : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val url = chain.request().url.toString()

        // No agregar token para login y registro
        if (url.contains("login") || url.contains("register")) {
            return chain.proceed(request.build())
        }

        val token = runBlocking {
            dataStoreManager.getToken()
        }

        request.addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
        return chain.proceed(request.build())
    }
}