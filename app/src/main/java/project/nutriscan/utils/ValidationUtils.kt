package project.nutriscan.utils

import android.util.Patterns

object ValidationUtils {

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
    }

    fun validateLoginPassword(password: String): String? {
        return if (password.isBlank()) "Password is required" else null
    }
}
