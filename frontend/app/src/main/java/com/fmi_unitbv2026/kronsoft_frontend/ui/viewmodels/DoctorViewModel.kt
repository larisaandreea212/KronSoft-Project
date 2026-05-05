package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientProfile
import com.fmi_unitbv2026.kronsoft_frontend.data.network.RetrofitClient
import kotlinx.coroutines.launch
import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import kotlinx.coroutines.coroutineScope

class DoctorViewModel : ViewModel() {

    private val apiService = RetrofitClient.provideApiService()

    private val _doctorInfo = mutableStateOf<Doctor?>(null)
    val doctorInfo: State<Doctor?> = _doctorInfo

    private val _patientsList = mutableStateOf<List<PatientCard>>(emptyList())
    val patientsList: State<List<PatientCard>> = _patientsList

    private val _patientData = mutableStateOf<PatientProfile?>(null)
    val patientData: State<PatientProfile?> = _patientData

    private val _evolutionData = mutableStateOf<List<Evolution>>(emptyList())
    val evolutionData: State<List<Evolution>> = _evolutionData

    private val _patientSummary = mutableStateOf<PatientSummary?>(null)
    val patientSummary: State<PatientSummary?> = _patientSummary

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        loadDoctorDashboard(1)
    }

    fun loadDoctorDashboard(doctorId: Int) {
        executeApiCall {
            val info = apiService.getDoctorInfo(doctorId)
            val patients = apiService.getAllPatientsForDoctor(doctorId)

            _doctorInfo.value = info
            _patientsList.value = patients
        }
    }


    fun loadCriticalPatients(doctorId: Int) {
        executeApiCall {
            val criticalOnes = apiService.getCriticalPatients(doctorId)
            _patientsList.value = criticalOnes
        }
    }


    fun searchPatients(doctorId: Int, query: String) {
        if (query.isEmpty()) {
            loadDoctorDashboard(doctorId)
            return
        }
        executeApiCall {
            val results = apiService.searchPatients(doctorId, query)
            _patientsList.value = results
        }
    }

    fun selectPatient(patientId: Int) {
        _patientData.value = null
        _patientSummary.value = null
        _evolutionData.value = emptyList()

        executeApiCall {
            coroutineScope {

                launch {
                    try {
                        val profile = apiService.getPatientProfile(patientId)
                        withContext(Dispatchers.Main) { _patientData.value = profile }
                    } catch (e: Exception) { Log.e("API", "Eroare Profil: ${e.message}") }
                }

                launch {
                    try {
                        val evolution = apiService.getEvolutionData(patientId)
                        withContext(Dispatchers.Main) { _evolutionData.value = evolution }
                    } catch (e: Exception) { Log.e("API", "Eroare Evolutie: ${e.message}") }
                }

                launch {
                    try {
                        val summary = apiService.getPatientSummary(patientId)
                        withContext(Dispatchers.Main) { _patientSummary.value = summary }
                    } catch (e: Exception) { Log.e("API", "Eroare Sumar: ${e.message}") }
                }
            }
        }
    }

    private fun executeApiCall(block: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            withContext(Dispatchers.IO) {
                try {
                    block()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Eroare: ${e.message}"
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                    }
                }
            }
        }
    }
}
