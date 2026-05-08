package com.fmi_unitbv2026.kronsoft_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientCard
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientStatus
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.DoctorCardComponent
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.PatientCardComponent

enum class ReceptionTab {
    PATIENTS, ADD_DOCTOR, ADD_PATIENT
}

@Composable
fun MainReceptionistView(
    activeDoctors: List<Doctor> = emptyList(),
    inactiveDoctors: List<Doctor> = emptyList(),
    patientsForSelectedDoctor: List<PatientCard> = emptyList(),
    onLoadPatients: (Long) -> Unit = {},
    onDeletePatient: (String) -> Unit = {}
) {
    var currentTab by remember { mutableStateOf(ReceptionTab.PATIENTS) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
            // SIDEBAR
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF001F3F))
                    .padding(vertical = 24.dp)
            ) {
                Text("RECEPTION PANEL", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(32.dp))

                SidebarButton("Register Doctor", Icons.Default.PersonAdd, currentTab == ReceptionTab.ADD_DOCTOR) {
                    currentTab = ReceptionTab.ADD_DOCTOR
                    selectedDoctor = null
                }
                SidebarButton("Register Patient", Icons.Default.GroupAdd, currentTab == ReceptionTab.ADD_PATIENT) {
                    currentTab = ReceptionTab.ADD_PATIENT
                    selectedDoctor = null
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.White.copy(0.1f))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item { SidebarHeader("Active Doctors") }
                    items(activeDoctors) { doctor ->
                        DoctorCardComponent(
                            doctor = doctor,
                            isSelected = selectedDoctor?.idDoctor == doctor.idDoctor && currentTab == ReceptionTab.PATIENTS,
                            onClick = {
                                selectedDoctor = doctor
                                currentTab = ReceptionTab.PATIENTS
                                onLoadPatients(doctor.idDoctor)
                            }
                        )
                    }

                    item { SidebarHeader("Inactive Doctors") }
                    items(inactiveDoctors) { doctor ->
                        DoctorCardComponent(
                            doctor = doctor,
                            isSelected = selectedDoctor?.idDoctor == doctor.idDoctor && currentTab == ReceptionTab.PATIENTS,
                            onClick = {
                                selectedDoctor = doctor
                                currentTab = ReceptionTab.PATIENTS
                                onLoadPatients(doctor.idDoctor)
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                when (currentTab) {
                    ReceptionTab.PATIENTS -> {
                        if (selectedDoctor != null) {
                            DashboardContent(selectedDoctor!!, patientsForSelectedDoctor, onDeletePatient)
                        } else {
                            EmptyStateView("Select a doctor to view patients")
                        }
                    }
                    ReceptionTab.ADD_DOCTOR -> {
                        Box(modifier = Modifier.fillMaxSize().heightIn(max = 2000.dp)) {
                            AddDoctorScreen(onCancel = { currentTab = ReceptionTab.PATIENTS })
                        }
                    }
                    ReceptionTab.ADD_PATIENT -> {
                        Box(modifier = Modifier.fillMaxSize().heightIn(max = 2000.dp)) {
                            AddPatientScreen(
                                availableDoctors = activeDoctors,
                                onCancel = { currentTab = ReceptionTab.PATIENTS },
                                onConfirm = { _, _ -> currentTab = ReceptionTab.PATIENTS }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardContent(doctor: Doctor, patients: List<PatientCard>, onDeletePatient: (String) -> Unit) {

    var transferDoctorName by remember(doctor.idDoctor) { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {

        Text("Dr. ${doctor.firstName} ${doctor.lastName}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF001F3F))
        Text(text = doctor.specialization, color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(20.dp))

        if (doctor.isActive) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEBEE).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    "Transfer Patients & Deactivate Doctor",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = transferDoctorName,
                        onValueChange = { transferDoctorName = it },
                        label = { Text("Transfer to Dr...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.PersonSearch, null, tint = Color(0xFFD32F2F)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD32F2F),
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color(0xFFD32F2F)
                        )
                    )

                    Button(
                        onClick = {  },
                        enabled = transferDoctorName.length >= 3,
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            disabledContainerColor = Color.LightGray.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PersonOff, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirm", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {

            Surface(
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("INACTIVE ACCOUNT", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(24.dp))

        if (doctor.isActive) {
            Text("Assigned Patients", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 350.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(patients) { patient ->
                    Box(contentAlignment = Alignment.CenterEnd) {
                        PatientCardComponent(patient = patient, isSelected = false)
                        IconButton(
                            onClick = { onDeletePatient(patient.idPatient) },
                            modifier = Modifier.padding(end = 52.dp)
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.6f))
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.History, null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Historical view only. Account deactivated.", color = Color.Gray, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun SidebarButton(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { onClick() },
        color = if (isSelected) Color(0xFF00E5FF).copy(0.1f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) Color(0xFF00E5FF) else Color.White.copy(0.7f))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, color = if (isSelected) Color(0xFF00E5FF) else Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun SidebarHeader(title: String) {
    Text(title.uppercase(), color = Color.White.copy(0.4f), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
}

@Composable
fun EmptyStateView(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(msg, color = Color.Gray) }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun MainReceptionistViewPreview() {
    MainReceptionistView(
        activeDoctors = listOf(Doctor(1, "Stefan", "Ionescu", "Cardio", "Clinic", true)),
        inactiveDoctors = listOf(
            Doctor(2, "Maria", "Popa", "Pediatrie", "Spital Județean", false)
        ),
        patientsForSelectedDoctor = listOf(PatientCard("1", "Ana", "Maria", "Consult", PatientStatus.STABLE))
    )
}