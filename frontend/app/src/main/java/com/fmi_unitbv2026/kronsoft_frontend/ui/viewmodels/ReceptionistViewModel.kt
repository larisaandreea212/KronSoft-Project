package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Doctor
import com.fmi_unitbv2026.kronsoft_frontend.data.models.PatientCard
import com.fmi_unitbv2026.kronsoft_frontend.data.models.DeactivateDoctor
import com.fmi_unitbv2026.kronsoft_frontend.data.network.ApiService
import kotlinx.coroutines.launch

class ReceptionistViewModel(private val apiService: ApiService) : ViewModel() {

    var activeDoctors by mutableStateOf<List<Doctor>>(emptyList())
    var inactiveDoctors by mutableStateOf<List<Doctor>>(emptyList())
    var patientsForSelectedDoctor by mutableStateOf<List<PatientCard>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadDoctors()
    }

    fun loadDoctors() {
        viewModelScope.launch {
            try {
                activeDoctors = apiService.getActiveDoctors()
                inactiveDoctors = apiService.getInactiveDoctors()
            } catch (e: Exception) {
                errorMessage = "Failed to load doctors: ${e.message}"
            }
        }
    }

    fun loadPatients(idDoctor: Int) {
        viewModelScope.launch {
            try {
                patientsForSelectedDoctor = apiService.getAllPatientsForDoctor(idDoctor)
            } catch (e: Exception) {
                errorMessage = "Failed to load patients"
            }
        }
    }

    fun deletePatient(idPatient: String) {
        viewModelScope.launch {
            try {
                val response = apiService.deletePatient(idPatient.toInt())
                if (response.isSuccessful) {
                    patientsForSelectedDoctor = patientsForSelectedDoctor.filter { it.idPatient != idPatient }
                }
            } catch (e: Exception) {
                errorMessage = "Delete failed"
            }
        }
    }

    fun deactivateDoctor(oldDoctorId: Int, newDoctorId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val dto = DeactivateDoctor(idOldDoctor = oldDoctorId, idNewDoctor = newDoctorId)
                val response = apiService.deactivateDoctor(dto)
                if (response.isSuccessful) {
                    loadDoctors()
                    patientsForSelectedDoctor = emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Deactivation failed"
            } finally {
                isLoading = false
            }
        }
    }
}