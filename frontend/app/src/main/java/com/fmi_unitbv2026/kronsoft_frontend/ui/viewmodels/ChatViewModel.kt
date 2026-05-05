package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels;

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

    fun sendMessage(senderId: String, receiverId: String, text: String) {
        val message = Message(senderId, receiverId, text)

        messagesCollection.add(message)
            .addOnSuccessListener {
                println("Message sent succesfully to Firestore!")
            }
            .addOnFailureListener { e ->
                println("Error: ${e.message}")
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
}