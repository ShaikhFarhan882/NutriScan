package project.nutriscan.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavedProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: SavedProduct)

    @Delete
    suspend fun deleteProduct(product: SavedProduct)

    @Query("SELECT * FROM saved_products ORDER BY savedAt DESC")
    fun getAllSavedProducts(): LiveData<List<SavedProduct>>

    @Query("DELETE FROM saved_products WHERE barcode = :barcode")
    suspend fun deleteByBarcode(barcode: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_products WHERE barcode = :barcode LIMIT 1)")
    suspend fun isProductSaved(barcode: String): Boolean
}
