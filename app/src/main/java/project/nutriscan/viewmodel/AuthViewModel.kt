package project.nutriscan.viewmodel

import androidx.lifecycle.ViewModel
import project.nutriscan.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    val authState = authRepository.authState
    val errorMessage = authRepository.errorMessage

    fun register(email: String, password: String) {
        authRepository.register(email, password)
    }

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }

    fun logout() {
        authRepository.logout()
    }

    fun isUserAuthenticated(): Boolean {
        return authRepository.isUserAuthenticated()
    }
}
