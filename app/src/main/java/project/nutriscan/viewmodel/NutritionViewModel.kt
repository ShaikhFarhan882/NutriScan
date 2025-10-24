package project.nutriscan.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import project.nutriscan.database.SavedProduct
import project.nutriscan.model.NutritionResponse
import project.nutriscan.model.Product
import project.nutriscan.repository.ProductRepository
import project.nutriscan.repository.Repository

class NutritionViewModel(
    private val repository: Repository,
    private val productRepository: ProductRepository,
    private val application: Application
) : AndroidViewModel(application) {

    val productDetails: MutableLiveData<NutritionResponse> = MutableLiveData()


    fun searchProduct(barcode: String, fields: String) = viewModelScope.launch {
        val response = repository.searchProduct(barcode, fields)
        if (response.isSuccessful) {
            response.body()?.let {
                productDetails.postValue(it)
            }
        }
    }

    // Saved products LiveData
    val savedProducts: LiveData<List<SavedProduct>> = productRepository.getAllSavedProducts()

    // Track if current product is saved
    private val _isProductSaved = MutableLiveData<Boolean>()
    val isProductSaved: LiveData<Boolean> = _isProductSaved

    /**
     * Save product to database
     */
    fun saveProduct(
        product: Product,
        barcode: String,
        hasPalmOil: Boolean,
        isSustainable: Boolean,
        sustainabilityLevel: String?
    ) {
        viewModelScope.launch {
            val savedProduct = SavedProduct(
                barcode = barcode,
                productName = product.product_name,
                brands = product.brands,
                imageUrl = product.image_url,
                manufacturingPlaces = product.manufacturing_places,
                nutriScore = product.nutriments?.nutrition_score_fr?.toString(),
                ecoScoreGrade = product.ecoscore_grade,
                ecoScoreScore = product.ecoscore_score,
                palmOilCount = product.ingredients_from_palm_oil_n,
                mayContainPalmOilCount = product.ingredients_that_may_be_from_palm_oil_n,
                hasPalmOil = hasPalmOil,
                isSustainable = isSustainable,
                sustainabilityLevel = sustainabilityLevel,
                allergens = product.allergens,
                additivesCount = product.additives_tags?.size,
                ingredientsText = product.ingredients_text_en,
            )

            productRepository.saveProduct(savedProduct)
            _isProductSaved.value = true
        }
    }

    /**
     * Delete product from database
     */
    fun deleteProduct(barcode: String) {
        viewModelScope.launch {
            productRepository.deleteProduct(barcode)
            _isProductSaved.value = false
        }
    }

    /**
     * Check if product is saved
     */
    fun checkIfProductSaved(barcode: String) {
        viewModelScope.launch {
            _isProductSaved.value = productRepository.isProductSaved(barcode)
        }
    }


    // Packaging degradability LiveData
    private val _isDegradable = MutableLiveData<Boolean>()
    val isDegradable: LiveData<Boolean> = _isDegradable

    private val _degradableText = MutableLiveData<String>()
    val degradableText: LiveData<String> = _degradableText

    private fun analyzePackaging(product: Product?) {
        val packagings = product?.packagings

        if (packagings.isNullOrEmpty()) {
            _isDegradable.value = false
            _degradableText.value = "No packaging information available"
        } else {
            val allDegradable = packagings.all {
                it.nonRecyclableAndNonBiodegradable == "no"
            }

            _isDegradable.value = allDegradable

            _degradableText.value = if (allDegradable) {
                "✓ Packaging is Recyclable/Biodegradable"
            } else {
                val degradableCount = packagings.count {
                    it.nonRecyclableAndNonBiodegradable == "no"
                }
                "⚠ $degradableCount of ${packagings.size} components are degradable"
            }
        }
    }
}