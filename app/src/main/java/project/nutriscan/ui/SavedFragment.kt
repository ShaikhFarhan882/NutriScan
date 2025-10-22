package project.nutriscan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import project.nutriscan.MainActivity
import project.nutriscan.ui.SavedProductAdapter
import project.nutriscan.databinding.FragmentSavedBinding
import project.nutriscan.viewmodel.NutritionViewModel

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NutritionViewModel
    private lateinit var savedProductAdapter: SavedProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Saved Products"

        // Get ViewModel from MainActivity
        viewModel = (activity as MainActivity).viewModel

        // Setup RecyclerView
        setupRecyclerView()

        // Setup swipe to delete
        setupSwipeToDelete()

        // Observe saved products
        observeSavedProducts()

        return binding.root
    }

    private fun setupRecyclerView() {
        savedProductAdapter = SavedProductAdapter()
        binding.savedProductsRecyclerView.apply {
            adapter = savedProductAdapter
            setHasFixedSize(true)
        }

        // Item click - navigate to details
        savedProductAdapter.setOnItemClickListener { savedProduct ->
            val action = SavedFragmentDirections
                .actionSavedFragmentToProductDetailFragment(savedProduct.barcode)
            findNavController().navigate(action)
        }

        // Delete button click
        savedProductAdapter.setOnDeleteClickListener { savedProduct ->
            showDeleteDialog(savedProduct.barcode, savedProduct.productName)
        }
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val product = savedProductAdapter.savedProducts[viewHolder.adapterPosition]
                viewModel.deleteProduct(product.barcode)

                Snackbar.make(binding.root, "Product removed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        // Could implement undo if needed
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.savedProductsRecyclerView)
    }

    private fun observeSavedProducts() {
        viewModel.savedProducts.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                showEmptyState()
            } else {
                showRecyclerView()
                savedProductAdapter.savedProducts = products
            }
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.savedProductsRecyclerView.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.savedProductsRecyclerView.visibility = View.VISIBLE
    }

    private fun showDeleteDialog(barcode: String, productName: String?) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Product")
            .setMessage("Remove ${productName ?: "this product"} from favorites?")
            .setPositiveButton("Remove") { _, _ ->
                viewModel.deleteProduct(barcode)
                Snackbar.make(binding.root, "Product removed", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
