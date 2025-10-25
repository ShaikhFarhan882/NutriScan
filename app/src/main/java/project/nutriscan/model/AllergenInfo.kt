package project.nutriscan.model

data class AllergenInfo(
    val code: String,
    val fullName: String,
    val category: String,
    val riskLevel: String,
    val description: String,
    val symptoms: String,
    val avoidanceAdvice: String,

    val isUserAllergen: Boolean = false
)
