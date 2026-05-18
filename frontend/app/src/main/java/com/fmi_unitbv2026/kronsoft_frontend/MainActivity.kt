package com.fmi_unitbv2026.kronsoft_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fmi_unitbv2026.kronsoft_frontend.data.network.RetrofitClient
import com.fmi_unitbv2026.kronsoft_frontend.data.repository.AuthRepository
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.*
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.*
import com.fmi_unitbv2026.kronsoft_frontend.ui.views.MainDoctorView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitClient.apiService
        val authRepository = AuthRepository(apiService)

        val loginViewModel = LoginViewModel(authRepository)
        val registerViewModel = RegisterViewModel(authRepository)
        val receptionistViewModel = ReceptionistViewModel(apiService)
        val addDoctorViewModel = AddDoctorViewModel(apiService)
        val addPatientViewModel = AddPatientViewModel(apiService)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {

                    composable("login") {
                        LoginScreen(
                            viewModel = loginViewModel,
                            onNavigateToRegister = { navController.navigate("register") },
                            onLoginSuccess = { role ->
                                when (role) {
                                    "DOCTOR" -> navController.navigate("doctor_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    "PATIENT" -> {
                                        val userId = loginViewModel.loggedUserId.value
                                        navController.navigate("patient_dashboard/$userId") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    "RECEPTIONIST" -> navController.navigate("receptionist_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        )
                    }

                    composable(
                        route = "patient_dashboard/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getInt("userId") ?: 1
                        val patientViewModel: PatientViewModel = viewModel()
                        val chatViewModel: ChatViewModel = viewModel()

                        LaunchedEffect(userId) {
                            patientViewModel.initializePatientSession(userId)
                        }

                        val profile = patientViewModel.patientProfileDto.value
                        val card = patientViewModel.patientCard.value
                        val canComplete = patientViewModel.canCompleteToday.value
                        val questions = patientViewModel.questionsList.value
                        val summary = patientViewModel.patientSummary.value
                        val errorMessage = patientViewModel.errorMessage.value
                        val isLoading = patientViewModel.isLoading.value

                        val messages by chatViewModel.messages.collectAsState()

                        LaunchedEffect(profile, card) {
                            if (profile != null && card != null) {
                                val doctorIdStr = "1" 
                                chatViewModel.listenForMessages(
                                    doctorId = doctorIdStr,
                                    patientId = profile.idPatient.toString()
                                )
                            }
                        }

                        if (isLoading) {
                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF001220)), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF00E5FF))
                            }
                        } else if (errorMessage != null) {
                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF001220)), contentAlignment = Alignment.Center) {
                                Text(text = "Eroare rețea: $errorMessage", color = Color.Red)
                            }
                        } else if (profile != null && card != null) {
                            MainPatientScreen(
                                patient = card,
                                patientProfile = profile,
                                canComplete = canComplete,
                                questions = questions,
                                patientSummary = summary,
                                viewModel = patientViewModel,
                                messages = messages,
                                onSendMessage = { text ->
                                    val doctorIdStr = "1"
                                    chatViewModel.sendMessage(
                                        senderId = profile.idPatient.toString(),
                                        receiverId = doctorIdStr,
                                        text = text
                                    )
                                },
                                onLogout = {
                                    chatViewModel.clearMessages()
                                    loginViewModel.resetFields()
                                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                                }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF001220)), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF00E5FF))
                            }
                        }
                    }
                    composable("receptionist_dashboard") {
                        MainReceptionistView(
                            viewModel = receptionistViewModel,
                            onLogout = {
                                loginViewModel.resetFields()
                                navController.navigate("login") { popUpTo(0) { inclusive = true } }
                            },
                            onNavigateToAddDoctor = { navController.navigate("add_doctor_screen") },
                            onNavigateToAddPatient = { navController.navigate("add_patient_screen") }
                        )
                    }

                    composable("add_doctor_screen") {
                        AddDoctorScreen(viewModel = addDoctorViewModel, onCancel = { navController.popBackStack() }, onSuccess = { navController.popBackStack() })
                    }

                    composable("add_patient_screen") {
                        AddPatientScreen(viewModel = addPatientViewModel, onCancel = { navController.popBackStack() }, onSuccess = { navController.popBackStack() })
                    }

                    composable("register") {
                        RegisterScreen(viewModel = registerViewModel, onNavigateBackToLogin = { navController.popBackStack() })
                    }

                    composable("doctor_dashboard") {
                        MainDoctorView(
                            onLogout = {
                                loginViewModel.resetFields()
                                navController.navigate("login") { popUpTo(0) { inclusive = true } }
                            }
                        )
                    }
                }
            }
        }
    }
}