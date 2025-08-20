package project.nutriscan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import project.nutriscan.utils.AuthState

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        // Check initial auth state
        _authState.value = if (firebaseAuth.currentUser != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.LOADING
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //_authState.value = AuthState.AUTHENTICATED
                // Sign out immediately after registration
                firebaseAuth.signOut()
                _authState.value = AuthState.UNAUTHENTICATED
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.ERROR
                _errorMessage.value = getErrorMessage(exception)
            }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.LOADING

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.AUTHENTICATED
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.ERROR
               _errorMessage.value = getErrorMessage(exception)
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.UNAUTHENTICATED
    }

    fun isUserAuthenticated(): Boolean = firebaseAuth.currentUser != null

    private fun getErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthException -> when (exception.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Invalid email format"
                "ERROR_WEAK_PASSWORD" -> "Password should be at least 6 characters"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email is already registered"
                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                "ERROR_USER_DISABLED" -> "User account has been disabled"
                "ERROR_TOO_MANY_REQUESTS" -> "Too many failed attempts. Try again later"
                else -> exception.message ?: "Authentication failed"
            }
            else -> "An unexpected error occurred"
        }
    }
}
