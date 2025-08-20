package project.nutriscan.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.room.util.findColumnIndexBySuffix
import project.nutriscan.R
import project.nutriscan.databinding.FragmentRegisterBinding
import project.nutriscan.utils.AuthState
import project.nutriscan.utils.ValidationUtils
import project.nutriscan.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()
    }


    private fun setupObservers() {
        // Observe auth state
        authViewModel.authState.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                AuthState.LOADING -> {
                    showLoading(true)
                }
                AuthState.AUTHENTICATED -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_register_to_login)
                }
                AuthState.UNAUTHENTICATED -> {
                    showLoading(false)
                }
                AuthState.ERROR -> {
                    showLoading(false)
                }
            }
        }
        // Observe error messages
        authViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.RegisterUser.setOnClickListener {
            if (validateFields()) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                authViewModel.register(email, password)
            }
        }

        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.RegisterUser.isEnabled = !isLoading
        binding.RegisterUser.text = if (isLoading) "Registering..." else "Register"
    }

    private fun navigateRegisterToLogin(){
        findNavController().navigate(R.id.action_register_to_login)
    }

//    private fun validateFields(): Boolean {
//        // Accessing the text from EditText using View Binding
//        val email = binding.emailEditText.text.toString().trim()
//        val password = binding.passwordEditText.text.toString().trim()
//
//        // Validate username
//        // Validate email
//        if (email.isEmpty()) {
//            binding.emailEditText.error = "Email is required" // Use binding to set error
//            binding.emailEditText.requestFocus() // Use binding to request focus
//            return false
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.emailEditText.error = "Please enter a valid email" // Use binding to set error
//            binding.emailEditText.requestFocus() // Use binding to request focus
//            return false
//        }
//        // Validate password
//        if (password.isEmpty()) {
//            binding.passwordEditText.error = "Password is required" // Use binding to set error
//            binding.passwordEditText.requestFocus() // Use binding to request focus
//            return false
//        } else if (password.length < 8) {
//            binding.passwordEditText.error =
//                "Password must be at least 8 characters" // Use binding to set error
//            binding.passwordEditText.requestFocus() // Use binding to request focus
//            return false
//        }
//        return true // All validations passed
//    }

    private fun validateFields(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Clear previous errors
        binding.emailEditText.error = null
        binding.passwordEditText.error = null

        // Use ValidationUtils only
        val emailError = ValidationUtils.validateEmail(email)
        val passwordError = ValidationUtils.validatePassword(password)

        var isValid = true

        if (emailError != null) {
            binding.emailEditText.error = emailError
            binding.emailEditText.requestFocus()
            isValid = false
        }

        if (passwordError != null) {
            binding.passwordEditText.error = passwordError
            if (isValid) binding.passwordEditText.requestFocus()
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}


