package com.fmi_unitbv2026.kronsoft_frontend.data.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("senderId")
    val senderId: String = "",

    @SerializedName("receiverId")
    val receiverId: String = "",

    @SerializedName("text")
    val text: String = "",

    @SerializedName("timestamp")
    val timestamp: Long = 0L,

    @SerializedName("isFromDoctor")
    val isFromDoctor: Boolean = false
)