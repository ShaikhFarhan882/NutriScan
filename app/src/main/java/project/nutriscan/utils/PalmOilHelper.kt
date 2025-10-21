package project.nutriscan.utils

import android.content.Context
import androidx.core.content.ContextCompat
import project.nutriscan.model.Product

/**
 * Extension functions for Product to detect and analyze palm oil content
 */

/**
 * Check if product contains palm oil
 */
fun Product.containsPalmOil(): Boolean {
    val hasPalmOilIngredients = (ingredients_from_palm_oil_n ?: 0) > 0

    val hasPalmOilTag = ingredients_analysis_tags?.any { tag ->
        tag.contains("palm-oil", ignoreCase = true) &&
                !tag.contains("palm-oil-free", ignoreCase = true)
    } ?: false

    val hasPalmOilInTags = ingredients_tags?.any { tag ->
        tag.contains("palm", ignoreCase = true)
    } ?: false

    val hasPalmOilInText = ingredients_text?.contains("palm oil", ignoreCase = true) ?: false ||
            ingredients_text_en?.contains("palm oil", ignoreCase = true) ?: false

    return hasPalmOilIngredients || hasPalmOilTag || hasPalmOilInTags || hasPalmOilInText
}

/**
 * Check if product may contain palm oil
 */
fun Product.mayContainPalmOil(): Boolean {
    return (ingredients_that_may_be_from_palm_oil_n ?: 0) > 0
}

/**
 * Check if product is palm oil free
 */
fun Product.isPalmOilFree(): Boolean {
    val hasFreeTag = ingredients_analysis_tags?.any { tag ->
        tag.contains("palm-oil-free", ignoreCase = true)
    } ?: false

    val hasNoPalmOil = !containsPalmOil() && !mayContainPalmOil()

    return hasFreeTag || hasNoPalmOil
}

/**
 * Get the palm oil status of the product
 */
fun Product.getPalmOilStatus(): PalmOilStatus {
    return when {
        containsPalmOil() -> PalmOilStatus.CONTAINS
        mayContainPalmOil() -> PalmOilStatus.MAY_CONTAIN
        isPalmOilFree() -> PalmOilStatus.FREE
        else -> PalmOilStatus.UNKNOWN
    }
}

/**
 * Get total count of palm oil related ingredients
 */
fun Product.getTotalPalmOilIngredientsCount(): Int {
    val definite = ingredients_from_palm_oil_n ?: 0
    val possible = ingredients_that_may_be_from_palm_oil_n ?: 0
    return definite + possible
}

/**
 * Get list of all palm oil ingredients (definite)
 */
fun Product.getPalmOilIngredients(): List<String> {
    return ingredients_from_palm_oil_tags ?: ingredients_from_palm_oil ?: emptyList()
}

/**
 * Get list of possible palm oil ingredients
 */
fun Product.getPossiblePalmOilIngredients(): List<String> {
    return ingredients_that_may_be_from_palm_oil_tags ?:
    ingredients_that_may_be_from_palm_oil ?:
    emptyList()
}

/**
 * Get all palm oil ingredients combined (definite + possible)
 */
fun Product.getAllPalmOilRelatedIngredients(): List<PalmOilIngredient> {
    val definiteIngredients = getPalmOilIngredients().map {
        PalmOilIngredient(it, true)
    }
    val possibleIngredients = getPossiblePalmOilIngredients().map {
        PalmOilIngredient(it, false)
    }
    return definiteIngredients + possibleIngredients
}

/**
 * Get formatted palm oil summary text
 */
fun Product.getPalmOilSummary(): String {
    val status = getPalmOilStatus()
    return when (status) {
        PalmOilStatus.CONTAINS -> {
            val count = ingredients_from_palm_oil_n ?: 0
            if (count > 0) {
                "$count ingredient${if (count > 1) "s" else ""} from palm oil"
            } else {
                "Contains palm oil"
            }
        }
        PalmOilStatus.MAY_CONTAIN -> {
            val count = ingredients_that_may_be_from_palm_oil_n ?: 0
            if (count > 0) {
                "$count ingredient${if (count > 1) "s may" else " may"} contain palm oil"
            } else {
                "May contain palm oil"
            }
        }
        PalmOilStatus.FREE -> "No palm oil detected"
        PalmOilStatus.UNKNOWN -> "Palm oil status unknown"
    }
}

/**
 * Get color for palm oil status
 */
fun Product.getPalmOilStatusColor(context: Context): Int {
    return ContextCompat.getColor(context, getPalmOilStatus().colorRes)
}

/**
 * Check if product has sustainable palm oil certification
 */
fun Product.hasSustainablePalmOil(): Boolean {
    return labels?.contains("sustainable palm oil", ignoreCase = true) ?: false ||
            labels?.contains("rspo", ignoreCase = true) ?: false
}

/**
 * Get detailed palm oil analysis
 */
fun Product.getPalmOilAnalysis(): PalmOilAnalysis {
    return PalmOilAnalysis(
        status = getPalmOilStatus(),
        containsPalmOil = containsPalmOil(),
        mayContainPalmOil = mayContainPalmOil(),
        definiteIngredientsCount = ingredients_from_palm_oil_n ?: 0,
        possibleIngredientsCount = ingredients_that_may_be_from_palm_oil_n ?: 0,
        definiteIngredients = getPalmOilIngredients(),
        possibleIngredients = getPossiblePalmOilIngredients(),
        isSustainable = hasSustainablePalmOil(),
        summary = getPalmOilSummary()
    )
}

/**
 * Data class for palm oil ingredient with certainty
 */
data class PalmOilIngredient(
    val name: String,
    val isDefinite: Boolean
) {
    fun getDisplayName(): String {
        return if (isDefinite) {
            name
        } else {
            "$name (may contain)"
        }
    }
}

/**
 * Data class for comprehensive palm oil analysis
 */
data class PalmOilAnalysis(
    val status: PalmOilStatus,
    val containsPalmOil: Boolean,
    val mayContainPalmOil: Boolean,
    val definiteIngredientsCount: Int,
    val possibleIngredientsCount: Int,
    val definiteIngredients: List<String>,
    val possibleIngredients: List<String>,
    val isSustainable: Boolean,
    val summary: String
) {
    fun getTotalIngredientsCount(): Int = definiteIngredientsCount + possibleIngredientsCount

    fun hasAnyPalmOilIngredients(): Boolean =
        definiteIngredientsCount > 0 || possibleIngredientsCount > 0
}
