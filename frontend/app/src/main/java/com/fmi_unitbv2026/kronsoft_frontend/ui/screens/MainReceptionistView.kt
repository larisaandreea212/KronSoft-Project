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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientCard
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.DoctorCardComponent
import com.fmi_unitbv2026.kronsoft_frontend.ui.components.PatientCardComponent
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.ReceptionistViewModel

@Composable
fun MainReceptionistView(
    viewModel: ReceptionistViewModel,
    onNavigateToAddDoctor: () -> Unit,
    onNavigateToAddPatient: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }
    var transferSearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadDoctors()
    }

    val destinationDoctor = viewModel.activeDoctors.find {
        val fullName = "${it.firstName} ${it.lastName}".lowercase()
        fullName.contains(transferSearchQuery.lowercase()) && it.idDoctor != selectedDoctor?.idDoctor
    }.takeIf { transferSearchQuery.length >= 3 }

    Surface(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {

            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF001F3F))
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    "RECEPTION PANEL",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                SidebarButton("Register Doctor", Icons.Default.PersonAdd, false) {
                    onNavigateToAddDoctor()
                }
                SidebarButton("Register Patient", Icons.Default.GroupAdd, false) {
                    onNavigateToAddPatient()
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.White.copy(alpha = 0.1f))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item { SidebarHeader("Active Doctors") }
                    items(viewModel.activeDoctors) { doctor ->
                        DoctorCardComponent(
                            doctor = doctor,
                            isSelected = selectedDoctor?.idDoctor == doctor.idDoctor,
                            onClick = {
                                selectedDoctor = doctor
                                transferSearchQuery = ""
                                viewModel.loadPatients(doctor.idDoctor.toInt())
                            }
                        )
                    }

                    item { SidebarHeader("Inactive Doctors") }
                    items(viewModel.inactiveDoctors) { doctor ->
                        DoctorCardComponent(
                            doctor = doctor,
                            isSelected = selectedDoctor?.idDoctor == doctor.idDoctor,
                            onClick = {
                                selectedDoctor = doctor
                                viewModel.loadPatients(doctor.idDoctor.toInt())
                            }
                        )
                    }
                }

                SidebarButton("Logout", Icons.Default.Logout, false) {
                    onLogout()
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                if (selectedDoctor != null) {
                    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {

                        Text(
                            "Dr. ${selectedDoctor!!.firstName} ${selectedDoctor!!.lastName}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001F3F)
                        )
                        Text(text = selectedDoctor!!.specialization, color = Color.Gray, fontSize = 16.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        if (selectedDoctor!!.isActive) {
                            DeactivationSection(
                                query = transferSearchQuery,
                                onQueryChange = { transferSearchQuery = it },
                                targetDoctor = destinationDoctor,
                                onConfirmDeactivate = {
                                    destinationDoctor?.let { target ->
                                        viewModel.deactivateDoctor(selectedDoctor!!.idDoctor.toInt(), target.idDoctor.toInt())
                                        selectedDoctor = null
                                    }
                                }
                            )
                        } else {
                            InactiveAccountBadge()
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Assigned Patients", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

                        if (viewModel.patientsForSelectedDoctor.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                                Text("No patients assigned to this doctor.", color = Color.Gray)
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 350.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize().padding(top = 16.dp)
                            ) {
                                items(viewModel.patientsForSelectedDoctor) { patient ->
                                    Box(contentAlignment = Alignment.CenterEnd) {
                                        PatientCardComponent(patient = patient, isSelected = false)

                                        if (selectedDoctor!!.isActive) {
                                            IconButton(
                                                onClick = { viewModel.deletePatient(patient.idPatient) },
                                                modifier = Modifier.padding(end = 52.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.6f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    EmptyStateView("Select a doctor from the sidebar to manage records")
                }
            }
        }
    }
}

@Composable
fun DeactivationSection(
    query: String,
    onQueryChange: (String) -> Unit,
    targetDoctor: Doctor?,
    onConfirmDeactivate: () -> Unit
) {
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
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search new doctor for transfer...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.PersonSearch, null, tint = Color(0xFFD32F2F)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD32F2F),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Button(
                onClick = onConfirmDeactivate,
                enabled = targetDoctor != null,
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PersonOff, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm Deactivation", fontWeight = FontWeight.Bold)
            }
        }
        if (targetDoctor != null) {
            Text(
                "Will transfer to: Dr. ${targetDoctor.firstName} ${targetDoctor.lastName}",
                color = Color(0xFF388E3C),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun InactiveAccountBadge() {
    Surface(
        color = Color.LightGray.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("HISTORICAL DATA - ACCOUNT INACTIVE", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
        }
    }
}

@Composable
fun SidebarButton(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        color = if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.1f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, color = if (isSelected) Color(0xFF00E5FF) else Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun SidebarHeader(title: String) {
    Text(
        title.uppercase(),
        color = Color.White.copy(alpha = 0.4f),
        fontSize = 11.sp,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    )
}

@Composable
fun EmptyStateView(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.TouchApp, null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(msg, color = Color.Gray)
        }
    }
}