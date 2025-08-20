package project.nutriscan.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import project.nutriscan.MainActivity
import project.nutriscan.R
import project.nutriscan.databinding.FragmentLoginBinding
import project.nutriscan.utils.AuthState
import project.nutriscan.viewmodel.AuthViewModel
import kotlin.getValue

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)
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
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_to_HomeScreen)
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
        binding.loginUser.setOnClickListener {
            if (validateFields()) {
                val email = binding.emailLogin.text.toString().trim()
                val password = binding.passwordLogin.text.toString().trim()
                authViewModel.login(email, password)
            }
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginUser.isEnabled = !isLoading
        binding.loginUser.text = if (isLoading) "Logging in..." else "Login"
    }

    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_login_to_register)
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_login_to_HomeScreen)
    }

    private fun validateFields(): Boolean {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString().trim()

        // Clear previous errors
        binding.emailLogin.error = null
        binding.passwordLogin.error = null

        var isValid = true

        // Basic email validation
        if (email.isBlank()) {
            binding.emailLogin.error = "Email is required"
            binding.emailLogin.requestFocus()
            isValid = false
        }

        // Basic password validation (just check if empty for login)
        if (password.isBlank()) {
            binding.passwordLogin.error = "Password is required"
            if (isValid) binding.passwordLogin.requestFocus()
            isValid = false
        }

        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}