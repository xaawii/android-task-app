package com.example.taskapp.core.di

import com.example.taskapp.BuildConfig
import com.example.taskapp.task.data.remote.api.TaskApiClient
import com.example.taskapp.task.data.remote.interceptors.AccessTokenInterceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient().newBuilder().apply {
                addInterceptor(AccessTokenInterceptor())
            }.build())
            .build()
    }


    @Singleton
    @Provides
    fun provideTaskApiClient(retrofit: Retrofit): TaskApiClient {
        return retrofit.create(TaskApiClient::class.java)
    }

}