package project.nutriscan.repository

import androidx.lifecycle.LiveData
import project.nutriscan.database.ProductDatabase
import project.nutriscan.database.SavedProduct

class ProductRepository(private val database: ProductDatabase) {

    private val savedProductDao = database.savedProductDao()

    // Get all saved products
    fun getAllSavedProducts(): LiveData<List<SavedProduct>> {
        return savedProductDao.getAllSavedProducts()
    }

    // Save product
    suspend fun saveProduct(product: SavedProduct) {
        savedProductDao.insertProduct(product)
    }

    // Delete product
    suspend fun deleteProduct(barcode: String) {
        savedProductDao.deleteByBarcode(barcode)
    }

    // Check if product is saved
    suspend fun isProductSaved(barcode: String): Boolean {
        return savedProductDao.isProductSaved(barcode)
    }
}
