package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientProfile
import com.fmi_unitbv2026.kronsoft_frontend.data.network.RetrofitClient
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {

    private val apiService = RetrofitClient.provideApiService()

    private val _patientData = mutableStateOf<PatientProfile?>(null)
    val patientData: State<PatientProfile?> = _patientData

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        fetchPatientData()
    }

    fun fetchPatientData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getPatientData()
                _patientData.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Eroare la încărcarea datelor: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
