package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val messagesCollection = db.collection("messages")

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Am lăsat exact cei 3 parametri originali (senderId, receiverId, text) ca să nu mai dea erori în ecrane!
    fun sendMessage(senderId: String, receiverId: String, text: String) {
        if (text.isBlank()) return

        // Logica automată: dacă cel care trimite are ID-ul de doctor (ex: nu e egal cu ID-ul pacientului din sesiune)
        // O metodă simplă: dacă receiverId este "1" (doctorul implicit), înseamnă că senderId este PACIENTUL (deci isFromDoctor = false)
        val isDoctor = (receiverId != "1")

        val message = Message(
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis(),
            isFromDoctor = isDoctor
        )

        println("DEBUG: Trimit în Firestore: $text | Sender: $senderId | IsFromDoctor: $isDoctor")

        messagesCollection.add(message)
            .addOnSuccessListener {
                println("DEBUG: SUCCES! Mesajul a ajuns în Firestore.")
            }
            .addOnFailureListener { e ->
                println("DEBUG: EROARE Firebase: ${e.message}")
            }
    }

    fun listenForMessages(doctorId: String, patientId: String) {
        messagesCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error listening Firestore: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val chatHistory = mutableListOf<Message>()

                    for (document in snapshot.documents) {
                        val msg = document.toObject(Message::class.java)

                        if (msg != null) {
                            if ((msg.senderId == doctorId && msg.receiverId == patientId) ||
                                (msg.senderId == patientId && msg.receiverId == doctorId)) {
                                chatHistory.add(msg)
                            }
                        }
                    }
                    _messages.value = chatHistory
                }
            }
    }

    fun clearMessages() {
        _messages.value = emptyList()
    }
}