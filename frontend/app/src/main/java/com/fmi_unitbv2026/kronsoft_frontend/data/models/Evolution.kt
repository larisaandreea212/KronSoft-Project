package com.fmi_unitbv2026.kronsoft_frontend.data.models
import com.google.gson.annotations.SerializedName

data class Evolution (
    @SerializedName("date") val dateTime: String,
    @SerializedName("aiScore") val score: Int
)