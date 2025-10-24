package project.nutriscan.model

import com.google.gson.annotations.SerializedName

data class Packaging(
    val material: MaterialInfo?,
    val shape: ShapeInfo?,
    @SerializedName("food_contact")
    val foodContact: Int?,
    @SerializedName("number_of_units")
    val numberOfUnits: String?,  // Changed to String (API returns "1")
    @SerializedName("nonrecyclableandnonbiodegradable")
    val nonRecyclableAndNonBiodegradable: String? = null  // ✅ Optional field
)

data class MaterialInfo(
    val id: String?
)

data class ShapeInfo(
    val id: String?
)
