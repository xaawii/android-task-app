package com.example.taskapp.task.data.remote.interceptors

import com.example.taskapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class AccessTokenInterceptor : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = BuildConfig.USER_TOKEN
        val request = chain.request().newBuilder()
        request.addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
        return chain.proceed(request.build())
    }
}