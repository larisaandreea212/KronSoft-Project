package com.fmi_unitbv2026.kronsoft_frontend.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("idUser")
    val idUser: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("firebaseUid")
    val firebaseUid: String,

    // Aceasta este linia care îi lipsește lui Kotlin ca să recunoască 'role'
    @SerializedName("role")
    val role: String
)