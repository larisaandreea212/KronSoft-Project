package com.fmi_unitbv2026.kronsoft_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fmi_unitbv2026.kronsoft_frontend.data.network.RetrofitClient
import com.fmi_unitbv2026.kronsoft_frontend.data.repository.AuthRepository
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.LoginScreen
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.LoginViewModel
import com.fmi_unitbv2026.kronsoft_frontend.ui.views.MainDoctorView
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.RegisterViewModel
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.RegisterScreen
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.*
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.AddDoctorScreen
import androidx.compose.material3.Text
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.AddPatientScreen
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.MainReceptionistView

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
                            onNavigateToRegister = {
                                navController.navigate("register")
                            },
                            onLoginSuccess = { role ->
                                when (role) {
                                    "DOCTOR" -> navController.navigate("doctor_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    "PATIENT" -> navController.navigate("patient_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    "RECEPTIONIST" -> navController.navigate("receptionist_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    else -> {

                                    }
                                }
                            }
                        )
                    }
                    composable("receptionist_dashboard") {
                        MainReceptionistView(
                            viewModel = receptionistViewModel,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            },
                            onNavigateToAddDoctor = {
                                navController.navigate("add_doctor_screen")
                            },
                            onNavigateToAddPatient = {
                                navController.navigate("add_patient_screen")
                            }
                        )
                    }

                    composable("add_doctor_screen") {
                        AddDoctorScreen(
                            viewModel = addDoctorViewModel,
                            onCancel = {
                                navController.popBackStack()
                            },
                            onSuccess = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("add_patient_screen") {
                        AddPatientScreen(
                            viewModel = addPatientViewModel,
                            onCancel = { navController.popBackStack() },
                            onSuccess = { navController.popBackStack() }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            viewModel = registerViewModel,
                            onNavigateBackToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("patient_dashboard") {
                        
                    }

                    composable("doctor_dashboard") {
                        MainDoctorView()
                    }
                }
            }
        }
    }
}