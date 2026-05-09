package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.repository.AuthRepository
import com.fmi_unitbv2026.kronsoft_frontend.data.network.ApiService
import kotlinx.coroutines.launch

class ReceptionistViewModel(private val apiService: ApiService) : ViewModel() {

    var patientName = mutableStateOf("")
    var patientEmail = mutableStateOf("")
    var selectedRole = mutableStateOf("PATIENT") // Default este pacient

    var isLoading = mutableStateOf(false)
    var statusMessage = mutableStateOf<String?>(null)

    fun preRegisterPatient() {
        if (patientName.value.isEmpty() || patientEmail.value.isEmpty()) {
            statusMessage.value = "Please fill all fields."
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                // Trimitem datele la Java
                // Notă: Va trebui să definim acest endpoint în ApiService
                val response = apiService.linkFirebaseUid(patientEmail.value, "PRE_REG")

                if (response.isSuccessful) {
                    statusMessage.value = "Patient ${patientEmail.value} pre-registered successfully!"
                    patientName.value = ""
                    patientEmail.value = ""
                } else {
                    statusMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                statusMessage.value = "Connection error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}