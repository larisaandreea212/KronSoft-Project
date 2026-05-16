package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class Questions(
    @SerializedName("idQuestions")
    val idQuestion: Int,

    @SerializedName("questionText")
    val questionText: String,

    @SerializedName("responseType")
    val responseType: ResponseType
)