package project.nutriscan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import project.nutriscan.R
import project.nutriscan.databinding.FragmentAdditivesDetailBinding
import project.nutriscan.model.AdditiveInfo
import project.nutriscan.utils.UtilityFunctions

class AdditivesDetailFragment : Fragment() {

    private var _binding: FragmentAdditivesDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AdditivesDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdditivesDetailBinding.inflate(inflater, container, false)

        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the additives data from Safe Args
        val additivesTags: List<String> = args.additives.toList()

        // Convert to AdditiveInfo objects
        val additivesInfoList: List<AdditiveInfo> = additivesTags.map {
            UtilityFunctions.getFullAdditiveInfo(it)
        }

        setupRecyclerView(additivesInfoList)
    }


    private fun setupRecyclerView(additivesInfoList: List<AdditiveInfo>) {
        val additivesAdapter = AdditivesDetailAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = additivesAdapter
        }
        additivesAdapter.submitList(additivesInfoList)
    }

}