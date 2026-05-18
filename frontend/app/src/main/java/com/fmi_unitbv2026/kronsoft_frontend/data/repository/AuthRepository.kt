package com.fmi_unitbv2026.kronsoft_frontend.data.repository

import com.fmi_unitbv2026.kronsoft_frontend.data.models.User
import com.fmi_unitbv2026.kronsoft_frontend.data.network.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await


class AuthRepository(private val apiService: ApiService) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String, parola: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, parola).await()
            val uid = result.user?.uid

            if (uid != null) {
                apiService.getUserByFirebaseUid(uid)
            } else {
                null
            }
        } catch (e: Exception) {
            val message = when (e) {
                is FirebaseAuthInvalidUserException -> "This email is not registered."
                is FirebaseAuthInvalidCredentialsException -> "Email or password wrong."
                else -> "Authentication error: ${e.localizedMessage}"
            }
            throw Exception(message)
        }
    }

    suspend fun completeRegistration(email: String, parola: String): Boolean {
        val isPreRegistered = apiService.checkEmailExists(email)

        if (!isPreRegistered) {
            return false
        }

        return try {
            val result = auth.createUserWithEmailAndPassword(email, parola).await()
            val uid = result.user?.uid ?: return false

            apiService.linkFirebaseUid(email, uid)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserUid(): String? = auth.currentUser?.uid
}