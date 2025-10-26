package project.nutriscan.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import project.nutriscan.model.Product

object HealthChecker {

    fun checkAndDisplay(
        product: Product?,
        healthWarningsCard: MaterialCardView,
        healthWarningsContainer: LinearLayout,
        context: Context
    ) {
        if (product?.nutriments == null) {
            healthWarningsCard.visibility = View.GONE
            return
        }

        // Load user health goals
        val sp = context.getSharedPreferences("NutriScanPrefs", Context.MODE_PRIVATE)
        val hasDiabetes = sp.getBoolean("health_diabetes", false)
        val hasHeart = sp.getBoolean("health_heart", false)
        val hasWeightLoss = sp.getBoolean("health_weight_loss", false)
        val hasLowSodium = sp.getBoolean("health_low_sodium", false)

        // If no health goals selected, hide card
        if (!hasDiabetes && !hasHeart && !hasWeightLoss && !hasLowSodium) {
            healthWarningsCard.visibility = View.GONE
            return
        }

        // Clear previous warnings
        healthWarningsContainer.removeAllViews()

        val nutrients = product.nutriments
        var warningCount = 0

        // DIABETES CHECKS
        if (hasDiabetes) {
            nutrients.sugars_100g?.let { sugar ->
                if (sugar > 10.0) {
                    addWarning(
                        healthWarningsContainer, context,
                        "⚠️ High Sugar",
                        "${String.format("%.1f", sugar)}g sugar per 100g",
                        "Not recommended for diabetes management",
                        Color.parseColor("#FFEBEE")
                    )
                    warningCount++
                }
            }
        }

        // HEART HEALTH CHECKS
        if (hasHeart) {
            nutrients.saturated_fat_100g?.let { satFat ->
                if (satFat > 5.0) {
                    addWarning(
                        healthWarningsContainer, context,
                        "⚠️ High Saturated Fat",
                        "${String.format("%.1f", satFat)}g per 100g",
                        "May increase cholesterol levels",
                        Color.parseColor("#FFE0B2")
                    )
                    warningCount++
                }
            }

            nutrients.sodium_100g?.let { sodium ->
                val sodiumMg = sodium * 1000
                if (sodiumMg > 600.0) {
                    addWarning(
                        healthWarningsContainer, context,
                        "⚠️ High Sodium",
                        "${String.format("%.0f", sodiumMg)}mg per 100g",
                        "May raise blood pressure",
                        Color.parseColor("#FFE0B2")
                    )
                    warningCount++
                }
            }
        }

        // WEIGHT MANAGEMENT CHECKS
        if (hasWeightLoss) {
            nutrients.energy_kcal_100g?.let { calories ->
                if (calories > 400.0) {
                    addWarning(
                        healthWarningsContainer, context,
                        "⚠️ High Calorie",
                        "${String.format("%.0f", calories)} calories per 100g",
                        "High energy density - watch portions",
                        Color.parseColor("#FFF9C4")
                    )
                    warningCount++
                }
            }
        }

        // LOW SODIUM CHECKS
        if (hasLowSodium) {
            nutrients.salt_100g?.let { salt ->
                if (salt > 1.5) {
                    addWarning(
                        healthWarningsContainer, context,
                        "⚠️ Very High Salt",
                        "${String.format("%.1f", salt)}g per 100g",
                        "Not suitable for low-sodium diet",
                        Color.parseColor("#FFEBEE")
                    )
                    warningCount++
                }
            }
        }

        // NUTRI-SCORE CHECK (for all users with health goals)
        product.nutriscore_grade?.let { grade ->
            if (grade.uppercase() in listOf("D", "E")) {
                addWarning(
                    healthWarningsContainer, context,
                    "⚠️ Poor Nutrition Score",
                    "Nutri-Score: ${grade.uppercase()}",
                    "Low overall nutritional quality",
                    Color.parseColor("#FFE0B2")
                )
                warningCount++
            }
        }

        // Show/hide card based on warnings
        healthWarningsCard.visibility = if (warningCount > 0) View.VISIBLE else View.GONE
    }

    private fun addWarning(
        container: LinearLayout,
        context: Context,
        title: String,
        message: String,
        recommendation: String,
        bgColor: Int
    ) {
        val warningView = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }

            orientation = LinearLayout.VERTICAL
            setPadding(20, 18, 20, 18)

            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(bgColor)
                cornerRadius = 12f * resources.displayMetrics.density
            }
        }

        // Warning Title
        val titleView = TextView(context).apply {
            text = title
            textSize = 15f
            setTextColor(Color.parseColor("#212121"))
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 8)
        }
        warningView.addView(titleView)

        // Warning Message
        val messageView = TextView(context).apply {
            text = message
            textSize = 14f
            setTextColor(Color.parseColor("#424242"))
            setPadding(0, 0, 0, 10)
            setLineSpacing(4f, 1.2f)  // ✅ FIXED: correct syntax (add, mult)
        }
        warningView.addView(messageView)

        // Recommendation
        val recommendationView = TextView(context).apply {
            text = "💡 $recommendation"
            textSize = 13f
            setTextColor(Color.parseColor("#616161"))
            setTypeface(null, android.graphics.Typeface.ITALIC)
            setLineSpacing(4f, 1.2f)  // ✅ FIXED: correct syntax (add, mult)
        }
        warningView.addView(recommendationView)

        container.addView(warningView)
    }
}
