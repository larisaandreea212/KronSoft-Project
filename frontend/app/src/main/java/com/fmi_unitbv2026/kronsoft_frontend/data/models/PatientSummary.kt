package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class PatientSummary (
    @SerializedName("aiScore")
    val aiScore: Int,

    @SerializedName("aiNote")
    val aiNote: String,

    @SerializedName("status")
    val status: PatientStatus,

    @SerializedName("questions")
    val questions: List<QuestionResponse>

)