package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.*
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

    private val _questionsList = mutableStateOf<List<Questions>>(emptyList())
    val questionsList: State<List<Questions>> = _questionsList

    private val _canCompleteToday = mutableStateOf(false)
    val canCompleteToday: State<Boolean> = _canCompleteToday

    private val _aiReport = mutableStateOf<AIReportResponse?>(null)
    val aiReport: State<AIReportResponse?> = _aiReport

    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    private val _patientSummary = mutableStateOf<PatientSummary?>(null)
    val patientSummary: State<PatientSummary?> = _patientSummary

    private val _patientCard = mutableStateOf<PatientCard?>(null)
    val patientCard: State<PatientCard?> = _patientCard

    fun initializePatientSession(userId: Int) {
        Log.d("Verification", "USER_ID : $userId !!!")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val profile = withContext(Dispatchers.IO) {
                    apiService.getPatientProfileByUserId(userId)
                }
                _patientProfileDto.value = profile

                if (profile != null) {
                    val patientId = profile.idPatient

                    val card = withContext(Dispatchers.IO) {
                        apiService.getPatientCardById(patientId)
                    }
                    _patientCard.value = card

                    loadQuestions()
                    checkDailyCompletion(patientId)
                    loadAIEvaluation(patientId)
                }

            } catch (e: Exception) {
                _errorMessage.value = "Connection error: ${e.message}"
                Log.e("PatientViewModel", "Failed to load session: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = withContext(Dispatchers.IO) {
                    apiService.loadQuestions()
                }
                _questionsList.value = questions
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error at loadQuestions: ${e.message}")
            }
        }
    }

    fun checkDailyCompletion(idPatient: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.checkDailyCompletion(idPatient)
                }
                if (response.isSuccessful) {
                    _canCompleteToday.value = response.body() ?: false
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error at checkDailyCompletion: ${e.message}")
            }
        }
    }

    fun loadAIEvaluation(idPatient: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPatientSummary(idPatient)
                }
                _patientSummary.value = response
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error at loadAIEvaluation: ${e.message}")
            }
        }
    }

    fun submitDailyQuestionnaire(submission: QuestionnaireSubmission, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.submitDailyQuestionnaire(submission)
                }
                if (response.isSuccessful) {
                    val idPatient = _patientProfileDto.value?.idPatient ?: 0
                    checkDailyCompletion(idPatient)
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error at submitQuestionnaire: ${e.message}")
                onResult(false)
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}