package project.nutriscan.ui

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import project.nutriscan.R
import project.nutriscan.databinding.FragmentProfileBinding
import project.nutriscan.viewmodel.AuthViewModel
import kotlin.getValue

class ProfileFragment : androidx.fragment.app.Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Profile"

        loadCheckboxValues()

        binding.emailTextView.text = "johndoe@gmail.com"


        binding.updateProfileButton.setOnClickListener {
            // Extract checkbox values
            val isGlutenChecked = binding.allergenGluten.isChecked
            val isNutsChecked = binding.allergenNuts.isChecked
            val isDairyChecked = binding.allergenDairy.isChecked
            val isSoyChecked = binding.allergenSoy.isChecked

            storeCheckboxValues(isGlutenChecked,isNutsChecked,isDairyChecked,isSoyChecked)

            val message =
                "Gluten: $isGlutenChecked, Nuts: $isNutsChecked, Dairy: $isDairyChecked, Soy: $isSoyChecked"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        //----------------Testing Screens-----------------.
        binding.testRegister.setOnClickListener {
            findNavController().navigate(R.id.register)
        }

        binding.testLogin.setOnClickListener {
            findNavController().navigate(R.id.login)
        }

        binding.logoutUser.setOnClickListener {
            showLogoutConfirmation()
        }


        //-------------------------------------------------


        return binding.root
    }

    private fun showLogoutConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                authViewModel.logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun storeCheckboxValues(isGlutenChecked: Boolean, isNutsChecked: Boolean, isDairyChecked: Boolean, isSoyChecked: Boolean) {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putBoolean("gluten", isGlutenChecked)
        editor.putBoolean("nuts", isNutsChecked)
        editor.putBoolean("dairy", isDairyChecked)
        editor.putBoolean("soy", isSoyChecked)
        editor.apply() // Save changes asynchronously
    }

    private fun loadCheckboxValues() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.allergenGluten.isChecked = sharedPreferences.getBoolean("gluten", false)
        binding.allergenNuts.isChecked = sharedPreferences.getBoolean("nuts", false)
        binding.allergenDairy.isChecked = sharedPreferences.getBoolean("dairy", false)
        binding.allergenSoy.isChecked = sharedPreferences.getBoolean("soy", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}