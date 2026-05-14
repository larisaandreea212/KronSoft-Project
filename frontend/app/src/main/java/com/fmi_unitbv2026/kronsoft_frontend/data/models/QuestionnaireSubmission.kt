package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class QuestionnaireSubmission(
    @SerializedName("idPatient")
    val idPatient: Int,

    @SerializedName("answers")
    val answers: List<Answer>
)