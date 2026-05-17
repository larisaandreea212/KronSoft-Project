package com.fmi_unitbv2026.kronsoft_frontend.data.models

import com.google.firebase.firestore.PropertyName

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),

    // Forțăm Firebase să mapeze corect din clasa Java/Kotlin în documentul Firestore
    @get:PropertyName("fromDoctor")
    @set:PropertyName("fromDoctor")
    var isFromDoctor: Boolean = false
)