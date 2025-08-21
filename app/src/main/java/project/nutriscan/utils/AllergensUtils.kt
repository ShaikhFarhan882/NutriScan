package project.nutriscan.utils


import project.nutriscan.R
import project.nutriscan.model.AllergenInfo

object AllergenUtils {

    fun getFullAllergenInfo(allergenCode: String): AllergenInfo {
        val cleanCode = allergenCode.replace("en:", "").trim().lowercase()

        return when (cleanCode) {
            "gluten" -> AllergenInfo(
                code = "Gluten",
                fullName = "Gluten (Wheat Protein)",
                category = "Cereal Protein",
                riskLevel = "LOW RISK",
                description = "Found in wheat, barley, rye, and oats. Can cause digestive issues in sensitive individuals with celiac disease or gluten sensitivity.",
                symptoms = "Digestive discomfort, bloating, diarrhea, skin rash, fatigue in sensitive individuals",
                avoidanceAdvice = "Look for gluten-free alternatives. Check labels for wheat, barley, rye derivatives.",
            )

            "milk" -> AllergenInfo(
                code = "Milk",
                fullName = "Milk and Dairy Products",
                category = "Dairy Allergen",
                riskLevel = "MEDIUM RISK",
                description = "Contains lactose and milk proteins (casein, whey). May cause allergic reactions or digestive discomfort in lactose intolerant individuals.",
                symptoms = "Hives, digestive upset, breathing difficulties, anaphylaxis in severe cases",
                avoidanceAdvice = "Avoid all dairy products. Check for hidden dairy in processed foods, baked goods.",
            )

            "eggs" -> AllergenInfo(
                code = "Eggs",
                fullName = "Eggs and Egg Products",
                category = "Animal Protein",
                riskLevel = "MEDIUM RISK",
                description = "Egg proteins can trigger allergic reactions. Common in baked goods, processed foods, and mayonnaise.",
                symptoms = "Skin reactions, digestive issues, respiratory problems, anaphylaxis in rare cases",
                avoidanceAdvice = "Read labels carefully. Eggs may be hidden in baked goods, pasta, mayonnaise, vaccines.",
            )

            "peanuts" -> AllergenInfo(
                code = "Peanuts",
                fullName = "Peanuts (Groundnuts)",
                category = "Legume",
                riskLevel = "HIGH RISK",
                description = "Legume that can cause severe allergic reactions including anaphylaxis. Often found in processed foods due to cross-contamination.",
                symptoms = "Severe allergic reactions, anaphylaxis, breathing difficulties, swelling, cardiac arrest",
                avoidanceAdvice = "Avoid all peanut products. Be aware of cross-contamination in manufacturing facilities.",
            )

            "tree nuts", "nuts" -> AllergenInfo(
                code = "Tree Nuts",
                fullName = "Tree Nuts (Various)",
                category = "Tree Nut",
                riskLevel = "HIGH RISK",
                description = "Includes almonds, walnuts, cashews, hazelnuts, pecans. Can cause severe allergic reactions and anaphylaxis.",
                symptoms = "Severe allergic reactions, anaphylaxis, swelling of throat/tongue, difficulty breathing",
                avoidanceAdvice = "Avoid all tree nuts. Check for cross-contamination warnings on packaging.",
            )

            "soy", "soya", "soybeans" -> AllergenInfo(
                code = "Soy",
                fullName = "Soy and Soy Products",
                category = "Legume",
                riskLevel = "MEDIUM RISK",
                description = "Soybean-derived ingredients. Common allergen in processed foods, Asian cuisine, and protein supplements.",
                symptoms = "Digestive issues, skin reactions, respiratory problems, rarely anaphylaxis",
                avoidanceAdvice = "Read labels for soy lecithin, soy protein, tofu, tempeh, miso. Common in processed foods.",
            )

            "fish" -> AllergenInfo(
                code = "Fish",
                fullName = "Fish and Fish Products",
                category = "Seafood",
                riskLevel = "MEDIUM RISK",
                description = "Fish proteins can cause allergic reactions. Check for fish-derived ingredients like anchovies in sauces.",
                symptoms = "Allergic reactions, digestive issues, respiratory problems, anaphylaxis in severe cases",
                avoidanceAdvice = "Avoid all fish and fish-derived products. Be cautious with sauces and seasonings.",
            )

            "shellfish" -> AllergenInfo(
                code = "Shellfish",
                fullName = "Crustaceans and Mollusks",
                category = "Seafood",
                riskLevel = "HIGH RISK",
                description = "Includes shrimp, crab, lobster, mussels, oysters. Can cause severe allergic reactions and is the most common adult food allergy.",
                symptoms = "Severe allergic reactions, anaphylaxis, swelling, difficulty breathing, cardiac issues",
                avoidanceAdvice = "Avoid all shellfish. Be cautious in seafood restaurants due to cross-contamination.",
            )

            "sesame" -> AllergenInfo(
                code = "Sesame",
                fullName = "Sesame Seeds and Oil",
                category = "Seed",
                riskLevel = "LOW RISK",
                description = "Sesame seeds and oil. Increasingly recognized as a major allergen, common in Middle Eastern and Asian foods.",
                symptoms = "Allergic reactions, digestive issues, skin reactions, rarely anaphylaxis",
                avoidanceAdvice = "Check labels for sesame oil, tahini, hummus. Common in baked goods and ethnic foods.",
            )

            "wheat" -> AllergenInfo(
                code = "Wheat",
                fullName = "Wheat and Wheat Products",
                category = "Cereal Grain",
                riskLevel = "MEDIUM RISK",
                description = "Contains gluten proteins. Can cause allergic reactions separate from celiac disease.",
                symptoms = "Digestive issues, skin reactions, respiratory problems, exercise-induced anaphylaxis",
                avoidanceAdvice = "Avoid wheat-containing products. Look for wheat-free alternatives.",
            )

            "celery" -> AllergenInfo(
                code = "Celery",
                fullName = "Celery and Celeriac",
                category = "Vegetable",
                riskLevel = "LOW RISK",
                description = "Can cause allergic reactions, particularly common in Europe. Found in soups, seasonings, and processed foods.",
                symptoms = "Oral allergy syndrome, digestive issues, skin reactions",
                avoidanceAdvice = "Check ingredient lists in soups, seasonings, and processed foods.",
            )

            "mustard" -> AllergenInfo(
                code = "Mustard",
                fullName = "Mustard Seeds and Powder",
                category = "Spice/Condiment",
                riskLevel = "LOW RISK",
                description = "Mustard seeds and powder can cause allergic reactions. Common in condiments and processed foods.",
                symptoms = "Allergic reactions, digestive issues, skin reactions",
                avoidanceAdvice = "Check condiments, salad dressings, and processed foods for mustard ingredients.",
            )

            "lupin" -> AllergenInfo(
                code = "Lupin",
                fullName = "Lupin Beans and Flour",
                category = "Legume",
                riskLevel = "LOW RISK",
                description = "Legume related to peanuts. Can cause cross-reactions in peanut-allergic individuals.",
                symptoms = "Allergic reactions, cross-reactions with peanut allergies",
                avoidanceAdvice = "Be cautious if you have peanut allergies. Check bread and baked goods.",
            )

            "sulfites", "sulphites" -> AllergenInfo(
                code = "Sulfites",
                fullName = "Sulfur Dioxide and Sulfites",
                category = "Preservative",
                riskLevel = "VARIABLE RISK",
                description = "Preservatives that can cause reactions in sensitive individuals, especially those with asthma.",
                symptoms = "Asthma attacks, breathing difficulties, skin reactions",
                avoidanceAdvice = "Check wine, dried fruits, processed foods. Particularly important for asthmatics.",
            )

            else -> AllergenInfo(
                code = cleanCode.replaceFirstChar { it.uppercase() },
                fullName = "Unknown Allergen",
                category = "Unspecified",
                riskLevel = "UNKNOWN RISK",
                description = "This ingredient may cause allergic reactions in sensitive individuals. Consult with a healthcare provider if you have concerns.",
                symptoms = "Varies depending on individual sensitivity",
                avoidanceAdvice = "Consult with healthcare provider or allergist for specific guidance.",

            )
        }
    }

    fun getAllergenCountRobust(allergens: String?): Int {
        if (allergens.isNullOrBlank()) return 0

        return allergens.split(",")
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { it != "en:" } // Remove empty en: prefixes
            .count()
    }


    fun parseAllergensToDetailedList(allergens: String?): List<AllergenInfo> {
        if (allergens.isNullOrEmpty()) return emptyList()

        return allergens.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { getFullAllergenInfo(it) }
    }

}
