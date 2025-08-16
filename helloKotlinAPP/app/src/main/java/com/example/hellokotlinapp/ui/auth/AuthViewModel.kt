package com.example.hellokotlinapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellokotlinapp.data.remote.AuthRepository
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModelProvider

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    val currentUid get() = repo.currentUid

    fun signup(
        fullName: String,
        username: String,
        email: String,
        birthdate: String,
        password: String,
        onDone: (Boolean, String?) -> Unit
    ) = viewModelScope.launch {
        try {
            repo.signup(fullName, username, email, birthdate, password)
            onDone(true, null)
        } catch (e: Exception) {
            onDone(false, e.message)
        }
    }

    fun login(emailOrUsername: String, password: String, onDone: (Boolean, String?) -> Unit) =
        viewModelScope.launch {
            try {
                repo.login(emailOrUsername, password)
                onDone(true, null)
            } catch (e: Exception) {
                onDone(false, e.message)
            }
        }

    fun logout() = repo.logout()
}

class AuthViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}