package com.fmi_unitbv2026.kronsoft_frontend.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.ReceptionistViewModel

@Composable
fun ReceptionistScreen(viewModel: ReceptionistViewModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Receptionist Panel", style = MaterialTheme.typography.headlineMedium)
        Text("Pre-register new patients or doctors", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.patientName.value,
            onValueChange = { viewModel.patientName.value = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.patientEmail.value,
            onValueChange = { viewModel.patientEmail.value = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        // Aici poți adăuga un Dropdown pentru selectarea rolului (DOCTOR/PATIENT)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.preRegisterPatient() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading.value
        ) {
            Text("Pre-Register User")
        }

        viewModel.statusMessage.value?.let {
            Text(it, modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onLogout) {
            Text("Logout")
        }
    }
}