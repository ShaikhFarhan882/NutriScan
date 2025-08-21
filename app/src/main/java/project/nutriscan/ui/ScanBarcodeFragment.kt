package project.nutriscan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import project.nutriscan.databinding.FragmentScanBarcodeBinding

class ScanBarcodeFragment : Fragment() {
    private var _binding: FragmentScanBarcodeBinding? = null
    private val binding get() = _binding!!
    private var barcode: String = ""
    private var codeScanner: CodeScanner? = null

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
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setupScanner()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        codeScanner = null
    }

    private fun setupScanner() {
        if (codeScanner == null) {
            val scannerView = binding.scannerView
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner?.decodeCallback = DecodeCallback { result ->
                activity.runOnUiThread {
                    barcode = result.text.toString()
                    binding.currentScannedBarcode.text = barcode
                    binding.barcodeDisplayCard.visibility = View.VISIBLE
                    binding.ScanForProduct.isEnabled = true
                }
            }
            scannerView.setOnClickListener {
                codeScanner?.startPreview()
            }
        }
    }

    private fun setupClickListeners() {
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
}
