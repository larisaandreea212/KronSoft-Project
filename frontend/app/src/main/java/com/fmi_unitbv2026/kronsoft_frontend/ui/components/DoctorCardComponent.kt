package com.fmi_unitbv2026.kronsoft_frontend.ui.components

import androidx.compose.foundation.BorderStroke
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
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor

@Composable
fun DoctorCardComponent(
    doctor: Doctor,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val statusColor = if (doctor.isActive) Color(0xFF43A047) else Color(0xFF9E9E9E)

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
            color = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0).copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = statusColor, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dr. ${doctor.lastName} ${doctor.firstName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = doctor.specialization,
                    fontSize = 13.sp,
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Medium
                )
                // Hospital Name
                Text(
                    text = doctor.hospitalName,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }

            Text(
                text = if (doctor.isActive) "ACTIVE" else "INACTIVE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = if (doctor.isActive) Color(0xFF43A047) else Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F8)
@Composable
fun DoctorCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Selected State", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        DoctorCardComponent(
            doctor = Doctor(
                idDoctor = 1,
                firstName = "John",
                lastName = "Smith",
                specialization = "Cardiology",
                isActive = true,
                hospitalName = "General Hospital Central"
            ),
            isSelected = true
        )

        Text("Unselected State", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        DoctorCardComponent(
            doctor = Doctor(
                idDoctor = 2,
                firstName = "Emily",
                lastName = "Watson",
                specialization = "Neurology",
                isActive = true,
                hospitalName = "St. Mary's Clinic"
            ),
            isSelected = false
        )

        Text("Inactive State", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        DoctorCardComponent(
            doctor = Doctor(
                idDoctor = 3,
                firstName = "Robert",
                lastName = "Brown",
                specialization = "Pediatrics",
                isActive = false,
                hospitalName = "Children's Care Center"
            ),
            isSelected = false
        )
    }
}