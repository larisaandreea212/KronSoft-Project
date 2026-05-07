package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class CreateDoctor(
    @SerializedName("email")
    val email: String,

    @SerializedName("firebaseUid")
    val firebaseUid: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("specialization")
    val specialization: String,

    @SerializedName("hospitalName")
    val hospitalName: String
)