package com.example.taskapp.core.di

import com.example.taskapp.BuildConfig
import com.example.taskapp.core.data.local.datastore.DataStoreManager
import com.example.taskapp.core.utils.LocalDateTimeConverter
import com.example.taskapp.task.data.remote.api.TaskApiClient
import com.example.taskapp.core.data.remote.interceptors.AccessTokenInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter())
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, dataStoreManager: DataStoreManager): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient().newBuilder().apply {
                addInterceptor(AccessTokenInterceptor(dataStoreManager))
            }.build())
            .build()
    }


}