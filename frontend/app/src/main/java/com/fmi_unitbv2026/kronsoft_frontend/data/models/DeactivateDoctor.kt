package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class DeactivateDoctor(
    @SerializedName("idDoctorToDeactivate")
    val idOldDoctor: Int,

    @SerializedName("idDoctorToReceivePatient")
    val idNewDoctor: Int
)