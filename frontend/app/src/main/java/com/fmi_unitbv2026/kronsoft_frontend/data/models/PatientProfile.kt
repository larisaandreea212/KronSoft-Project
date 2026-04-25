package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class PatientProfile (
    @SerializedName("idPacient")
    val idPacient: Int,

    @SerializedName("CNP")
    val CNP: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("sex")
    val sex: String,

    @SerializedName("surgeryDate")
    val surgeryDate: String,
)