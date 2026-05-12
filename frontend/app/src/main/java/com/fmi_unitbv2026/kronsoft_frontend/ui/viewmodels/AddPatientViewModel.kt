package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.CreatePatient
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor
import com.fmi_unitbv2026.kronsoft_frontend.data.network.ApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddPatientViewModel(private val apiService: ApiService) : ViewModel() {

    var activeDoctors by mutableStateOf<List<Doctor>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadActiveDoctors()
    }

    private fun loadActiveDoctors() {
        viewModelScope.launch {
            try {
                activeDoctors = apiService.getActiveDoctors()
            } catch (e: Exception) {
                errorMessage = "Could not load doctors list."
            }
        }
    }

    fun registerPatient(patientData: CreatePatient, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val authResult = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(patientData.email, password)
                    .await()

                val uid = authResult.user?.uid ?: throw Exception("Firebase UID failed")

                val finalPatient = patientData.copy(firebaseUid = uid)
                val response = apiService.createPatient(finalPatient)

                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    errorMessage = "Backend error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}