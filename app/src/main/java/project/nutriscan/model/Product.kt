package project.nutriscan.model

import com.google.gson.annotations.SerializedName

data class Product(
    val _id: String?,
    val _keywords: List<String>?,
    val id: String?,
    val allergens: String?,
    val additives_tags : List<String>?,
    val nutrient_levels_tags: List<String>?,
    val ingredients_text_en : String?,
    val countries : String?,
    val ecoscore_grade : String?,
    val ecoscore_score: Int?,
    val image_front_small_url: String?,
    val image_front_thumb_url: String?,
    val image_front_url: String?,
    val image_ingredients_small_url: String?,
    val image_ingredients_thumb_url: String?,
    val image_ingredients_url: String?,
    val image_nutrition_small_url: String?,
    val image_nutrition_thumb_url: String?,
    val image_nutrition_url: String?,
    val image_small_url: String?,
    val image_thumb_url: String?,
    val image_url: String?,
    val ingredients_non_nutritive_sweeteners_n: Int?,
    val ingredients_original_tags: List<String>?,
    val ingredients_percent_analysis: Int?,
    val ingredients_sweeteners_n: Int?,
    val ingredients_tags: List<String>?,
    val ingredients_text: String?,
    val ingredients_text_fr: String?,
    val ingredients_text_with_allergens: String?,
    val ingredients_text_with_allergens_fr: String?,
    val ingredients_without_ciqual_codes: List<String>?,
    val ingredients_without_ciqual_codes_n: Int?,
    val ingredients_without_ecobalyse_ids: List<String>?,
    val ingredients_without_ecobalyse_ids_n: Int?,
    val interface_version_created: String?,
    val interface_version_modified: String?,
    val known_ingredients_n: Int?,
    //val labels: String?,
    val lc: String?,
    val link: String?,
    val main_countries_tags: List<Any>?,
    val manufacturing_places_tags: List<Any>?,
    val max_imgid: String?,
    val minerals_tags: List<Any>?,
    val misc_tags: List<String>?,
    val no_nutrition_data: String?,
    val nova_group: Int?,
    val nova_group_debug: String?,
    val nova_groups: String?,
    val nova_groups_markers: NovaGroupsMarkers?,
    val nova_groups_tags: List<String>?,
    val nucleotides_tags: List<Any>?,
    val nutrient_levels: NutrientLevels?,
   /* val nutrient_levels_tags: List<Any>?,*/
    val nutriments: Nutriments?,
    val nutriscore: Nutriscore?,
    val nutriscore_2021_tags: List<String>?,
    val nutriscore_2023_tags: List<String>?,
    val nutriscore_data: NutriscoreData?,
    val nutriscore_grade: String?,
    val nutriscore_score: Int?,
    val nutriscore_score_opposite: Int?,
    val nutriscore_tags: List<String>?,

    //Product info
    val manufacturing_places: String?,
    val product_name : String?,
    val brands : String?,


    // Palm Oil Detection Fields
    val ingredients_from_palm_oil_n: Int?,
    val ingredients_from_palm_oil: List<String>?,
    val ingredients_from_palm_oil_tags: List<String>?,
    val ingredients_that_may_be_from_palm_oil_n: Int?,
    val ingredients_that_may_be_from_palm_oil: List<String>?,
    val ingredients_that_may_be_from_palm_oil_tags: List<String>?,
    val ingredients_analysis_tags: List<String>?,

    val labels: String?,  // This field contains sustainability info
    val labels_tags: List<String>?,

    // Packaging degradability
    val packagings: List<Packaging>?
)

