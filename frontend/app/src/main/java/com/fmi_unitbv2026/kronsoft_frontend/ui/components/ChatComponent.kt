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
fun ChatComponent(messages: List<Message>) {
    // Paleta de culori "Electric Navy"
    val deepNavy = Color(0xFF000C18)
    val electricBlue = Color(0xFF00E5FF)
    val royalBlue = Color(0xFF2979FF)
    val accentPurple = Color(0xFF651FFF)

    // Gradient pentru fundalul interior al chat-ului (Mesh effect)
    val meshGradient = Brush.radialGradient(
        colors = listOf(royalBlue.copy(alpha = 0.15f), Color.Transparent),
        radius = 500f
    )

    var newMessage by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = deepNavy),
        border = BorderStroke(2.dp, Brush.linearGradient(listOf(electricBlue, accentPurple))),
        elevation = CardDefaults.cardElevation(defaultElevation = 25.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(meshGradient)) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Zona de mesaje
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
                            doctorBrush = Brush.linearGradient(listOf(royalBlue, accentPurple)),
                            patientColor = Color(0xFFE0E0E0)
                        )
                    }
                }

                // Input Bar stilizat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newMessage,
                        onValueChange = { newMessage = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Mesaj nou...", color = Color.Gray, fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = { newMessage = "" },
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Brush.linearGradient(listOf(electricBlue, royalBlue)),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            tint = deepNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatComponentVibrant(messages: List<Message>) {
    val deepNavy = Color(0xFF000C18)
    val electricBlue = Color(0xFF00E5FF)
    val royalBlue = Color(0xFF2979FF)
    val accentPurple = Color(0xFF651FFF)

    // Gradient pentru bordura cardului
    val borderGradient = Brush.linearGradient(listOf(electricBlue, accentPurple))
    // Gradient pentru mesajele doctorului
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
            // Lista de mesaje
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
                        doctorBrush = doctorBrush,
                        patientColor = Color(0xFFE0E0E0)
                    )
                }
            }

            // Input bar
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
                    onClick = { newMessage = "" },
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
fun ChatBubbleVibrant(message: Message, doctorBrush: Brush, patientColor: Color) {
    val isDoctor = message.isFromDoctor
    val alignment = if (isDoctor) Alignment.End else Alignment.Start

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        // Folosim BOX cu Modifier.background pentru a evita eroarea de 'brush' din Surface
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .background(
                    brush = if (isDoctor) doctorBrush else SolidColor(patientColor),
                    shape = RoundedCornerShape(
                        topStart = 20.dp, topEnd = 20.dp,
                        bottomStart = if (isDoctor) 20.dp else 4.dp,
                        bottomEnd = if (isDoctor) 4.dp else 20.dp
                    )
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                color = if (isDoctor) Color.White else Color(0xFF000C18),
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
    ChatComponentVibrant(messages = mock)
}
