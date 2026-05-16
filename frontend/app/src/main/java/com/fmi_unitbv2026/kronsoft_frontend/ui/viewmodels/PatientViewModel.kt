package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientProfile
import com.fmi_unitbv2026.kronsoft_frontend.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val _patientProfileDto = mutableStateOf<PatientProfile?>(null)
    val patientProfileDto: State<PatientProfile?> = _patientProfileDto

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun initializePatientSession(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val profile = withContext(Dispatchers.IO) {
                    apiService.getPatientProfileByUserId(userId)
                }

                _patientProfileDto.value = profile

            } catch (e: Exception) {
                _errorMessage.value = "Eroare conectare: ${e.message}"
                Log.e("PatientViewModel", "Eroare la incarcare profil: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}