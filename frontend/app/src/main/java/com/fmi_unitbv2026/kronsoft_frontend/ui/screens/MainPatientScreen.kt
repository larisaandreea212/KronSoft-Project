package com.fmi_unitbv2026.kronsoft_frontend.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.ChatComponentVibrant

@Composable
fun MainPatientScreen(
    patient: PatientCard,
    patientProfile: PatientProfile,
    canComplete: Boolean,
    questions: List<Questions>,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onLogout: () -> Unit
) {
    var isQuestionnaireActive by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, String>() }

    val deepNavy = Color(0xFF001220)
    val panelColor = Color(0xFF011A2E)
    val electricBlue = Color(0xFF00E5FF)
    val vibrantGradient = Brush.linearGradient(listOf(electricBlue, Color(0xFF2979FF)))

    Row(modifier = Modifier.fillMaxSize().background(deepNavy)) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("PATIENT PROFILE", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                ProfileDataField("NAME", "${patient.firstName} ${patient.lastName}")
                ProfileDataField("AGE", "${patientProfile.age} years")
                ProfileDataField("GENDER", patientProfile.sex)
                ProfileDataField("SURGERY", patient.surgeryType)
                ProfileDataField("STATUS", patient.status.toString())
                Spacer(modifier = Modifier.height(40.dp))
                SidebarNavItem(Icons.Default.Dashboard, "Dashboard", isSelected = true)
            }
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onLogout() }.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Logout, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sign Out", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (!isQuestionnaireActive) {
                Column(modifier = Modifier.fillMaxWidth().align(Alignment.Start)) {
                    Text("Welcome back, ${patient.firstName}!", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                    Text("Recovery Plan: ${patient.surgeryType}", color = electricBlue.copy(alpha = 0.7f), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(60.dp))
                DesktopQuestionnaireCard(canComplete = canComplete, gradient = vibrantGradient) { isQuestionnaireActive = true }
            } else {
                ActiveQuestionnaireView(
                    questions = questions,
                    currentIndex = currentQuestionIndex,
                    userAnswers = userAnswers,
                    onAnswerSelected = { qId, answer -> userAnswers[qId] = answer },
                    onNext = { if (currentQuestionIndex < questions.size - 1) currentQuestionIndex++ },
                    onBack = { if (currentQuestionIndex > 0) currentQuestionIndex-- },
                    onFinish = { isQuestionnaireActive = false; currentQuestionIndex = 0; userAnswers.clear() }
                )
            }
        }

        Column(
            modifier = Modifier
                .width(380.dp)
                .fillMaxHeight()
                .background(panelColor)
                .padding(top = 32.dp)
        ) {
            Text(
                "LAST EVALUATION",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                val scoreProgress = 0.75f
                CircularProgressIndicator(
                    progress = { scoreProgress },
                    modifier = Modifier.size(140.dp),
                    color = if (scoreProgress > 0.7f) Color(0xFF00E676) else electricBlue,
                    strokeWidth = 12.dp,
                    trackColor = Color.White.copy(alpha = 0.05f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Text(
                    text = "${(scoreProgress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Card(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, "AI", tint = electricBlue, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("AI INSIGHTS", color = electricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your recovery is ahead of schedule. Pain levels have stabilized in the last 48h. Keep up the mobility exercises.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ChatComponentVibrant(messages = messages, onSendMessage = onSendMessage)
            }
        }
    }
}

@Composable
fun ActiveQuestionnaireView(
    questions: List<Questions>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val currentQuestion = questions[currentIndex]
    val selectedAnswer = userAnswers[currentQuestion.idQuestion]
    val electricBlue = Color(0xFF00E5FF)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(550.dp)) {
        Text("Question ${currentIndex + 1} of ${questions.size}", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / questions.size },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = electricBlue,
            trackColor = Color.White.copy(alpha = 0.05f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = currentQuestion.questionText,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 42.sp
        )

        Spacer(modifier = Modifier.height(60.dp))

        if (currentQuestion.responseType == ResponseType.YES_NO) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                SelectableOptionButton("YES", isSelected = selectedAnswer == "YES") { onAnswerSelected(currentQuestion.idQuestion, "YES") }
                SelectableOptionButton("NO", isSelected = selectedAnswer == "NO") { onAnswerSelected(currentQuestion.idQuestion, "NO") }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                (1..5).forEach { num ->
                    val sNum = num.toString()
                    val isSelected = selectedAnswer == sNum
                    OutlinedButton(
                        onClick = { onAnswerSelected(currentQuestion.idQuestion, sNum) },
                        shape = CircleShape,
                        modifier = Modifier.size(60.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isSelected) electricBlue else Color.Transparent),
                        border = BorderStroke(2.dp, if (isSelected) electricBlue else Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(sNum, color = if (isSelected) Color.Black else Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (currentIndex > 0) {
                TextButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("BACK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else Spacer(Modifier.width(100.dp))

            Button(
                onClick = if (currentIndex == questions.size - 1) onFinish else onNext,
                enabled = selectedAnswer != null,
                modifier = Modifier.height(56.dp).width(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentIndex == questions.size - 1) Color(0xFF00E676) else electricBlue,
                    disabledContainerColor = Color.White.copy(alpha = 0.05f)
                )
            ) {
                Text(
                    if (currentIndex == questions.size - 1) "COMPLETE" else "NEXT",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun SelectableOptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(150.dp).height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) Color(0xFF2979FF) else Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(14.dp),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF00E5FF)) else null
    ) { Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White) }
}

@Composable
fun ProfileDataField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(label, color = Color(0xFF00E5FF).copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Black)
        Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SidebarNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().background(if (isSelected) Color.White.copy(alpha = 0.05f) else Color.Transparent, RoundedCornerShape(10.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = if (isSelected) Color(0xFF00E5FF) else Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(label, color = if (isSelected) Color.White else Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun DesktopQuestionnaireCard(canComplete: Boolean, gradient: Brush, onStart: () -> Unit) {
    Card(
        modifier = Modifier.width(500.dp).height(260.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {  if (canComplete) {
                Text("Daily Evaluation", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(24.dp))
                Button(onClick = onStart, modifier = Modifier.width(220.dp).height(56.dp), contentPadding = PaddingValues(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                    Box(Modifier.fillMaxSize().background(gradient), Alignment.Center) { Text("START NOW", color = Color.White, fontWeight = FontWeight.Black) }
                }
            } else {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF00E676), modifier = Modifier.size(60.dp))
                Text("Everything updated!", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1440, heightDp = 900)
@Composable
fun MainPatientDesktopPreview() {
    val mockPatient = PatientCard(
        idPatient = "1",
        firstName = "Alex",
        lastName = "Morgan",
        surgeryType = "ACL Reconstruction",
        status = PatientStatus.STABLE
    )

    val mockProfile = PatientProfile(
        1,
        "1920815123456",
        32,
        "Male",
        "2024-05-10"
    )

    val mockQuestions = listOf(
        Questions(1, "How is your pain level today?", ResponseType.SCALE_1_5),
        Questions(2, "Have you performed your morning exercises?", ResponseType.YES_NO)
    )

    val mockMessages = listOf(
        Message("1", "1", "Bună ziua! Cum vă simțiți astăzi?"),)

    Surface {
        MainPatientScreen(
            patient = mockPatient,
            patientProfile = mockProfile,
            canComplete = true,
            questions = mockQuestions,
            messages = mockMessages,
            onSendMessage = {},
            onLogout = {}
        )
    }
}