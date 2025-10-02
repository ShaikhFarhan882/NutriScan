package project.nutriscan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import project.nutriscan.databinding.FragmentAllergenDetailsBinding
import project.nutriscan.model.AllergenInfo
import project.nutriscan.utils.AllergenUtils

class AllergensDetailFragment : Fragment() {

    private var _binding: FragmentAllergenDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AllergensDetailFragmentArgs by navArgs()

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

        binding.allergenCount.text = "$allergenCount allergen(s) detected"
        // Show warning if high-risk allergens present

        // Show no allergens card if empty
        if (allergenCount == 0) {
            binding.noAllergensCard.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
            binding.summaryCard.visibility = View.GONE
        } else {
            binding.noAllergensCard.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
            binding.summaryCard.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        val allergens = args.allergens
        val allergensList: List<AllergenInfo> = AllergenUtils.parseAllergensToDetailedList(allergens)

        val allergensAdapter = AllergensDetailAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = allergensAdapter
        }
        allergensAdapter.submitList(allergensList)
    }

    private fun setupClickListeners() {
        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
