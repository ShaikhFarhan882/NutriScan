package project.nutriscan.model

data class UserPreferences(
    val username: String = "",
    val bio: String = "",
    val allergenPreferences: Map<String, Boolean> = emptyMap()
) {
    companion object {
        object AllergenCodes {
            const val GLUTEN = "gluten"
            const val MILK = "milk"
            const val EGGS = "eggs"
            const val PEANUTS = "peanuts"
            const val TREE_NUTS = "nuts"
            const val SOY = "soybeans"
            const val FISH = "fish"
            const val SHELLFISH = "shellfish"
            const val SESAME = "sesame-seeds"
            const val WHEAT = "wheat"
        }
    }

    // Convenience properties for backward compatibility
    val glutenAllergy: Boolean get() = allergenPreferences["gluten"] == true
    val nutsAllergy: Boolean get() = allergenPreferences["nuts"] == true
    val dairyAllergy: Boolean get() = allergenPreferences["milk"] == true
    val soyAllergy: Boolean get() = allergenPreferences["soybeans"] == true

    fun isAllergenEnabled(allergenCode: String): Boolean {
        val cleanCode = allergenCode.replace("en:", "").trim().lowercase()
        return allergenPreferences[cleanCode] == true
    }

    fun matchesUserAllergen(productAllergenCode: String): Boolean {
        val cleanCode = productAllergenCode.replace("en:", "").trim().lowercase()

        // Direct match first
        if (isAllergenEnabled(cleanCode)) return true

        // Handle variations and alternative names
        return when {
            // Milk/Dairy variations
            cleanCode.contains("milk") || cleanCode.contains("dairy") ->
                allergenPreferences["milk"] == true

            // Nuts variations (exclude coconut)
            cleanCode.contains("nut") && !cleanCode.contains("coconut") ->
                allergenPreferences["nuts"] == true || allergenPreferences["peanuts"] == true

            // Gluten/Wheat variations
            cleanCode.contains("gluten") || cleanCode.contains("wheat") ->
                allergenPreferences["gluten"] == true || allergenPreferences["wheat"] == true

            // Soy variations
            cleanCode.contains("soy") || cleanCode.contains("soybean") ->
                allergenPreferences["soybeans"] == true

            // Sesame variations
            cleanCode.contains("sesame") ->
                allergenPreferences["sesame-seeds"] == true

            // Egg variations
            cleanCode.contains("egg") ->
                allergenPreferences["eggs"] == true

            // Fish variations
            cleanCode.contains("fish") ->
                allergenPreferences["fish"] == true

            // Shellfish variations
            cleanCode.contains("shellfish") || cleanCode.contains("crustacean") ||
                    cleanCode.contains("mollusc") ->
                allergenPreferences["shellfish"] == true

            else -> false
        }
    }

    fun hasAnyAllergens(): Boolean =
        allergenPreferences.values.any { it }
}
