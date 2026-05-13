package com.fmi_unitbv2026.kronsoft_frontend.data.models

import com.google.gson.annotations.SerializedName

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFromDoctor: Boolean = false
)