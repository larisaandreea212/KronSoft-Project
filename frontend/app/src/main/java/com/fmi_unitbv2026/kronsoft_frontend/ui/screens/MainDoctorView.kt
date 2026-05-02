package com.fmi_unitbv2026.kronsoft_frontend.ui.views
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDoctorView() {
    val currentDoctor = Doctor(1L, "Maria", "Ionescu", "Senior Cardiologist", "County Emergency Hospital")
    var selectedTab by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPatientId by remember { mutableStateOf<Long?>(null) }

    val patientsList = remember {
        listOf(
            PatientCard(1, "John", "Doe", "Heart Bypass", PatientStatus.CRITICAL),
            PatientCard(2, "Alice", "Smith", "Hip Replacement", PatientStatus.MODERATE),
            PatientCard(3, "Robert", "Brown", "Appendectomy", PatientStatus.STABLE),
            PatientCard(4, "Elena", "Vasile", "Valve Replacement", PatientStatus.CRITICAL),
            PatientCard(5, "George", "Popa", "Knee Arthroscopy", PatientStatus.STABLE)
        )
    }

    Row(modifier = Modifier.fillMaxSize()) {
        DoctorSidebar(doctor = currentDoctor, selectedTab = selectedTab, onTabSelected = { selectedTab = it })

        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
                .background(Color(0xFFF4F7FA))
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Patients",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF001F3F),
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF00E5FF)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredList = if (selectedTab == "critical") {
                    patientsList.filter { it.status == PatientStatus.CRITICAL }
                } else {
                    patientsList
                }

                items(filteredList) { patient ->
                    PatientCardComponent(
                        patient = patient,
                        isSelected = selectedPatientId == patient.idPatient,
                        onClick = { selectedPatientId = patient.idPatient }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .background(Color.White)
        ) {
            if (selectedPatientId != null) {
                val profile = PatientProfile(selectedPatientId!!.toInt(), "1920815400012", 68, "Male", "2026-04-10")
                val summary = PatientSummary(
                    aiScore = 85,
                    aiNote = "Patient exhibits persistent hypertension and reported localized pain. The AI detected a 15% deviation in recovery speed compared to standard parameters. Immediate checkup recommended.",
                    status = PatientStatus.CRITICAL,
                    questions = listOf(
                        QuestionResponse("Do you feel any sharp pain?", "Yes", ResponseType.YES_NO),
                        QuestionResponse("Mobility level (1-5)", "2", ResponseType.SCALE_1_5),
                        QuestionResponse("Sleep quality", "3", ResponseType.SCALE_1_5)
                    )
                )
                val evolutions = listOf(
                    Evolution("01 May", 20), Evolution("05 May", 45),
                    Evolution("10 May", 70), Evolution("15 May", 85)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp)
                ) {
                    Text(
                        text = "Patient Profile: ${profile.CNP}",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        DetailItem("Age", "${profile.age}")
                        DetailItem("Sex", profile.sex)
                        DetailItem("Surgery Date", profile.surgeryDate)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF001F3F)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AI ANALYSIS", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("Risk Score: ${summary.aiScore}%", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(summary.aiNote, color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, lineHeight = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    EvolutionChartComponent(data = evolutions)

                    Spacer(modifier = Modifier.height(24.dp))

                    QuestionnaireComponent(responses = summary.questions)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Select a patient to see the medical dashboard", color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF001F3F))
    }
}

@Composable
fun DoctorSidebar(doctor: Doctor, selectedTab: String, onTabSelected: (String) -> Unit) {
    val deepNavy = Color(0xFF000814)
    val royalBlue = Color(0xFF001F3F)
    val sidebarGradient = Brush.verticalGradient(colors = listOf(deepNavy, royalBlue))

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(sidebarGradient)
            .padding(vertical = 48.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp))
        {
            Text("DR. ${doctor.lastName.uppercase()}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
            Text(doctor.specialization, color = Color(0xFF00E5FF), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(doctor.hospitalName, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, modifier = Modifier.padding(top = 6.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(40.dp))
        }
        Column(modifier = Modifier
            .fillMaxWidth())
        {
            SidebarNavItem("All Patients", selectedTab == "all") { onTabSelected("all") }
            Spacer(modifier = Modifier.height(8.dp))
            SidebarNavItem("Critical Patients", selectedTab == "critical") { onTabSelected("critical") }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text("Sign Out", color = Color.White.copy(alpha = 0.3f), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 24.dp).clickable { })
    }
}

@Composable
fun SidebarNavItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent)
            .clickable { onClick() }.padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Box(modifier = Modifier.size(6.dp).background(Color(0xFF00E5FF), CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(label, color = if (isSelected) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.7f), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 15.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 1400, heightDp = 900)
@Composable
fun MainDoctorViewPreview() {
    MainDoctorView()
}