package com.fmi_unitbv2026.kronsoft_frontend.data.network

import retrofit2.http.GET

interface ApiService {
    // Exemplu de endpoint. Îl poți modifica ulterior în funcție de backend-ul tău.
    @GET("health")
    suspend fun checkHealth(): String
}
