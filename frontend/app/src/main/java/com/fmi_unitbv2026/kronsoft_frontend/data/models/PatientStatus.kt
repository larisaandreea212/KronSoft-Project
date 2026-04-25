package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName
enum class PatientStatus {
    @SerializedName("CRITICAL") CRITICAL,
    @SerializedName("STABLE") STABLE,
    @SerializedName("MODERATE") MODERATE
}