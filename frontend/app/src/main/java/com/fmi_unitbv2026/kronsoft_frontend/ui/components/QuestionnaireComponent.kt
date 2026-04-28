package com.fmi_unitbv2026.kronsoft_frontend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.QuestionResponse
import com.fmi_unitbv2026.kronsoft_frontend.data.models.ResponseType

@Composable
fun QuestionnaireComponent(responses: List<QuestionResponse>) {
    val superLightBlue = Color(0xFFF0F7FF)
    val medicalBlue = Color(0xFF2196F3)
    val lightBorderBlue = Color(0xFFD1E4F5)
    val inactiveGray = Color(0xFFE0E0E0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = superLightBlue),
        border = BorderStroke(1.5.dp, lightBorderBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Latest Assessment Details",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF101828),
                modifier = Modifier.padding(bottom = 18.dp)
            )

            responses.forEachIndexed { index, response ->
                Column {
                    QuestionRow(response, medicalBlue, inactiveGray)

                    if (index < responses.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 14.dp),
                            thickness = 1.dp,
                            color = lightBorderBlue.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionRow(response: QuestionResponse, activeColor: Color, inactiveColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = response.questionText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF344054),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (response.responseType) {
                ResponseType.YES_NO -> {
                    val isYes = response.answerText.lowercase() in listOf("yes", "da")

                    ResponseOptionTab("YES", isSelected = isYes, activeColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    ResponseOptionTab("NO", isSelected = !isYes, activeColor)
                }
                ResponseType.SCALE_1_5 -> {
                    val score = response.answerText.toIntOrNull() ?: 0
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 1..5) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = if (i <= score) activeColor else inactiveColor,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResponseOptionTab(text: String, isSelected: Boolean, activeColor: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) activeColor else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFD0D5DD)),
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else Color(0xFF667085)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionnairePreview() {
    val mockData = listOf(
        QuestionResponse("Does the patient have known allergies?", "Yes", ResponseType.YES_NO),
        QuestionResponse("Overall physical health status", "4", ResponseType.SCALE_1_5),
        QuestionResponse("Patient's anxiety level", "2", ResponseType.SCALE_1_5)
    )

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
            QuestionnaireComponent(responses = mockData)
        }
    }
}