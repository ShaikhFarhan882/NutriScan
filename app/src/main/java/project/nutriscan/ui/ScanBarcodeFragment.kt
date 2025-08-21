package project.nutriscan.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import project.nutriscan.MainActivity
import project.nutriscan.databinding.FragmentScanBarcodeBinding

class ScanBarcodeFragment : Fragment() {
    private var _binding: FragmentScanBarcodeBinding? = null
    private val binding get() = _binding!!
    private var barcode: String = ""

    private lateinit var codeScanner: CodeScanner

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        (activity as MainActivity).hideBottomNavigation()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBarcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScanner()
        setupClickListeners()


    }

    private fun setupScanner() {
        val scannerView = binding.scannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)

        codeScanner.decodeCallback = DecodeCallback { result ->
            activity.runOnUiThread {
                barcode = result.text.toString()

                // Update UI with scanned barcode
                binding.currentScannedBarcode.text = barcode
                binding.barcodeDisplayCard.visibility = View.VISIBLE

                // Enable the search button
                binding.ScanForProduct.isEnabled = true
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun setupClickListeners() {
        // Set up search button click listener ONCE
        binding.ScanForProduct.setOnClickListener {
            handleSearchClick()
        }
    }

    private fun handleSearchClick() {
        if (barcode.isNotEmpty()) {
            if (isValidEan(barcode)) {
                try {
                    val action = ScanBarcodeFragmentDirections
                        .actionScanBarcodeFragmentToProductDetailFragment(barcode)
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), "Invalid barcode format", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireActivity(), "No barcode detected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEan(barcode: String): Boolean {
        // Support both EAN-13 and UPC-A (12 digits)
        if (barcode.length != 13 && barcode.length != 12) return false

        return try {
            val allDigits = barcode.map { it.toString().toInt() }
            val s = if (barcode.length % 2 == 0) 3 else 1
            val s2 = if (s == 3) 1 else 3
            allDigits.last() == (10 - (allDigits.take(barcode.length - 1)
                .mapIndexed { ci, c -> c * (if (ci % 2 == 0) s else s2) }.sum() % 10)) % 10
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

//    override fun onDetach() {
//        super.onDetach()
//        (requireActivity() as AppCompatActivity).supportActionBar?.show()
//        (activity as MainActivity).showBottomNavigation()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
