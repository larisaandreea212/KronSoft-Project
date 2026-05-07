package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class CreatePatient(
    @SerializedName("email")
    val email: String,

    @SerializedName("firebaseUid")
    val firebaseUid: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("sex")
    val sex: String,

    @SerializedName("cnp")
    val cnp: String,

    @SerializedName("idDoctor")
    val idDoctor: Int
)