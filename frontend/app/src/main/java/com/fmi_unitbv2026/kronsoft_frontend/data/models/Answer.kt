package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("idQuestion")
    val idQuestion: Int,

    @SerializedName("answerText")
    val answerText: String
)