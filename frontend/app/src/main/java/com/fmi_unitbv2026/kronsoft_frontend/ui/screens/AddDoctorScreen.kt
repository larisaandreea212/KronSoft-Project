package com.fmi_unitbv2026.kronsoft_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

@Composable
fun AddDoctorScreen(onCancel: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var hospitalName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            specialization.isNotBlank() &&
            hospitalName.isNotBlank() &&
            email.isNotBlank() &&
            password.length >= 6

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(40.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Register New Doctor",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF001F3F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StyledInputField(value = firstName, onValueChange = { firstName = it }, label = "First Name", modifier = Modifier.weight(1f))
            StyledInputField(value = lastName, onValueChange = { lastName = it }, label = "Last Name", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField(value = specialization, onValueChange = { specialization = it }, label = "Specialization")

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField(value = hospitalName, onValueChange = { hospitalName = it }, label = "Hospital Name")

        Spacer(modifier = Modifier.height(16.dp))
        StyledInputField(value = email, onValueChange = { email = it }, label = "Email Address")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
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
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color(0xFF001F3F)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {  },
                modifier = Modifier.height(54.dp).weight(1f),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF),
                    disabledContainerColor = Color(0xFF00E5FF).copy(alpha = 0.3f),
                    contentColor = Color(0xFF001F3F),
                    disabledContentColor = Color(0xFF001F3F).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirm Registration", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.height(54.dp).weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
            ) {
                Text("Cancel", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StyledInputField(
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

@Preview(showBackground = true, widthDp = 800, heightDp = 800)
@Composable
fun AddDoctorScreenPreview() {
    AddDoctorScreen(onCancel = {})
}