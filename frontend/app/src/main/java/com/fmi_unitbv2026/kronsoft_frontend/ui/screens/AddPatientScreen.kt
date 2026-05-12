package com.fmi_unitbv2026.kronsoft_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.CreatePatient
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.AddPatientViewModel

@Composable
fun AddPatientScreen(
    viewModel: AddPatientViewModel,
    onCancel: () -> Unit,
    onSuccess: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var cnp by remember { mutableStateOf("") }
    var surgeryDate by remember { mutableStateOf("") } // Format: YYYY-MM-DD
    var surgeryType by remember { mutableStateOf("") }
    var doctorSearchName by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            onSuccess()
        }
    }

    val foundDoctor = viewModel.activeDoctors.find {
        val fullName = "${it.firstName} ${it.lastName}".lowercase()
        val reverseFullName = "${it.lastName} ${it.firstName}".lowercase()
        fullName.contains(doctorSearchName.lowercase()) || reverseFullName.contains(doctorSearchName.lowercase())
    }.takeIf { doctorSearchName.length > 2 }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && password.length >= 6 &&
            cnp.length == 13 && foundDoctor != null &&
            surgeryDate.isNotBlank() && surgeryType.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(40.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Register New Patient",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF001F3F)
        )

        Text(
            text = "Assign a patient to a doctor and create their secure account.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        viewModel.errorMessage?.let { error ->
            Surface(
                color = Color.Red.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(error, color = Color.Red, modifier = Modifier.padding(12.dp), fontSize = 14.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField(value = firstName, onValueChange = { firstName = it }, label = "First Name", modifier = Modifier.weight(1f), enabled = !viewModel.isLoading)
            StyledInputField(value = lastName, onValueChange = { lastName = it }, label = "Last Name", modifier = Modifier.weight(1f), enabled = !viewModel.isLoading)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField(value = age, onValueChange = { if(it.all { char -> char.isDigit() }) age = it }, label = "Age", modifier = Modifier.weight(0.4f), enabled = !viewModel.isLoading)
            StyledInputField(value = sex, onValueChange = { sex = it.uppercase() }, label = "Sex (M/F)", modifier = Modifier.weight(0.6f), enabled = !viewModel.isLoading)
        }

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField(value = cnp, onValueChange = { if(it.length <= 13) cnp = it }, label = "CNP (13 digits)", enabled = !viewModel.isLoading)

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField(value = surgeryDate, onValueChange = { surgeryDate = it }, label = "Surgery Date (YYYY-MM-DD)", modifier = Modifier.weight(1f), enabled = !viewModel.isLoading)
            StyledInputField(value = surgeryType, onValueChange = { surgeryType = it }, label = "Surgery Type", modifier = Modifier.weight(1f), enabled = !viewModel.isLoading)
        }

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField(value = email, onValueChange = { email = it }, label = "Patient Email", enabled = !viewModel.isLoading)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Initial Password (min. 6 chars)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !viewModel.isLoading,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00E5FF),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(24.dp))

        Text("Assigning Doctor", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF001F3F))

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = doctorSearchName,
            onValueChange = { doctorSearchName = it },
            label = { Text("Search Doctor by Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !viewModel.isLoading,
            leadingIcon = { Icon(Icons.Default.PersonSearch, null, tint = Color(0xFF00E5FF)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00E5FF),
                unfocusedBorderColor = Color.LightGray
            ),
            singleLine = true
        )

        if (foundDoctor != null) {
            Card(
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Selected Doctor:", fontSize = 12.sp, color = Color(0xFF00796B), fontWeight = FontWeight.Bold)
                        Text("Dr. ${foundDoctor.firstName} ${foundDoctor.lastName}", fontWeight = FontWeight.ExtraBold)
                        Text(foundDoctor.specialization, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    foundDoctor?.let { doctor ->
                        val patient = CreatePatient(
                            email = email,
                            firebaseUid = "",
                            firstName = firstName,
                            lastName = lastName,
                            age = age.toIntOrNull() ?: 0,
                            sex = sex,
                            cnp = cnp,
                            surgeryDate = surgeryDate,
                            surgeryType = surgeryType,
                            idDoctor = doctor.idDoctor
                        )
                        viewModel.registerPatient(patient, password)
                    }
                },
                modifier = Modifier.height(54.dp).weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF),
                    disabledContainerColor = Color(0xFF00E5FF).copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = isFormValid && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF001F3F), strokeWidth = 2.dp)
                } else {
                    Text("Register Patient", color = Color(0xFF001F3F), fontWeight = FontWeight.Bold)
                }
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.height(54.dp).weight(1f),
                shape = RoundedCornerShape(12.dp),
                enabled = !viewModel.isLoading
            ) {
                Text("Cancel", color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable

fun StyledInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF00E5FF),
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color(0xFF001F3F)
        ),
        singleLine = true
    )
}

