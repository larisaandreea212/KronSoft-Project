package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.CreateDoctor
import com.fmi_unitbv2026.kronsoft_frontend.data.network.ApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddDoctorViewModel(private val apiService: ApiService) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun registerDoctor(firstName: String, lastName: String, specialization: String, hospitalName: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val authResult = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await()

                val uid = authResult.user?.uid ?: throw Exception("Failed to get Firebase UID")

                val doctorDto = CreateDoctor(
                    firstName = firstName,
                    lastName = lastName,
                    specialization = specialization,
                    hospitalName = hospitalName,
                    email = email,
                    firebaseUid = uid
                )

                val response = apiService.createDoctor(doctorDto)

                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    errorMessage = "Backend error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Registration failed"
            } finally {
                isLoading = false
            }
        }
    }
}