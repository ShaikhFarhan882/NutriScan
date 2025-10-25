package project.nutriscan.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import project.nutriscan.R
import project.nutriscan.databinding.FragmentAllergenDetailsBinding
import project.nutriscan.model.AllergenInfo
import project.nutriscan.model.UserPreferences
import project.nutriscan.utils.AllergenUtils

class AllergensDetailFragment : Fragment() {

    private var _binding: FragmentAllergenDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AllergensDetailFragmentArgs by navArgs()

    companion object {
        private const val PREFS_NAME = "NutriScanPrefs"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllergenDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupUI() {
        val allergens = args.allergens
        val allergenCount = AllergenUtils.getAllergenCountRobust(allergens)

        val userPrefs = loadUserPreferences()
        val allergensList: List<AllergenInfo> =
            AllergenUtils.parseAllergensToDetailedList(allergens, userPrefs)

        val userAllergenCount = allergensList.count { it.isUserAllergen }
        val userAllergensList = allergensList.filter { it.isUserAllergen }

        binding.allergenCount.text = "$allergenCount allergen(s)"

        if (allergenCount == 0) {
            // Show no allergens card, hide everything else
            binding.noAllergensCard.visibility = View.VISIBLE
            binding.mainCard.visibility = View.GONE
            binding.warningCard.visibility = View.GONE
            binding.detailsHeader.visibility = View.GONE
            binding.recyclerview.visibility = View.GONE
        } else {
            // Show main card and list
            binding.noAllergensCard.visibility = View.GONE
            binding.mainCard.visibility = View.VISIBLE
            binding.detailsHeader.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.VISIBLE

            // Show appropriate alert section
            showUserAllergenStatus(userAllergenCount, userAllergensList)

            // Show general warning if needed
            showGeneralWarningIfNeeded(allergensList)

            // Update summary
            updateSummary(allergenCount, userAllergenCount, allergensList)
        }
    }

    private fun showUserAllergenStatus(count: Int, userAllergens: List<AllergenInfo>) {
        if (count > 0) {
            // Show WARNING section
            binding.userAllergenWarningSection.visibility = View.VISIBLE
            binding.userSafeSection.visibility = View.GONE
            binding.sectionDivider.visibility = View.VISIBLE

            // Update count text
            val countText = if (count == 1) {
                "Contains 1 of YOUR allergens"
            } else {
                "Contains $count of YOUR allergens"
            }
            binding.userAllergenCountText.text = countText

            // Update allergen list with bullet points
            val allergenNames = userAllergens.joinToString("\n") { "• ${it.fullName}" }
            binding.userAllergensList.text = allergenNames

        } else {
            // Show SAFE section
            binding.userAllergenWarningSection.visibility = View.GONE
            binding.userSafeSection.visibility = View.VISIBLE
            binding.sectionDivider.visibility = View.VISIBLE
        }
    }


    private fun showGeneralWarningIfNeeded(allergensList: List<AllergenInfo>) {
        val highRiskCount = allergensList.count { it.riskLevel == "HIGH RISK" }

        if (highRiskCount > 0) {
            binding.warningCard.visibility = View.VISIBLE
            val warningEmoji = getString(R.string.warning_emoji)
            binding.warningText.text =
                "$warningEmoji Contains $highRiskCount high-risk allergen(s) that may cause severe reactions"
        } else {
            binding.warningCard.visibility = View.GONE
        }
    }


    private fun updateSummary(total: Int, userCount: Int, allergensList: List<AllergenInfo>) {
        val highRiskCount = allergensList.count { it.riskLevel == "HIGH RISK" }
        val mediumRiskCount = allergensList.count { it.riskLevel == "MEDIUM RISK" }
        val lowRiskCount = allergensList.count { it.riskLevel == "LOW RISK" }

        val summaryText = buildString {
            append("Total Allergens: $total\n")
            if (userCount > 0) {
                append("⚠️ Your Allergens: $userCount\n")
            }
            append("High Risk: $highRiskCount\n")
            append("Medium Risk: $mediumRiskCount\n")
            append("Low Risk: $lowRiskCount")
        }

        binding.allergenSummary.text = summaryText
    }

    private fun setupRecyclerView() {
        val allergens = args.allergens
        val userPrefs = loadUserPreferences()

        // Parse allergens with user preferences
        val allergensList: List<AllergenInfo> =
            AllergenUtils.parseAllergensToDetailedList(allergens, userPrefs)

        val allergensAdapter = AllergensDetailAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = allergensAdapter
        }

        // Sort: User allergens first, then by risk level
        val sortedList = allergensList.sortedWith(
            compareByDescending<AllergenInfo> { it.isUserAllergen }
                .thenByDescending {
                    when (it.riskLevel) {
                        "HIGH RISK" -> 3
                        "MEDIUM RISK" -> 2
                        "LOW RISK" -> 1
                        else -> 0
                    }
                }
        )

        allergensAdapter.submitList(sortedList)
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Navigate to profile to edit preferences
        binding.editPreferencesButton?.setOnClickListener {
            findNavController().navigate(R.id.Profile)
        }
    }

    private fun loadUserPreferences(): UserPreferences {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load allergen preferences using the new key format
        val allergenPreferences = mapOf(
            "gluten" to sharedPreferences.getBoolean("allergen_gluten", false),
            "milk" to sharedPreferences.getBoolean("allergen_milk", false),
            "eggs" to sharedPreferences.getBoolean("allergen_eggs", false),
            "peanuts" to sharedPreferences.getBoolean("allergen_peanuts", false),
            "nuts" to sharedPreferences.getBoolean("allergen_nuts", false),
            "soybeans" to sharedPreferences.getBoolean("allergen_soy", false),
            "fish" to sharedPreferences.getBoolean("allergen_fish", false),
            "shellfish" to sharedPreferences.getBoolean("allergen_shellfish", false),
            "sesame-seeds" to sharedPreferences.getBoolean("allergen_sesame", false),
            "wheat" to sharedPreferences.getBoolean("allergen_wheat", false)
        )

        return UserPreferences(
            username = sharedPreferences.getString("username", "") ?: "",
            bio = sharedPreferences.getString("bio", "") ?: "",
            allergenPreferences = allergenPreferences
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
