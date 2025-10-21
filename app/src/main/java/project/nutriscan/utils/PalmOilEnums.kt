package project.nutriscan.utils

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import project.nutriscan.R

/**
 * Enum representing the palm oil status of a product
 */
enum class PalmOilStatus(
    val displayName: String,
    val description: String,
    @ColorRes val colorRes: Int,
    @DrawableRes val iconRes: Int,
    val severity: Int
) {
    CONTAINS(
        displayName = "Contains Palm Oil",
        description = "This product contains ingredients derived from palm oil",
        colorRes = R.color.palm_oil_red,
        iconRes = R.drawable.ic_palm_oil_warning,
        severity = 3
    ),

    MAY_CONTAIN(
        displayName = "May Contain Palm Oil",
        description = "This product may contain ingredients that could be derived from palm oil",
        colorRes = R.color.palm_oil_orange,
        iconRes = R.drawable.ic_palm_oil_caution,
        severity = 2
    ),

    FREE(
        displayName = "Palm Oil Free",
        description = "This product does not contain palm oil or palm oil derivatives",
        colorRes = R.color.palm_oil_green,
        iconRes = R.drawable.ic_palm_oil_free,
        severity = 0
    ),

    UNKNOWN(
        displayName = "Unknown",
        description = "Palm oil status could not be determined",
        colorRes = R.color.palm_oil_gray,
        iconRes = R.drawable.ic_palm_oil_unknown,
        severity = 1
    );

    companion object {
        /**
         * Get PalmOilStatus from severity level
         */
        fun fromSeverity(severity: Int): PalmOilStatus {
            return entries.firstOrNull { it.severity == severity } ?: UNKNOWN
        }
    }
}

/**
 * Enum for palm oil ingredient categories
 */
enum class PalmOilIngredientType(
    val displayName: String,
    val tag: String
) {
    PALM_OIL("Palm Oil", "en:palm-oil"),
    PALM_KERNEL_OIL("Palm Kernel Oil", "en:palm-kernel-oil"),
    PALM_FAT("Palm Fat", "en:palm-fat"),
    VEGETABLE_OIL("Vegetable Oil (may contain palm)", "en:vegetable-oil"),
    GLYCEROL("Glycerol (may contain palm)", "en:e422"),
    FATTY_ACID_ESTERS("Fatty Acid Esters", "en:e471"),
    VITAMIN_A_PALMITATE("Vitamin A Palmitate", "en:retinyl-palmitate"),
    PALMITIC_ACID("Palmitic Acid", "en:palmitic-acid"),
    STEARIC_ACID("Stearic Acid (may contain palm)", "en:stearic-acid");

    companion object {
        /**
         * Find ingredient type by tag
         */
        fun fromTag(tag: String): PalmOilIngredientType? {
            return entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) }
        }
    }
}

/**
 * Enum for analysis tags related to palm oil
 */
enum class PalmOilAnalysisTag(val tag: String, val meaning: String) {
    PALM_OIL("en:palm-oil", "Contains palm oil"),
    PALM_OIL_FREE("en:palm-oil-free", "Free from palm oil"),
    MAY_CONTAIN_PALM_OIL("en:may-contain-palm-oil", "May contain palm oil");

    companion object {
        fun fromTag(tag: String): PalmOilAnalysisTag? {
            return entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) }
        }
    }
}
