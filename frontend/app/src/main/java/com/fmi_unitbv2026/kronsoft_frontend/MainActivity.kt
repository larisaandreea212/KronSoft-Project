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
import com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels.ReceptionistViewModel
import com.fmi_unitbv2026.kronsoft_frontend.ui.screens.ReceptionistScreen
import androidx.compose.material3.Text
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inițializăm serviciile
        val apiService = RetrofitClient.apiService
        val authRepository = AuthRepository(apiService)

        // ViewModel-urile au nevoie de repository
        val loginViewModel = LoginViewModel(authRepository)
        val registerViewModel = RegisterViewModel(authRepository)
        val receptionistViewModel = ReceptionistViewModel(apiService)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {

                    // --- ECRAN LOGIN ---
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
                                        // Opțional: un mesaj dacă rolul e necunoscut
                                    }
                                }
                            }
                        )
                    }
                    composable("receptionist_dashboard") {
                        ReceptionistScreen(
                            viewModel = receptionistViewModel,
                            onLogout = {
                                // Te trimite la login și șterge istoricul
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    // --- ECRAN REGISTER ---
                    composable("register") {
                        RegisterScreen(
                            viewModel = registerViewModel,
                            onNavigateBackToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("patient_dashboard") {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Text(text = "Patient Dashboard - Under Construction")
                        }
                    }

                    // --- ECRAN DASHBOARD DOCTOR ---
                    composable("doctor_dashboard") {
                        MainDoctorView()
                    }
                }
            }
        }
    }
}