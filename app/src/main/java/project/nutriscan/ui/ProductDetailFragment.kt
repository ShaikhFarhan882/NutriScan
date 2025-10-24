package project.nutriscan.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import project.nutriscan.MainActivity
import project.nutriscan.R
import project.nutriscan.databinding.FragmentProductDetailBinding
import project.nutriscan.model.Product
import project.nutriscan.utils.UtilityFunctions.Companion.getAdditiveFullName
import project.nutriscan.viewmodel.NutritionViewModel

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var viewmodel: NutritionViewModel

    private val args: ProductDetailFragmentArgs by navArgs()

    private var currentProduct: Product? = null
    private var currentBarcode: String? = null
    private var isSustainable: Boolean = false
    private var sustainabilityLevel: String? = null
    private var hasPalmOil: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = (activity as MainActivity).viewModel
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailBinding.inflate(inflater)

        //
        binding.lifecycleOwner = viewLifecycleOwner

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Product Details"

        //API Call to retrieve data.
        val barcode = args.barcode

        currentBarcode = args.barcode
        //Only call checkIfProductSaved if barcode is not null
        if (currentBarcode != null) {
            viewmodel.checkIfProductSaved(currentBarcode!!)
        }

        viewmodel.searchProduct(
            barcode,
            "nutriments,allergens,image_url," +
                    "additives_tags,ingredients_text_en,ingredients," +
                    "countries,ecoscore_grade,ecoscore_score," +
                    "nutrient_levels_tags,product_name,brands,manufacturing_places," +
                    // Palm Oil Detection Fields
                    "ingredients_from_palm_oil_n," +
                    "ingredients_from_palm_oil," +
                    "ingredients_from_palm_oil_tags," +
                    "ingredients_that_may_be_from_palm_oil_n," +
                    "ingredients_that_may_be_from_palm_oil," +
                    "ingredients_that_may_be_from_palm_oil_tags," +
                    "ingredients_analysis_tags," +
                    // Additional useful fields
                    "ingredients_tags," +
                    "labels," +
                    "labels_tags," +
                    "packagings"
        )

        //Observing and Mapping Values.
        viewmodel.productDetails.observe(viewLifecycleOwner, Observer {

            //STORE THE PRODUCT FIRST - BEFORE ANYTHING ELSE
            currentProduct = it.product

            //Add null check and early return if product is null
            if (currentProduct == null) {
                Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
                binding.saveProductButton.isEnabled = false
                binding.saveProductButton.text = "Product Not Available"
                return@Observer
            }

            // Enable save button now that we have product data
            binding.saveProductButton.isEnabled = true
            binding.saveProductButton.text = "Save Product"

            //Detect palm oil
            hasPalmOil = detectPalmOil(currentProduct!!)

            //Store sustainability info RIGHT AFTER storing product
            isSustainable = checkSustainablePalmOil(currentProduct?.labels)
            sustainabilityLevel = getSustainabilityLevel(currentProduct?.labels)

            //Palm Oil
            displayPalmOilInfo(it.product)

            //Product Info Card
            binding.productName.text =
                "Name: " + (it.product?.product_name?.toString() ?: "Not Found")

            binding.productBrands.text =
                "Brand: " + (it.product?.brands?.toString() ?: "Not Found")

            binding.productManufacturing.text =
                "Manufactured In: " + (it.product?.manufacturing_places?.toString() ?: "Not Found")


            //Nutriments Card
            binding.carbs.text =
                "Carbs: " + (it.product?.nutriments?.carbohydrates?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.carbohydrates_unit ?: "")

            binding.sugar.text =
                "Sugar: " + (it.product?.nutriments?.sugars?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.sugars_unit ?: "")

            binding.energy.text =
                "Energy: " + (it.product?.nutriments?.energy?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.energy_unit ?: "")

            binding.fat.text = "Fat: " + (it.product?.nutriments?.fat?.toString() ?: "Not Found") +
                    (it.product?.nutriments?.fat_unit ?: "")

            binding.fatPer100g.text =
                "Fat per 100g: " + (it.product?.nutriments?.fat_100g?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.fat_unit ?: "")

            binding.fiber.text =
                "Fiber: " + (it.product?.nutriments?.fiber?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.fiber_unit ?: "")

//            binding.nutritionalScore.text =
//                "Nutritional Score: " + (it.product?.nutriments?.nutrition_score_fr?.toString()
//                    ?: "Not Found")

            it.product?.nutriments?.nutrition_score_fr?.let { score ->
                // Convert Double to Int
                val scoreInt = score.toInt()
                // Set the text
                binding.nutritionalScore.text = "Nutritional Score: $scoreInt/100"
                // Set dynamic card background color
                binding.nutritionalScoreCard.setCardBackgroundColor(getScoreColor(scoreInt))
                // Set text color for readability
                binding.nutritionalScore.setTextColor(getScoreTextColor(scoreInt))
            } ?: run {
                // Handle null case
                binding.nutritionalScore.text = "Nutritional Score: Not Found"
                binding.nutritionalScoreCard.setCardBackgroundColor(Color.parseColor("#9E9E9E"))
                binding.nutritionalScore.setTextColor(Color.WHITE)
            }

            binding.proteins.text =
                "Protein: " + (it.product?.nutriments?.proteins?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.proteins_unit ?: "")

            binding.salt.text =
                "Salt Content: " + (it.product?.nutriments?.salt?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.salt_unit ?: "")

            binding.saturatedFat.text =
                "Saturated Fat: " + (it.product?.nutriments?.saturated_fat?.toString()
                    ?: "Not Found") +
                        (it.product?.nutriments?.saturated_fat_unit ?: "")

            binding.sodium.text =
                "Sodium: " + (it.product?.nutriments?.sodium?.toString() ?: "Not Found") +
                        (it.product?.nutriments?.sodium_unit ?: "")


            /*  //ProductName
              binding.productName.text = "Name: " + it.product?.product_name*/

            //Ingredients
            binding.ingredientsInfo.text =
                "Contains: ${it.product?.ingredients_text_en ?: "No ingredients available"}"

            //Nutrients_Level Summary
            val nutrientsTags = (it?.product?.nutrient_levels_tags)
            convertNutrientsSummary(nutrientsTags)

            //Allergens
//            val formattedAllergens = formatAllergens(it.product?.allergens)
//            binding.allergen.text = formattedAllergens
//            //binding.allergen.setTextColor(Color.RED)

            val allergensTags = (it?.product?.allergens)
            displayAllergensTags(allergensTags)


            //Product Image
            val imageUrl = it.product?.image_url
            binding.productImage.load(imageUrl)

            // Extract additives tags
            val additivesTags = (it?.product?.additives_tags)
            displayAdditivesTags(additivesTags)

            //Countries Found
            val formattedCountries = formatCountries(it.product?.countries)
            binding.countries.text = formattedCountries


            //Ecoscore
            binding.ecoscoreGrade.text =
                "Grade: ${it.product?.ecoscore_grade ?: "Not Found"}"
            binding.ecoscoreScore.text =
                "Score: ${it.product?.ecoscore_score ?: "Not Found"}"


            //Packaging Info
            displayPackagingInfo(it.product)

        })

        //DB Operations
        viewmodel.isProductSaved.observe(viewLifecycleOwner) { isSaved ->
            isSaved?.let {
                updateSaveButton(it)
            }
        }

        // Setup button
        setupSaveButton()

        return binding.root

    }

    private fun convertNutrientsSummary(nutrientsTags: List<String>?) {
        if (!nutrientsTags.isNullOrEmpty()) {
            val formattedTag = nutrientsTags.mapIndexed { index, tag ->
                val convertedTag = tag.removePrefix("en:") // Remove the "en:" prefix
                "${index + 1}. $convertedTag" // Format as "1. fat-in-high-quantity"
            }.joinToString("\n") // Join the list into a single string
            binding.nutrientsLevel.text = "List:\n$formattedTag"
        } else {
            binding.additives.text = "Cannot Generate Summary"
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun displayAdditivesTags(additivesTags: List<String>?) {
        if (!additivesTags.isNullOrEmpty()) {
            val formattedAdditives = additivesTags.map { tag ->
                val convertedTag = convertTagFormat(tag)
                val fullName = getAdditiveFullName(convertedTag)
                "$convertedTag - $fullName"
            }.joinToString("\n") // Only returns a list of strings!

            binding.additives.text = "Additives:\n$formattedAdditives"

            // Set click listener and properties ONCE, not for each tag
            binding.additives.setOnClickListener {
                navigateToAdditivesDetail(additivesTags)
            }
            binding.additives.isClickable = true
            binding.additives.isFocusable = true

        } else {
            //onclick();
            binding.additives.text = "Additives not found."
            binding.additives.setOnClickListener(null)
            binding.additives.isClickable = false
            binding.additives.isFocusable = false
        }
    }


    fun convertTagFormat(tag: String): String {
        // Remove the "en:" prefix and convert to uppercase
        return tag.replace("en:", "").uppercase()
    }

    //

    private fun displayAllergensTags(allergensTags: String?) {
        if (!allergensTags.isNullOrEmpty()) {
            val allergenList = allergensTags.split(",").map { it.trim() }
            val formattedAllergens = allergenList.mapIndexed { index, tag ->
                val convertedTag = convertAllergenTagFormat(tag)
                val fullName = getAllergenFullName(convertedTag)
                "${index + 1}. $convertedTag - $fullName"
            }.joinToString("\n")

            binding.allergen.text = "Allergens:\n$formattedAllergens"

            // Set click listener and properties ONCE
            binding.allergen.setOnClickListener {
                navigateToAllergensDetail(allergensTags)
            }
            binding.allergen.isClickable = true
            binding.allergen.isFocusable = true

        } else {
            binding.allergen.text = "No allergens detected."
            binding.allergen.setOnClickListener(null)
            binding.allergen.isClickable = false
            binding.allergen.isFocusable = false
        }
    }

    private fun convertAllergenTagFormat(tag: String): String {
        return tag.replace("en:", "").trim().replaceFirstChar { it.uppercase() }
    }

    /**
     * Gets the full name/description for an allergen
     */
    private fun getAllergenFullName(allergenCode: String): String {
        return when (allergenCode.lowercase()) {
            "gluten" -> "Gluten (Wheat Protein)"
            "milk" -> "Milk and Dairy Products"
            "eggs" -> "Eggs and Egg Products"
            "peanuts" -> "Peanuts (Groundnuts)"
            "tree nuts", "nuts" -> "Tree Nuts (Various)"
            "soy", "soya", "soybeans" -> "Soy and Soy Products"
            "fish" -> "Fish and Fish Products"
            "shellfish" -> "Crustaceans and Mollusks"
            "sesame", "sesame-seeds" -> "Sesame Seeds and Oil"
            "wheat" -> "Wheat and Wheat Products"
            "celery" -> "Celery and Celeriac"
            "mustard" -> "Mustard Seeds and Powder"
            "lupin" -> "Lupin Beans and Flour"
            "sulfites", "sulphites", "sulphur-dioxide-and-sulphites" -> "Sulfur Dioxide and Sulfites"
            else -> "Unknown Allergen"
        }
    }

    //


    fun formatAllergens(allergens: String?): String {
        if (allergens.isNullOrEmpty()) {
            return "No allergens available"
        }

        val allergenList = allergens.split(",").map { it.trim() }
        return allergenList.mapIndexed { index, allergen ->
            "${index + 1}. ${
                allergen.replace(
                    "en:",
                    ""
                )
            }"
        }
            .joinToString("\n")
    }


    fun formatCountries(countries: String?): String {
        if (countries.isNullOrEmpty()) {
            return "Cannot Determine"
        }

        val countriesList = countries.split(",").map { it.trim() }
        return countriesList.mapIndexed { index, countries ->
            "${index + 1}. ${countries}"
        }
            .joinToString("\n")
    }

    /**
     * Display palm oil detection information with improved detection
     */
    @SuppressLint("SetTextI18n")
    private fun displayPalmOilInfo(product: Product?) {
        if (product == null) {
            binding.palmOilCard.visibility = View.GONE
            return
        }

        // PRIMARY: Check dedicated palm oil fields
        val palmOilCount = product.ingredients_from_palm_oil_n ?: 0
        val mayContainCount = product.ingredients_that_may_be_from_palm_oil_n ?: 0

        // FALLBACK: Check ingredients text for palm oil keywords
        val ingredientsText =
            (product.ingredients_text_en ?: product.ingredients_text ?: "").lowercase()
        val ingredientsTags = product.ingredients_tags ?: emptyList()

        // Check if palm oil is mentioned in text or tags
        val hasPalmOilInText = ingredientsText.contains("palm oil") ||
                ingredientsText.contains("palm kernel") ||
                ingredientsText.contains("palm fat")

        val hasPalmOilInTags = ingredientsTags.any { tag ->
            tag.contains("palm", ignoreCase = true)
        }

        // ADDITIONAL: Check ingredients_analysis_tags
        val analysisTags = product.ingredients_analysis_tags ?: emptyList()
        val isPalmOilFree = analysisTags.any { it.contains("palm-oil-free", ignoreCase = true) }
        val hasPalmOilInAnalysis = analysisTags.any {
            it.contains("palm-oil", ignoreCase = true) && !it.contains("free", ignoreCase = true)
        }

        // Check for sustainable certification
        val isSustainable = checkSustainablePalmOil(product.labels)
        val sustainabilityLevel = getSustainabilityLevel(product.labels)

        // Debug logging
        Log.d("PalmOilDebug", "=== Palm Oil Detection ===")
        Log.d("PalmOilDebug", "palmOilCount: $palmOilCount")
        Log.d("PalmOilDebug", "mayContainCount: $mayContainCount")
        Log.d("PalmOilDebug", "hasPalmOilInText: $hasPalmOilInText")
        Log.d("PalmOilDebug", "hasPalmOilInTags: $hasPalmOilInTags")
        Log.d("PalmOilDebug", "hasPalmOilInAnalysis: $hasPalmOilInAnalysis")
        Log.d("PalmOilDebug", "isPalmOilFree: $isPalmOilFree")
        Log.d("PalmOilDebug", "isSustainable: $isSustainable")
        Log.d("PalmOilDebug", "sustainabilityLevel: $sustainabilityLevel")
        Log.d("PalmOilDebug", "ingredients_tags: ${ingredientsTags.take(5)}")
        Log.d("PalmOilDebug", "=========================")

        binding.palmOilCard.visibility = View.VISIBLE

        // Hide all layouts first
        binding.containsPalmOilLayout.visibility = View.GONE
        binding.mayContainPalmOilLayout.visibility = View.GONE
        binding.palmOilFreeLayout.visibility = View.GONE

        when {
            // CASE 1: Definitely contains palm oil (from dedicated fields OR text/tags)
            palmOilCount > 0 || hasPalmOilInText || hasPalmOilInTags || hasPalmOilInAnalysis -> {
                binding.containsPalmOilLayout.visibility = View.VISIBLE

                // Build message
                val message = if (palmOilCount > 0) {
                    "$palmOilCount ingredient(s) from palm oil"
                } else {
                    "Contains palm oil"
                }

                binding.palmOilCount.text = message

                // Add ingredient names if available
                val ingredients = product.ingredients_from_palm_oil_tags
                    ?: product.ingredients_from_palm_oil
                    ?: extractPalmOilFromTags(ingredientsTags)

                if (ingredients.isNotEmpty()) {
                    val formattedIngredients =
                        ingredients.take(3).joinToString(", ") { ingredient ->
                            ingredient.replace("en:", "")
                                .replace("-", " ")
                                .replace("_", " ")
                                .split(" ")
                                .joinToString(" ") { it.capitalize() }
                        }
                    val more = if (ingredients.size > 3) " and ${ingredients.size - 3} more" else ""
                    binding.palmOilCount.text =
                        "${message}\n\nIngredients:\n$formattedIngredients$more"
                }

                // Add sustainability badge
                if (isSustainable) {
                    val badge = when (sustainabilityLevel) {
                        "identity-preserved" -> "\n\n✓ RSPO Identity Preserved"
                        "segregated" -> "\n\n✓ RSPO Segregated"
                        "mass-balance" -> "\n\n✓ RSPO Mass Balance"
                        else -> "\n\n✓ RSPO Certified"
                    }
                    binding.palmOilCount.text = binding.palmOilCount.text.toString() + badge
                    binding.palmOilWarningIcon.setColorFilter(Color.parseColor("#FF6F00"))
                }
            }

            // CASE 2: May contain palm oil
            mayContainCount > 0 -> {
                binding.mayContainPalmOilLayout.visibility = View.VISIBLE
                binding.mayContainPalmOilCount.text =
                    "$mayContainCount ingredient(s) may contain palm oil"

                val ingredients = product.ingredients_that_may_be_from_palm_oil_tags
                    ?: product.ingredients_that_may_be_from_palm_oil
                    ?: emptyList()

                if (ingredients.isNotEmpty()) {
                    val formattedIngredients =
                        ingredients.take(3).joinToString(", ") { ingredient ->
                            ingredient.replace("en:", "").replace("-", " ")
                                .split(" ").joinToString(" ") { it.capitalize() }
                        }
                    val more = if (ingredients.size > 3) " and ${ingredients.size - 3} more" else ""
                    binding.mayContainPalmOilCount.text =
                        "$mayContainCount ingredient(s):\n$formattedIngredients$more"
                }

                if (isSustainable) {
                    binding.mayContainPalmOilCount.text =
                        binding.mayContainPalmOilCount.text.toString() +
                                "\n\n✓ Sustainable sources possible"
                }
            }

            // CASE 3: Palm oil free (explicit or by absence)
            isPalmOilFree || (palmOilCount == 0 && mayContainCount == 0 && !hasPalmOilInText && !hasPalmOilInTags) -> {
                binding.palmOilFreeLayout.visibility = View.VISIBLE
                binding.palmOilFreeDescription.text = if (isPalmOilFree) {
                    "Certified palm oil free"
                } else {
                    "No palm oil detected"
                }
            }

            // CASE 4: Unknown
            else -> {
                binding.palmOilFreeLayout.visibility = View.VISIBLE
                binding.palmOilFreeDescription.text = "Palm oil status unknown"
            }
        }
    }

    /**
     * Extract palm oil related ingredients from tags
     */
    private fun extractPalmOilFromTags(tags: List<String>): List<String> {
        return tags.filter { tag ->
            tag.contains("palm", ignoreCase = true)
        }
    }

    /**
     * Check if product contains sustainable palm oil
     */
    private fun checkSustainablePalmOil(labels: String?): Boolean {
        if (labels.isNullOrEmpty()) return false

        val sustainableKeywords = listOf(
            "sustainable palm oil",
            "sustainable-palm-oil",
            "rspo",
            "certified sustainable palm oil",
            "rspo-certified",
            "roundtable on sustainable palm oil"
        )

        return sustainableKeywords.any { keyword ->
            labels.contains(keyword, ignoreCase = true)
        }
    }

    /**
     * Get the sustainability certification level
     */
    private fun getSustainabilityLevel(labels: String?): String? {
        if (labels.isNullOrEmpty()) return null

        return when {
            labels.contains("identity preserved", ignoreCase = true) ||
                    labels.contains("identity-preserved", ignoreCase = true) -> "identity-preserved"

            labels.contains("segregated", ignoreCase = true) -> "segregated"

            labels.contains("mass balance", ignoreCase = true) ||
                    labels.contains("mass-balance", ignoreCase = true) -> "mass-balance"

            labels.contains("rspo", ignoreCase = true) ||
                    labels.contains("sustainable palm oil", ignoreCase = true) -> "rspo"

            else -> null
        }
    }

    private fun detectPalmOil(product: Product): Boolean {
        // Method 1: Check dedicated API fields
        val palmOilCount = product.ingredients_from_palm_oil_n ?: 0
        if (palmOilCount > 0) {
            Log.d("PalmOilDetect", "Detected via API field: $palmOilCount")
            return true
        }

        // Method 2: Check ingredients text
        val ingredientsText =
            (product.ingredients_text_en ?: product.ingredients_text ?: "").lowercase()
        if (ingredientsText.contains("palm oil") ||
            ingredientsText.contains("palm kernel") ||
            ingredientsText.contains("palm fat")
        ) {
            Log.d("PalmOilDetect", "Detected via ingredients text")
            return true
        }

        // Method 3: Check ingredients tags
        val ingredientsTags = product.ingredients_tags ?: emptyList()
        if (ingredientsTags.any { it.contains("palm", ignoreCase = true) }) {
            Log.d("PalmOilDetect", "Detected via ingredients tags")
            return true
        }

        // Method 4: Check analysis tags
        val analysisTags = product.ingredients_analysis_tags ?: emptyList()
        if (analysisTags.any {
                it.contains("palm-oil", ignoreCase = true) &&
                        !it.contains("free", ignoreCase = true)
            }) {
            Log.d("PalmOilDetect", "Detected via analysis tags")
            return true
        }

        Log.d("PalmOilDetect", "No palm oil detected")
        return false
    }


    //Navigation
    private fun navigateToAdditivesDetail(additivesTags: List<String>) {
        val action = ProductDetailFragmentDirections
            .actionProductDetailFragmentToAdditivesDetailFragment(additivesTags.toTypedArray())
        findNavController().navigate(action)
    }

    private fun navigateToAllergensDetail(allergens: String) {
        val action = ProductDetailFragmentDirections
            .actionProductDetailFragmentToAllergenDetails(allergens ?: "")
        findNavController().navigate(action)
    }

    //RoomDB
    private fun setupSaveButton() {
        binding.saveProductButton.setOnClickListener {
            toggleSaveProduct()
        }
    }

    private fun toggleSaveProduct() {
        if (currentProduct == null || currentBarcode == null) {
            Toast.makeText(requireContext(), "Product data not available", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (viewmodel.isProductSaved.value == true) {
            // Delete
            viewmodel.deleteProduct(currentBarcode!!)
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            // Save - pass hasPalmOil
            viewmodel.saveProduct(
                currentProduct!!,
                currentBarcode!!,
                hasPalmOil,  //Pass palm oil detection result
                isSustainable,
                sustainabilityLevel
            )
            Toast.makeText(requireContext(), "Saved to favorites ✓", Toast.LENGTH_SHORT).show()
        }
    }


    // Score Color - returns actual color Int
    private fun getScoreColor(score: Int): Int {
        return when (score) {
            in 0..30 -> ContextCompat.getColor(requireContext(), R.color.score_poor)   // Red - Poor
            in 31..50 -> ContextCompat.getColor(
                requireContext(),
                R.color.score_below_average
            )   // Orange - Below Average
            in 51..70 -> ContextCompat.getColor(
                requireContext(),
                R.color.score_average
            )   // Yellow - Average
            in 71..85 -> ContextCompat.getColor(
                requireContext(),
                R.color.score_good
            )   // Light Green - Good
            in 86..100 -> ContextCompat.getColor(
                requireContext(),
                R.color.score_excellent
            ) // Dark Green - Excellent
            else -> "#9E9E9E".toColorInt()        // Gray - Not Found/Invalid
        }
    }

    private fun getScoreTextColor(score: Int): Int {
        // Use black text for yellow background (51-70), white for others
        return if (score in 51..70) Color.BLACK else Color.WHITE
    }


    private fun updateSaveButton(isSaved: Boolean) {
        binding.saveProductButton.apply {
            if (isSaved) {
                text = "Saved ✓"
                setBackgroundColor(Color.parseColor("#4CAF50"))
            } else {
                text = "Save Product"
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.pastel_blue)
                )
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun displayPackagingInfo(product: Product?) {
        val packagings = product?.packagings

        if (packagings.isNullOrEmpty()) {
            binding.packagingCard.visibility = View.GONE
            return
        }

        binding.packagingCard.visibility = View.VISIBLE

        // Determine if recyclable
        val recyclableCount = packagings.count { pkg ->
            isRecyclableMaterial(pkg.material?.id)
        }
        val totalCount = packagings.size
        val allRecyclable = recyclableCount == totalCount

        // Set card colors
        val cardColor = if (allRecyclable) {
            Color.parseColor("#E8F5E9")
        } else {
            Color.parseColor("#FFF3E0")
        }
        binding.packagingCard.setCardBackgroundColor(cardColor)

        val strokeColor = if (allRecyclable) {
            Color.parseColor("#4CAF50")
        } else {
            Color.parseColor("#FF9800")
        }
        binding.packagingCard.strokeColor = strokeColor

        // Set icon
        binding.packagingIconText.text = if (allRecyclable) "♻️" else "⚠️"
        binding.packagingIconText.textSize = 28f

        // Set status text
        binding.packagingStatusText.text = when {
            allRecyclable && recyclableCount > 0 ->
                "✓ All Components Recyclable ($recyclableCount/$totalCount)"
            recyclableCount == 0 ->
                "✗ Not Recyclable"
            else ->
                "⚠ Partially Recyclable ($recyclableCount/$totalCount)"
        }

        // Display component list
        binding.packagingComponentsContainer.removeAllViews()

        packagings.forEachIndexed { index, pkg ->
            val isRecyclable = isRecyclableMaterial(pkg.material?.id)
            val materialName = formatMaterialName(pkg.material?.id)
            val shapeName = formatShapeName(pkg.shape?.id)

            val itemView = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }

                val icon = if (isRecyclable) "♻️" else "⚠️"
                val status = if (isRecyclable) "Recyclable" else "Not Recyclable"

                text = "$icon $materialName - $shapeName\n   $status"
                textSize = 14f
                setTextColor(Color.parseColor("#424242"))
                setPadding(16, 12, 16, 12)

                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(if (isRecyclable) {
                        Color.parseColor("#E8F5E9")
                    } else {
                        Color.parseColor("#FFF3E0")
                    })
                    cornerRadius = 8f * resources.displayMetrics.density
                }
            }

            binding.packagingComponentsContainer.addView(itemView)
        }
    }

    private fun isRecyclableMaterial(materialId: String?): Boolean {
        if (materialId == null) return false
        val id = materialId.lowercase()
        return when {
            id.contains("pet-1") || id.contains("polyethylene-terephthalate") -> true
            id.contains("pp-5") || id.contains("polypropylene") -> true
            id.contains("hdpe") || id.contains("hdpe-2") -> true
            id.contains("ldpe") || id.contains("ldpe-4") -> true
            id.contains("paper") || id.contains("cardboard") || id.contains("carton") -> true
            id.contains("glass") -> true
            id.contains("aluminium") || id.contains("aluminum") || id.contains("steel") -> true
            else -> false
        }
    }

    private fun formatMaterialName(materialId: String?): String {
        if (materialId == null) return "Unknown"

        val id = materialId.lowercase()

        // Return user-friendly names
        return when {
            // Metals
            id.contains("steel") -> "Steel (Tinplate)"
            id.contains("aluminium") || id.contains("aluminum") -> "Aluminum"
            id.contains("metal") -> "Metal"

            // Plastics with codes
            id.contains("pet-1") -> "PET-1 (Polyethylene Terephthalate)"
            id.contains("hdpe-2") -> "HDPE-2 (High-Density Polyethylene)"
            id.contains("pvc-3") -> "PVC-3 (Polyvinyl Chloride)"
            id.contains("ldpe-4") -> "LDPE-4 (Low-Density Polyethylene)"
            id.contains("pp-5") -> "PP-5 (Polypropylene)"
            id.contains("ps-6") -> "PS-6 (Polystyrene)"

            // Generic plastic
            id.contains("plastic") -> "Plastic (Type Unknown)"

            // Paper products
            id.contains("paper") -> "Paper"
            id.contains("cardboard") -> "Cardboard"
            id.contains("carton") -> "Carton"

            // Glass
            id.contains("glass") -> "Glass"

            // Default: format nicely
            else -> materialId
                .removePrefix("en:")
                .replace("-", " ")
                .split(" ")
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
        }
    }

    private fun formatShapeName(shapeId: String?): String {
        if (shapeId == null) return "Unknown"
        return shapeId
            .removePrefix("en:")
            .replace("-", " ")
            .replaceFirstChar { it.uppercase() }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}