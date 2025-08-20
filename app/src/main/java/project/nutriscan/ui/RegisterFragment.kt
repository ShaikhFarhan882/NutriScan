package project.nutriscan.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.util.findColumnIndexBySuffix
import project.nutriscan.R
import project.nutriscan.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.RegisterUser.setOnClickListener {
            if (validateFields()) {
                Toast.makeText(requireActivity(), "Registered Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.loginLink.setOnClickListener {
            navigateRegisterToLogin()
        }

        return binding.root


    }

    private fun navigateRegisterToLogin(){
        findNavController().navigate(R.id.action_register_to_login)
    }

    private fun validateFields(): Boolean {
        // Accessing the text from EditText using View Binding
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Validate username
        // Validate email
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required" // Use binding to set error
            binding.emailEditText.requestFocus() // Use binding to request focus
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please enter a valid email" // Use binding to set error
            binding.emailEditText.requestFocus() // Use binding to request focus
            return false
        }
        // Validate password
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required" // Use binding to set error
            binding.passwordEditText.requestFocus() // Use binding to request focus
            return false
        } else if (password.length < 8) {
            binding.passwordEditText.error =
                "Password must be at least 8 characters" // Use binding to set error
            binding.passwordEditText.requestFocus() // Use binding to request focus
            return false
        }
        return true // All validations passed
    }
}