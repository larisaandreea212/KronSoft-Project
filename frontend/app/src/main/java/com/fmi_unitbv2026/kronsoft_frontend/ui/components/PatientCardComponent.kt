package com.fmi_unitbv2026.kronsoft_frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.BorderStroke
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientCard
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientStatus

@Composable
fun PatientCardComponent(patient: PatientCard, isSelected: Boolean = false, onClick: () -> Unit = {})
{
    val statusColor = when (patient.status) {
        PatientStatus.CRITICAL -> Color(0xFFE53935)
        PatientStatus.MODERATE -> Color(0xFFFFB300)
        PatientStatus.STABLE -> Color(0xFF43A047)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F7FF) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${patient.firstName} ${patient.lastName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Surgery Type: ${patient.surgeryType}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = statusColor, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F8)
@Composable
fun PatientCardPreview() {
    Column(modifier = Modifier.padding(10.dp)) {

        PatientCardComponent(
            patient = PatientCard(1, "Ion", "Popescu", "Cardiologie", PatientStatus.CRITICAL),
            isSelected = false
        )

        PatientCardComponent(
            patient = PatientCard(2, "Vasile", "Georgescu", "Ortopedie", PatientStatus.MODERATE),
            isSelected=false
        )

        PatientCardComponent(
            patient = PatientCard(2, "Maria", "Ionescu", "Neurochirurgie", PatientStatus.STABLE),
            isSelected = true
        )
    }
}