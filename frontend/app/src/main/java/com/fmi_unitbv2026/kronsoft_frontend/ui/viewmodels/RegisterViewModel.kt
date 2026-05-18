package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var isSuccess = mutableStateOf(false)

    fun onRegisterClick() {
        if (password.value != confirmPassword.value) {
            errorMessage.value = "Passwords do not match."
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = repository.completeRegistration(email.value, password.value)

                if (result) {
                    isSuccess.value = true
                } else {
                    errorMessage.value = "Registration impossible. This email is not pre-registered in our system. Please contact the receptionist."
                }
            } catch (e: Exception) {
                errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun resetFields() {
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
        isLoading.value = false
        errorMessage.value = null
        isSuccess.value = false
    }
}