package project.nutriscan.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_products")
data class SavedProduct(
    @PrimaryKey
    val barcode: String,

    // Basic Info
    val productName: String?,
    val brands: String?,
    val imageUrl: String?,
    val manufacturingPlaces: String?,

    // Nutritional Info
    val nutriScore: String?,
    val ecoScoreGrade: String?,
    val ecoScoreScore: Int?,

    // Palm Oil Info
    val palmOilCount: Int?,
    val mayContainPalmOilCount: Int?,
    val isSustainable: Boolean?,
    val sustainabilityLevel: String?,
    val hasPalmOil: Boolean,  // Actual detection result

    // Allergens & Additives
    val allergens: String?,
    val additivesCount: Int?,
    val ingredientsText: String?,

    // Timestamp
    val savedAt: Long = System.currentTimeMillis()
)
