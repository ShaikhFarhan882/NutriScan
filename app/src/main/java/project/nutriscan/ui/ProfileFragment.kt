package project.nutriscan.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import project.nutriscan.R
import project.nutriscan.databinding.FragmentProfileBinding
import project.nutriscan.model.UserPreferences
import project.nutriscan.viewmodel.AuthViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        private const val PREFS_NAME = "NutriScanPrefs"

        // Personal Info Keys
        private const val KEY_USERNAME = "username"
        private const val KEY_BIO = "bio"

        //Health Goals
        private const val KEY_HEALTH_DIABETES = "health_diabetes"
        private const val KEY_HEALTH_HEART = "health_heart"
        private const val KEY_HEALTH_WEIGHT_LOSS = "health_weight_loss"
        private const val KEY_HEALTH_LOW_SODIUM = "health_low_sodium"

        // All 11 Allergen Keys (matching UserPreferences.AllergenCodes)
        private const val KEY_ALLERGEN_GLUTEN = "allergen_gluten"
        private const val KEY_ALLERGEN_MILK = "allergen_milk"
        private const val KEY_ALLERGEN_EGGS = "allergen_eggs"
        private const val KEY_ALLERGEN_PEANUTS = "allergen_peanuts"
        private const val KEY_ALLERGEN_NUTS = "allergen_nuts"
        private const val KEY_ALLERGEN_SOY = "allergen_soy"
        private const val KEY_ALLERGEN_FISH = "allergen_fish"
        private const val KEY_ALLERGEN_SHELLFISH = "allergen_shellfish"
        private const val KEY_ALLERGEN_SESAME = "allergen_sesame"
        private const val KEY_ALLERGEN_WHEAT = "allergen_wheat"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Profile"

        // Load saved preferences
        loadUserPreferences()

        // Setup click listeners
        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        // Update Profile Button
        binding.updateProfileButton.setOnClickListener {
            saveUserPreferences()
        }

        // Testing Screens
        binding.testRegister.setOnClickListener {
            findNavController().navigate(R.id.register)
        }

        binding.testLogin.setOnClickListener {
            findNavController().navigate(R.id.login)
        }

        // Logout Button
        binding.logoutUser.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun saveUserPreferences() {
        val username = binding.usernameProfile.text.toString().trim()
//        val bio = binding.bioProfile.text.toString().trim()

        // Validate username
        if (username.isEmpty()) {
            Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = requireActivity()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        sharedPreferences.edit().apply {
            // Save personal info
            putString(KEY_USERNAME, username)
            //putString(KEY_BIO, bio)

            //Save Health Goals
            putBoolean(KEY_HEALTH_DIABETES, binding.checkboxDiabetes.isChecked)
            putBoolean(KEY_HEALTH_HEART, binding.checkboxHeartHealth.isChecked)
            putBoolean(KEY_HEALTH_WEIGHT_LOSS, binding.checkboxWeightLoss.isChecked)
            putBoolean(KEY_HEALTH_LOW_SODIUM, binding.checkboxLowSodium.isChecked)

            // Save all 11 allergen preferences
            putBoolean(KEY_ALLERGEN_GLUTEN, binding.allergenGluten.isChecked)
            putBoolean(KEY_ALLERGEN_MILK, binding.allergenMilk.isChecked)
            putBoolean(KEY_ALLERGEN_EGGS, binding.allergenEggs.isChecked)
            putBoolean(KEY_ALLERGEN_PEANUTS, binding.allergenPeanuts.isChecked)
            putBoolean(KEY_ALLERGEN_NUTS, binding.allergenNuts.isChecked)
            putBoolean(KEY_ALLERGEN_SOY, binding.allergenSoy.isChecked)
            putBoolean(KEY_ALLERGEN_FISH, binding.allergenFish.isChecked)
            putBoolean(KEY_ALLERGEN_SHELLFISH, binding.allergenShellfish.isChecked)
            putBoolean(KEY_ALLERGEN_SESAME, binding.allergenSesame.isChecked)
            putBoolean(KEY_ALLERGEN_WHEAT, binding.allergenWheat.isChecked)
            apply()
        }

        // Show success message
        showSaveSuccessMessage()
    }

    private fun showSaveSuccessMessage() {
        Toast.makeText(
            requireContext(),
            "Profile updated successfully",
            Toast.LENGTH_SHORT
        ).show()

        // Show which allergens are enabled
        val enabledAllergens = getEnabledAllergens()

        if (enabledAllergens.isNotEmpty()) {
            val allergenList = enabledAllergens.joinToString(", ")
            Toast.makeText(
                requireContext(),
                "Active allergens: $allergenList",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "No allergen restrictions set",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getEnabledAllergens(): List<String> {
        val enabledAllergens = mutableListOf<String>()

        if (binding.allergenGluten.isChecked) enabledAllergens.add("Gluten")
        if (binding.allergenMilk.isChecked) enabledAllergens.add("Milk")
        if (binding.allergenEggs.isChecked) enabledAllergens.add("Eggs")
        if (binding.allergenPeanuts.isChecked) enabledAllergens.add("Peanuts")
        if (binding.allergenNuts.isChecked) enabledAllergens.add("Tree Nuts")
        if (binding.allergenSoy.isChecked) enabledAllergens.add("Soy")
        if (binding.allergenFish.isChecked) enabledAllergens.add("Fish")
        if (binding.allergenShellfish.isChecked) enabledAllergens.add("Shellfish")
        if (binding.allergenSesame.isChecked) enabledAllergens.add("Sesame")
        if (binding.allergenWheat.isChecked) enabledAllergens.add("Wheat")

        return enabledAllergens
    }

    private fun loadUserPreferences() {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load personal info
        val username = sharedPreferences.getString(KEY_USERNAME, "") ?: ""
        val bio = sharedPreferences.getString(KEY_BIO, "") ?: ""

        binding.usernameProfile.setText(username)
        //binding.bioProfile.setText(bio)

        //Load Health Goals
        binding.checkboxDiabetes.isChecked = sharedPreferences.getBoolean(KEY_HEALTH_DIABETES, false)
        binding.checkboxHeartHealth.isChecked = sharedPreferences.getBoolean(KEY_HEALTH_HEART, false)
        binding.checkboxWeightLoss.isChecked = sharedPreferences.getBoolean(KEY_HEALTH_WEIGHT_LOSS, false)
        binding.checkboxLowSodium.isChecked = sharedPreferences.getBoolean(KEY_HEALTH_LOW_SODIUM, false)

        // Load all 11 allergen checkboxes
        binding.allergenGluten.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_GLUTEN, false)
        binding.allergenMilk.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_MILK, false)
        binding.allergenEggs.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_EGGS, false)
        binding.allergenPeanuts.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_PEANUTS, false)
        binding.allergenNuts.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_NUTS, false)
        binding.allergenSoy.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_SOY, false)
        binding.allergenFish.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_FISH, false)
        binding.allergenShellfish.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_SHELLFISH, false)
        binding.allergenSesame.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_SESAME, false)
        binding.allergenWheat.isChecked =
            sharedPreferences.getBoolean(KEY_ALLERGEN_WHEAT, false)
    }

    private fun showLogoutConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                authViewModel.logout()

                // Optional: Clear preferences on logout
                clearUserPreferences()

                Toast.makeText(
                    requireContext(),
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to login screen (if needed)
                // findNavController().navigate(R.id.action_profileFragment_to_login)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearUserPreferences() {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        sharedPreferences.edit().clear().apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
