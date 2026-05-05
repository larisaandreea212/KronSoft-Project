package com.fmi_unitbv2026.kronsoft_frontend.data.network

import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/doctor/{idDoctor}")
    suspend fun getDoctorInfo(@Path("idDoctor") idDoctor: Int): Doctor

    @GET("api/patient/all/{idDoctor}")
    suspend fun getAllPatientsForDoctor(@Path("idDoctor") idDoctor: Int): List<PatientCard>

    @GET("api/patient/critical/{idDoctor}")
    suspend fun getCriticalPatients(@Path("idDoctor") idDoctor: Int): List<PatientCard>

    @GET("api/patient/search")
    suspend fun searchPatients(
        @Query("idDoctor") idDoctor: Int,
        @Query("name") name: String
    ): List<PatientCard>

    @GET("api/patient/profile/{idPatient}")
    suspend fun getPatientProfile(@Path("idPatient") idPatient: Int): PatientProfile

    @GET("api/report/evolution/{IdPatient}")
    suspend fun getEvolutionData(@Path("IdPatient") idPatient: Int): List<Evolution>

    @GET("api/report/summary/{IdPatient}")
    suspend fun getPatientSummary(@Path("IdPatient") idPatient: Int): PatientSummary
}