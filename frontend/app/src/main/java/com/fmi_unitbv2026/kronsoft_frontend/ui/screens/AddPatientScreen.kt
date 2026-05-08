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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.CreatePatient
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor

@Composable
fun AddPatientScreen(
    availableDoctors: List<Doctor>,
    onCancel: () -> Unit,
    onConfirm: (CreatePatient, String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var cnp by remember { mutableStateOf("") }

    var doctorSearchName by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val foundDoctor = availableDoctors.find {
        val fullName = "${it.firstName} ${it.lastName}".lowercase()
        val reverseFullName = "${it.lastName} ${it.firstName}".lowercase()
        fullName.contains(doctorSearchName.lowercase()) || reverseFullName.contains(doctorSearchName.lowercase())
    }.takeIf { doctorSearchName.length > 2 }

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

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField2(value = firstName, onValueChange = { firstName = it }, label = "Patient First Name", modifier = Modifier.weight(1f))
            StyledInputField2(value = lastName, onValueChange = { lastName = it }, label = "Patient Last Name", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField2(value = age, onValueChange = { age = it }, label = "Age", modifier = Modifier.weight(0.4f))
            StyledInputField2(value = sex, onValueChange = { sex = it }, label = "Sex (M/F)", modifier = Modifier.weight(0.6f))
        }

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField2(value = cnp, onValueChange = { cnp = it }, label = "CNP")

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField2(value = email, onValueChange = { email = it }, label = "Patient Email")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Patient Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
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
        Text("Search for doctor by name", fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = doctorSearchName,
            onValueChange = { doctorSearchName = it },
            label = { Text("Doctor Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.PersonSearch, contentDescription = null, tint = Color(0xFF00E5FF)) },
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
                        Text("Linked to:", fontSize = 12.sp, color = Color(0xFF00796B), fontWeight = FontWeight.Bold)
                        Text("Dr. ${foundDoctor.firstName} ${foundDoctor.lastName}", fontWeight = FontWeight.ExtraBold)
                        Text(foundDoctor.specialization, fontSize = 12.sp)
                    }
                }
            }
        } else if (doctorSearchName.length > 2) {
            Text(
                "No doctor matches this name. Check spelling.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                            idDoctor = doctor.idDoctor
                        )
                        onConfirm(patient, password)
                    }
                },
                modifier = Modifier.height(54.dp).weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF),
                    disabledContainerColor = Color(0xFF00E5FF).copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = foundDoctor != null && password.isNotEmpty() && email.isNotEmpty()
            ) {
                Text("Register Patient", color = Color(0xFF001F3F), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.height(54.dp).weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", color = Color.Gray, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun StyledInputField2(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
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

@Preview(showBackground = true, widthDp = 800, heightDp = 1000)
@Composable
fun AddPatientScreenPreview() {
    val mockDoctors = listOf(
        Doctor(1, "John", "Smith", "Cardiology", "Central Hospital", true),
        Doctor(2, "Emily", "Watson", "Neurology", "St. Mary Clinic", true)
    )
    AddPatientScreen(availableDoctors = mockDoctors, onCancel = {}, onConfirm = { _, _ -> })
}