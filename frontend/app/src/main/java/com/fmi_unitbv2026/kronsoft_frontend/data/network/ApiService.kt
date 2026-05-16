package com.fmi_unitbv2026.kronsoft_frontend.data.network

import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Evolution
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientCard
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientProfile
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientSummary
import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Body
import retrofit2.http.DELETE

interface ApiService {

    @GET("api/auth/check-email")
    suspend fun checkEmailExists(@Query("email") email: String): Boolean

    @POST("api/auth/link-uid")
    suspend fun linkFirebaseUid(
        @Query("email") email: String,
        @Query("uid") uid: String
    ): Response<Unit>

    @GET("api/users/auth/{uid}")
    suspend fun getUserByFirebaseUid(@Path("uid") uid: String): User

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

    @GET("api/report/evolution/{idPatient}")
    suspend fun getEvolutionData(@Path("idPatient") idPatient: Int): List<Evolution>

    @GET("api/report/summary/{idPatient}")
    suspend fun getPatientSummary(@Path("idPatient") idPatient: Int): PatientSummary

    @POST("api/doctor")
    suspend fun createDoctor(@Body doctor: CreateDoctor): Response<Doctor>

    @PATCH("api/doctor/deactivate")
    suspend fun deactivateDoctor(@Body deactivateDto: DeactivateDoctor): Response<Unit>

    @GET("api/doctor/active")
    suspend fun getActiveDoctors(): List<Doctor>

    @GET("api/doctor/inactive")
    suspend fun getInactiveDoctors(): List<Doctor>

    @POST("api/patient/create")
    suspend fun createPatient(@Body patient: CreatePatient): Response<PatientProfile>

    @DELETE("api/patient/{idPatient}")
    suspend fun deletePatient(@Path("idPatient") idPatient: Int): Response<Unit>

    @GET("api/doctor/user/{userId}")
    suspend fun getDoctorByUserId(@Path("userId") userId: Int): Doctor

    @GET("api/patient/by-user/{idUser}")
    suspend fun getPatientProfileByUserId(@Path("idUser") idUser: Int): PatientProfile

    @GET("api/questions")
    suspend fun loadQuestions(): List<Questions>

    @GET("api/ai-reports/can-complete/{idPatient}")
    suspend fun checkDailyCompletion(@Path("idPatient") idPatient: Int): Response<Boolean>

    @POST("api/ai-reports/submit")
    suspend fun submitDailyQuestionnaire(@Body submission: QuestionnaireSubmission): Response<AIReportResponse>
}
