package com.fmi_unitbv2026.kronsoft_frontend.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmi_unitbv2026.kronsoft_frontend.data.models.User
import com.fmi_unitbv2026.kronsoft_frontend.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _userRole = mutableStateOf<String?>(null)
    val userRole: State<String?> = _userRole

    private val _loggedUserId = mutableStateOf<Int>(0)
    val loggedUserId: State<Int> = _loggedUserId

    fun onLoginClick(onNavigationRequested: (String) -> Unit) {
        if (email.value.isEmpty() || password.value.isEmpty()) {
            _errorMessage.value = "Te rugăm să completezi toate câmpurile."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user: User? = repository.login(email.value, password.value)

                if (user != null) {
                    _userRole.value = user.role
                    _loggedUserId.value = user.idUser
                    onNavigationRequested(user.role)
                } else {
                    _errorMessage.value = "Incorect email or password."
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        repository.logout()
        onLogoutSuccess()
    }

    fun resetFields() {
        email.value = ""
        password.value = ""
        _errorMessage.value = null
        _isLoading.value = false
    }
}
