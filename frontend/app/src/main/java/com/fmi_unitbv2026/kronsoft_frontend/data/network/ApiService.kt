package com.fmi_unitbv2026.kronsoft_frontend.data.network

import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientProfile
import retrofit2.http.GET

interface ApiService {
    @GET("health")
    suspend fun checkHealth(): String

    @GET("patient/profile")
    suspend fun getPatientData(): PatientProfile
}
