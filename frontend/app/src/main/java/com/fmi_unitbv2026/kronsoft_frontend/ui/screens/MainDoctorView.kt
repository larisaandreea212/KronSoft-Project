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
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDoctorView(viewModel: DoctorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val doctorInfo by viewModel.doctorInfo
    val patientsList by viewModel.patientsList
    val profile by viewModel.patientData
    val summary by viewModel.patientSummary
    val evolutions by viewModel.evolutionData
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    var selectedTab by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPatientId by remember { mutableStateOf<String?>(null) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF00E5FF))
        }
        return
    }

    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage!!, color = Color.Red)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        doctorInfo?.let { doctor ->
            DoctorSidebar(
                doctor = doctor,
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    if (tab == "critical") {
                        viewModel.loadCriticalPatients(1)
                    } else {
                        viewModel.loadDoctorDashboard(1)
                    }
                }
            )
        }

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
                onValueChange = {
                    searchQuery = it
                    viewModel.searchPatients( 1, it)
                },
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

                items(patientsList) { patient ->
                    PatientCardComponent(
                        patient = patient,
                        isSelected = selectedPatientId == patient.idPatient,
                        onClick = {
                            selectedPatientId = patient.idPatient

                            val idAsInt = patient.idPatient.toIntOrNull() ?: 0
                            viewModel.selectPatient(idAsInt)
                        }
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
                if (profile != null && summary != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "Patient Profile: ${profile!!.CNP}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            DetailItem("Age", "${profile!!.age}")
                            DetailItem("Sex", profile!!.sex)
                            DetailItem("Surgery Date", profile!!.surgeryDate)
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
                                    Text("Risk Score: ${summary!!.aiScore}%", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(summary!!.aiNote, color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, lineHeight = 22.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        EvolutionChartComponent(data = evolutions)

                        Spacer(modifier = Modifier.height(24.dp))

                        QuestionnaireComponent(responses = summary!!.questions)
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00E5FF))
                    }
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