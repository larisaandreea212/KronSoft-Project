package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

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

    @SerializedName("surgeryType")
    val surgeryType: String,

    @SerializedName("surgeryDate")
    val surgeryDate: String,

    @SerializedName("idDoctor")
    val idDoctor: Long
)