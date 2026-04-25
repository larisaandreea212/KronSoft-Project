package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class PatientCard (
    @SerializedName("idPatient")
    val idPatient: Long,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("surgeryType")
    val surgeryType: String,

    @SerializedName("status")
    val status: PatientStatus
)