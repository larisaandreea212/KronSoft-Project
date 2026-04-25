package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class QuestionResponse (
    @SerializedName("questionText")
    val questionText: String,

    @SerializedName("answerText")
    val answerText: String,

    @SerializedName("responseType")
    val responseType: ResponseType
)