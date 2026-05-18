package com.fmi_unitbv2026.kronsoft_frontend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatComponent(
    messages: List<Message>,
    isDoctorView: Boolean = false,
    onSendMessage: (String) -> Unit
) {
    val deepNavy = Color(0xFF001F3F)
    val electricBlue = Color(0xFF00E5FF)
    val royalBlue = Color(0xFF2979FF)
    val accentPurple = Color(0xFF651FFF)

    val borderGradient = Brush.linearGradient(listOf(electricBlue, accentPurple))
    val doctorBrush = Brush.linearGradient(listOf(royalBlue, accentPurple))

    var newMessage by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = deepNavy),
        border = BorderStroke(2.dp, borderGradient),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubbleVibrant(
                        message = msg,
                        isDoctorView = isDoctorView,
                        doctorBrush = doctorBrush,
                        patientColor = Color(0xFFE0E0E0)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Scrie mesaj...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (newMessage.isNotBlank()) {
                            onSendMessage(newMessage)
                            newMessage = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(doctorBrush, CircleShape)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun ChatBubbleVibrant(message: Message, isDoctorView: Boolean, doctorBrush: Brush, patientColor: Color) {
    val isMyMessage = if (isDoctorView) message.isFromDoctor else !message.isFromDoctor
    val alignment = if (isMyMessage) Alignment.End else Alignment.Start

    val bubbleBrush = if (message.isFromDoctor) doctorBrush else SolidColor(patientColor)
    val textColor = if (message.isFromDoctor) Color.White else Color(0xFF000C18)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .background(
                    brush = bubbleBrush,
                    shape = RoundedCornerShape(
                        topStart = 20.dp, topEnd = 20.dp,
                        bottomStart = if (alignment == Alignment.End) 20.dp else 4.dp,
                        bottomEnd = if (alignment == Alignment.End) 4.dp else 20.dp
                    )
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun FinalVibrantPreview() {
    val mock = listOf(
        Message(text = "Bună ziua! Am primit rezultatele.", isFromDoctor = false),
        Message(text = "Perfect. Le analizăm în câteva minute.", isFromDoctor = true)
    )

    ChatComponent(
        messages = mock,
        isDoctorView = false,
        onSendMessage = { text -> println("Preview send: $text") }
    )
}