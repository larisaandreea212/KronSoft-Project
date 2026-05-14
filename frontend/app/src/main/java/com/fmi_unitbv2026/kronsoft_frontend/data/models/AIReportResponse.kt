package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class AIReportResponse(
    @SerializedName("aiScore")
    val aiScore: Int,

    @SerializedName("aiNote")
    val aiNote: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("date")
    val date: String
)