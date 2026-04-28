package com.fmi_unitbv2026.kronsoft_frontend.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.exemplu.com/" // Înlocuiește cu URL-ul tău de backend

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
