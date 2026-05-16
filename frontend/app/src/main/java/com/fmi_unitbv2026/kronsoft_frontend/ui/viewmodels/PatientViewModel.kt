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

    // 1. Stări existente pentru Profil
    private val _patientProfileDto = mutableStateOf<PatientProfile?>(null)
    val patientProfileDto: State<PatientProfile?> = _patientProfileDto

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // 2. STĂRI NOI pentru noile endpoint-uri
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
    // 3. Modificăm funcția ta să le lanseze pe toate automat
    fun initializePatientSession(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Pasul A: Încărcăm profilul pacientului bazat pe userId-ul din login
                val profile = withContext(Dispatchers.IO) {
                    apiService.getPatientProfileByUserId(userId)
                }
                _patientProfileDto.value = profile

                // Pasul B: Dacă profilul s-a încărcat cu succes, pornim automat celelalte date
                if (profile != null) {
                    // CORECTAT: profile.idPatient este Int direct, nu mai are nevoie de "?: 0"
                    val patientId = profile.idPatient

                    val card = withContext(Dispatchers.IO) {
                        apiService.getPatientCardById(patientId)
                    }
                    _patientCard.value = card

                    // Chemăm în paralel/secvențial celelalte încărcări de date
                    loadQuestions()
                    checkDailyCompletion(patientId)
                    loadAIEvaluation(patientId)
                }

            } catch (e: Exception) {
                _errorMessage.value = "Eroare conectare: ${e.message}"
                Log.e("PatientViewModel", "Eroare la incarcare sesiune: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 4. Endpoint: loadQuestions (Aduce întrebările active)
    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = withContext(Dispatchers.IO) {
                    apiService.loadQuestions()
                }
                _questionsList.value = questions
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Eroare la loadQuestions: ${e.message}")
            }
        }
    }

    // 5. Endpoint: checkDailyCompletion (Verifică dacă are voie să completeze azi)
    fun checkDailyCompletion(idPatient: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.checkDailyCompletion(idPatient)
                }
                if (response.isSuccessful) {
                    // canCompleteToday va fi true dacă NU a completat încă, false dacă a completat deja
                    _canCompleteToday.value = response.body() ?: false
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Eroare la checkDailyCompletion: ${e.message}")
            }
        }
    }

    // 6. Endpoint: loadAIEvaluation (Aduce cel mai recent scor de risc și note AI)
    fun loadAIEvaluation(idPatient: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPatientSummary(idPatient)
                }
                _patientSummary.value = response
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Eroare la loadAIEvaluation: ${e.message}")
            }
        }
    }

    // 7. Endpoint: submitDailyQuestionnaire (Trimite răspunsurile când apasă "COMPLETE")
    fun submitDailyQuestionnaire(submission: QuestionnaireSubmission, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.submitDailyQuestionnaire(submission)
                }
                if (response.isSuccessful) {
                    // CORECTAT: Folosim variabila corectă idPatient extrasă în siguranță
                    val idPatient = _patientProfileDto.value?.idPatient ?: 0
                    checkDailyCompletion(idPatient)

                    onResult(true) // Anunțăm UI-ul că a mers bine
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Eroare la submitQuestionnaire: ${e.message}")
                onResult(false)
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}