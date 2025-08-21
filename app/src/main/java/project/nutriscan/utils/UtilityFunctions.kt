package project.nutriscan.utils

import project.nutriscan.model.AdditiveInfo

class UtilityFunctions {
    companion object {
        fun getAdditiveFullName(code: String): String {
            // --- Additive Full Names Map ---
             val additivesMap = mapOf(
                // E100–E199: Colours
                "E100" to "Curcumin",
                "E101" to "Riboflavin (Vitamin B2)",
                "E101a" to "Riboflavin-5'-phosphate",
                "E102" to "Tartrazine",
                "E103" to "Alkanet",
                "E104" to "Quinoline Yellow",
                "E105" to "Fast Yellow AB",
                "E106" to "Riboflavin-5'-phosphate sodium",
                "E107" to "Yellow 2G",
                "E110" to "Sunset Yellow FCF",
                "E120" to "Cochineal, Carminic acid",
                "E122" to "Carmoisine",
                "E123" to "Amaranth",
                "E124" to "Ponceau 4R",
                "E125" to "Ponceau SX",
                "E126" to "Ponceau 6R",
                "E127" to "Erythrosine",
                "E128" to "Red 2G",
                "E129" to "Allura Red AC",
                "E131" to "Patent Blue V",
                "E132" to "Indigo Carmine",
                "E133" to "Brilliant Blue FCF",
                "E140" to "Chlorophyll",
                "E141" to "Copper complexes of chlorophylls",
                "E142" to "Green S",
                "E143" to "Fast Green FCF",
                "E150a" to "Caramel (Plain)",
                "E150b" to "Caustic sulphite caramel",
                "E150c" to "Ammonia caramel",
                "E150d" to "Sulphite ammonia caramel",
                "E151" to "Brilliant Black BN",
                "E153" to "Vegetable carbon",
                "E154" to "Brown FK",
                "E155" to "Brown HT",
                "E160a" to "Carotenoids (Beta-carotene)",
                "E160b" to "Annatto, bixin, norbixin",
                "E160c" to "Paprika extract",
                "E160d" to "Lycopene",
                "E160e" to "Beta-apo-8'-carotenal ethyl ester",
                "E160f" to "Beta-apo-8'-carotenal",
                "E161b" to "Lutein",
                "E161g" to "Canthaxanthin",
                "E162" to "Beetroot red, betanin",
                "E163" to "Anthocyanins",
                "E170" to "Calcium carbonate",
                "E171" to "Titanium dioxide",
                "E172" to "Iron oxides and hydroxides",
                "E173" to "Aluminium",
                "E174" to "Silver",
                "E175" to "Gold",
                "E180" to "Lithol rubine BK",

                // E200–E299: Preservatives
                "E200" to "Sorbic acid",
                "E201" to "Sodium sorbate",
                "E202" to "Potassium sorbate",
                "E203" to "Calcium sorbate",
                "E210" to "Benzoic acid",
                "E211" to "Sodium benzoate",
                "E212" to "Potassium benzoate",
                "E213" to "Calcium benzoate",
                "E214" to "Ethyl p-hydroxybenzoate",
                "E215" to "Sodium ethyl p-hydroxybenzoate",
                "E216" to "Propyl p-hydroxybenzoate",
                "E217" to "Sodium propyl p-hydroxybenzoate",
                "E218" to "Methyl p-hydroxybenzoate",
                "E219" to "Sodium methyl p-hydroxybenzoate",
                "E220" to "Sulphur dioxide",
                "E221" to "Sodium sulphite",
                "E222" to "Sodium hydrogen sulphite",
                "E223" to "Sodium metabisulphite",
                "E224" to "Potassium metabisulphite",
                "E226" to "Calcium sulphite",
                "E227" to "Calcium hydrogen sulphite",
                "E228" to "Potassium hydrogen sulphite",
                "E230" to "Biphenyl, diphenyl",
                "E231" to "Orthophenyl phenol",
                "E232" to "Sodium orthophenyl phenol",
                "E233" to "Thiabendazole",
                "E234" to "Nisin",
                "E235" to "Natamycin",
                "E236" to "Formic acid",
                "E237" to "Sodium formate",
                "E238" to "Calcium formate",
                "E239" to "Hexamethylene tetramine",
                "E242" to "Dimethyl dicarbonate",
                "E249" to "Potassium nitrite",
                "E250" to "Sodium nitrite",
                "E251" to "Sodium nitrate",
                "E252" to "Potassium nitrate",

                // E260–E299: Acidity regulators, anti-caking agents, etc.
                "E260" to "Acetic acid",
                "E261" to "Potassium acetate",
                "E262" to "Sodium acetate",
                "E263" to "Calcium acetate",
                "E270" to "Lactic acid",
                "E280" to "Propionic acid",
                "E281" to "Sodium propionate",
                "E282" to "Calcium propionate",
                "E283" to "Potassium propionate",
                "E284" to "Boric acid",
                "E285" to "Sodium tetraborate (borax)",

                // E300–E399: Antioxidants, acidity regulators
                "E300" to "Ascorbic acid (Vitamin C)",
                "E301" to "Sodium ascorbate",
                "E302" to "Calcium ascorbate",
                "E304" to "Fatty acid esters of ascorbic acid",
                "E306" to "Tocopherol (Vitamin E)",
                "E307" to "Alpha-tocopherol",
                "E308" to "Gamma-tocopherol",
                "E309" to "Delta-tocopherol",
                "E310" to "Propyl gallate",
                "E315" to "Erythorbic acid",
                "E316" to "Sodium erythorbate",
                "E319" to "Tertiary-butyl hydroquinone (TBHQ)",
                "E320" to "Butylated hydroxyanisole (BHA)",
                "E321" to "Butylated hydroxytoluene (BHT)",
                "E322" to "Lecithins",
                "E322i" to "Sodium salt of sulfurous acid",
                "E330" to "Citric acid",
                "E331" to "Sodium citrate",
                "E332" to "Potassium citrate",
                "E333" to "Calcium citrate",
                "E334" to "Tartaric acid",
                "E335" to "Sodium tartrate",
                "E336" to "Potassium tartrate",
                "E337" to "Sodium potassium tartrate",
                "E392" to "Extracts of rosemary",
                "E400" to "Alginic acid",
                "E401" to "Sodium alginate",
                "E402" to "Potassium alginate",
                "E403" to "Ammonium alginate",
                "E404" to "Calcium alginate",
                "E405" to "Propane-1,2-diol alginate (PGA)",
                "E406" to "Agar",
                "E407" to "Carrageenan (Irish moss)",
                "E407a" to "Processed eucheuma seaweed",
                "E410" to "Locust bean gum (carob gum)",
                "E412" to "Guar gum (cluster bean gum)",
                "E413" to "Tragacanth",
                "E414" to "Acacia gum (gum arabic)",
                "E415" to "Xanthan gum",
                "E416" to "Karaya gum",
                "E417" to "Tara gum",
                "E418" to "Gellan gum",
                "E420" to "Sorbitol",
                "E421" to "Mannitol",
                "E422" to "Glycerol",
                "E425" to "Konjac",
                "E426" to "Soybean hemicellulose",
                "E427" to "Cassia gum",

                // E430–E499: Emulsifiers, stabilizers, thickeners
                "E432" to "Polysorbate 20",
                "E433" to "Polysorbate 80",
                "E434" to "Polysorbate 40",
                "E435" to "Polysorbate 60",
                "E436" to "Polysorbate 65",
                "E440a" to "Pectin",
                "E440b" to "Amidated pectin",
                "E441" to "Gelatine",
                "E442" to "Ammonium phosphatide",
                "E444" to "Sucrose acetate isobutyrate",
                "E445" to "Glycerol esters of wood rosins",
                "E450" to "Diphosphates",
                "E451" to "Triphosphates",
                "E452" to "Polyphosphates",
                "E460" to "Cellulose",
                "E461" to "Methyl cellulose",
                "E462" to "Ethyl cellulose",
                "E463" to "Hydroxypropyl cellulose",
                "E464" to "Hydroxypropyl methyl cellulose",
                "E465" to "Ethyl methyl cellulose",
                "E466" to "Carboxymethyl cellulose",
                "E468" to "Crosslinked sodium carboxymethyl cellulose",
                "E469" to "Enzymatically hydrolysed carboxymethyl cellulose",
                "E470a" to "Sodium, potassium, and calcium salts of fatty acids",
                "E470b" to "Magnesium salts of fatty acids",
                "E471" to "Mono- and diglycerides of fatty acids",
                "E472a" to "Acetic acid esters of mono- and diglycerides of fatty acids",
                "E472b" to "Lactic acid esters of mono- and diglycerides of fatty acids",
                "E472c" to "Citric acid esters of mono- and diglycerides of fatty acids",
                "E472d" to "Tartaric acid esters of mono- and diglycerides of fatty acids",
                "E472e" to "Mono- and diacetyltartaric acid esters of mono- and diglycerides of fatty acids",
                "E472f" to "Mixed acetic and tartaric acid esters of mono- and diglycerides of fatty acids",
                "E473" to "Sucrose esters of fatty acids",
                "E474" to "Sucroglycerides",
                "E475" to "Polyglycerol esters of fatty acids",
                "E476" to "Polyglycerol polyricinoleate",
                "E477" to "Propane-1,2-diol esters of fatty acids",
                "E478" to "Lactylated fatty acid esters of glycerol and propylene glycol",
                "E479b" to "Thermally oxidised soya bean oil interacted with mono- and diglycerides of fatty acids",
                "E481" to "Sodium stearoyl-2-lactylate",
                "E482" to "Calcium stearoyl-2-lactylate",
                "E483" to "Stearyl tartrate",
                "E491" to "Sorbitan monostearate",
                "E492" to "Sorbitan tristearate",
                "E493" to "Sorbitan monolaurate",
                "E494" to "Sorbitan monooleate",
                "E495" to "Sorbitan monopalmitate",

                // E500–E599: Acidity regulators, anti-caking agents, raising agents
                "E500" to "Sodium carbonates",
                "E500II" to "Sodium bicarbonate",
                "E501" to "Potassium carbonates",
                "E502" to "Ammonium carbonates",
                "E503" to "Magnesium carbonates",
                "E504" to "Calcium carbonates",
                "E507" to "Hydrochloric acid",
                "E508" to "Potassium chloride",
                "E509" to "Calcium chloride",
                "E510" to "Ammonium chloride",
                "E511" to "Magnesium chloride",
                "E512" to "Stannous chloride",
                "E513" to "Sulphuric acid",
                "E514" to "Sodium sulphates",
                "E515" to "Potassium sulphates",
                "E516" to "Calcium sulphate",
                "E517" to "Ammonium sulphate",
                "E518" to "Magnesium sulphate",
                "E519" to "Copper sulphate",
                "E520" to "Aluminium sulphate",
                "E521" to "Aluminium sodium sulphate",
                "E522" to "Aluminium potassium sulphate",
                "E523" to "Aluminium ammonium sulphate",
                "E524" to "Sodium hydroxide",
                "E525" to "Potassium hydroxide",
                "E526" to "Calcium hydroxide",
                "E527" to "Ammonium hydroxide",
                "E528" to "Magnesium hydroxide",
                "E529" to "Calcium oxide",
                "E530" to "Magnesium oxide",
                "E535" to "Sodium ferrocyanide",
                "E536" to "Potassium ferrocyanide",
                "E538" to "Calcium ferrocyanide",
                "E541" to "Sodium aluminium phosphate",
                "E551" to "Silicon dioxide",
                "E552" to "Calcium silicate",
                "E553a" to "Magnesium silicate",
                "E553b" to "Talc",
                "E554" to "Sodium aluminosilicate",
                "E555" to "Potassium aluminosilicate",
                "E556" to "Calcium aluminosilicate",
                "E557" to "Magnesium aluminosilicate",
                "E558" to "Kaolin",
                "E559" to "Aluminium silicate",
                "E570" to "Stearic acid",
                "E572" to "Magnesium stearate",
                "E574" to "Gluconic acid",
                "E575" to "Glucono delta-lactone",
                "E576" to "Sodium gluconate",
                "E577" to "Potassium gluconate",
                "E578" to "Calcium gluconate",
                "E579" to "Ferrous gluconate",
                "E585" to "Ferrous lactate",

                // E620–E699: Flavour enhancers
                "E620" to "Glutamic acid",
                "E621" to "Monosodium glutamate (MSG)",
                "E622" to "Monopotassium glutamate",
                "E623" to "Calcium glutamate",
                "E624" to "Monoammonium glutamate",
                "E625" to "Magnesium glutamate",
                "E626" to "Guanylic acid",
                "E627" to "Disodium guanylate",
                "E628" to "Dipotassium guanylate",
                "E629" to "Calcium guanylate",
                "E630" to "Inosinic acid",
                "E631" to "Disodium inosinate",
                "E632" to "Dipotassium inosinate",
                "E633" to "Calcium inosinate",
                "E634" to "Calcium 5'-ribonucleotide",
                "E635" to "Disodium 5'-ribonucleotide",
                "E640" to "Glycine and its salts",
                "E650" to "Zinc acetate",

                // E900–E999: Miscellaneous
                "E900" to "Dimethylpolysiloxane",
                "E901" to "Beeswax",
                "E902" to "Candelilla wax",
                "E903" to "Carnauba wax",
                "E904" to "Shellac",
                "E905" to "Microcrystalline wax",
                "E907" to "Refined microcrystalline wax",
                "E912" to "Montan acid esters",
                "E914" to "Oxidised polyethylene wax",
                "E920" to "L-Cysteine",
                "E927b" to "Carbamide (urea)",
                "E950" to "Acesulfame K",
                "E951" to "Aspartame",
                "E952" to "Cyclamic acid and its salts",
                "E953" to "Isomalt",
                "E954" to "Saccharin and its salts",
                "E955" to "Sucralose",
                "E957" to "Thaumatin",
                "E959" to "Neohesperidine DC",
                "E960" to "Steviol glycosides",
                "E961" to "Neotame",
                "E962" to "Aspartame-acesulfame salt",
                "E965" to "Maltitol",
                "E966" to "Lactitol",
                "E967" to "Xylitol",
                "E968" to "Erythritol",
                "E999" to "Quillaia extract",

                // E1000–E1520: Additional additives
                "E1000" to "Cholic acid",
                "E1001" to "Choline salts",
                "E1002" to "Beta-apo-8'-carotenal (C30)",
                "E1010" to "Triethyl citrate",
                "E1103" to "Invertase",
                "E1105" to "Lysozyme",
                "E1200" to "Polydextrose",
                "E1201" to "Polyvinylpyrrolidone",
                "E1202" to "Polyvinylpolypyrrolidone",
                "E1203" to "Polyethylene glycol",
                "E1204" to "Pullulan",
                "E1400" to "Dextrins, roasted starch",
                "E1401" to "Acid treated starch",
                "E1402" to "Alkali treated starch",
                "E1403" to "Bleached starch",
                "E1404" to "Oxidised starch",
                "E1410" to "Monostarch phosphate",
                "E1412" to "Distarch phosphate",
                "E1413" to "Phosphated distarch phosphate",
                "E1414" to "Acetylated distarch phosphate",
                "E1420" to "Acetylated starch",
                "E1422" to "Acetylated distarch adipate",
                "E1440" to "Hydroxypropyl starch",
                "E1442" to "Hydroxypropyl distarch phosphate",
                "E1450" to "Starch sodium octenyl succinate",
                "E1451" to "Acetylated oxidised starch",
                "E1505" to "Triethyl citrate",
                "E1517" to "Glyceryl diacetate",
                "E1518" to "Glyceryl triacetate",
                "E1520" to "Propylene glycol"
            )

            // Return the full name or "Unknown Additive" if not found
            return additivesMap[code] ?: "Unknown Additive"

        }

        // --- Converts 'en:e150d' or 'e150d' to 'E150D' ---
        fun convertTagFormat(tag: String?): String {
            if (tag.isNullOrBlank()) return ""
            return tag.substringAfter(":").uppercase()
        }


        // --- Gets category from code ---
        fun getAdditiveCategory(code: String): String {
            val digits = Regex("""\d+""").find(code)?.value?.toIntOrNull() ?: return "Other"
            return when (digits) {
                in 100..199 -> "Colours"
                in 200..299 -> "Preservatives"
                in 300..399 -> "Antioxidants / Acidity Regulators"
                in 400..499 -> "Thickeners / Stabilizers / Emulsifiers"
                in 500..599 -> "Acidity Regulators / Anti-caking Agents"
                in 600..699 -> "Flavour Enhancers"
                in 700..799 -> "Antibiotics"
                in 900..999 -> "Glazing Agents / Gases / Sweeteners"
                in 1000..1599 -> "Other Additives"
                else -> "Other"
            }
        }

        // --- Gets safety information for code ---
        fun getAdditiveSafetyInfo(code: String): String {
            val riskyCodes = setOf("E102", "E110", "E124", "E129", "E621")
            val avoidForChildren = setOf("E102", "E104", "E110", "E122", "E124", "E129")

            return when {
                code in riskyCodes -> "Use with caution – may cause allergic reactions"
                code in avoidForChildren -> "Avoid in children – linked to hyperactivity"
                code == "E621" -> "MSG – may cause sensitivity in some individuals"
                code.startsWith("E3") -> "Generally safe – natural antioxidant"
                else -> "Generally recognized as safe in permitted amounts"
            }
        }

        //Health Concerns.
        fun getAdditiveHealthConcerns(code: String): String {
            // Additives linked to cancer concerns
            val cancerLinked = setOf("E102", "E110", "E124", "E127", "E131", "E142", "E320", "E321")

            // Additives linked to allergic reactions
            val allergyTriggers = setOf("E102", "E104", "E110", "E122", "E124", "E129", "E131", "E132", "E133", "E151", "E220", "E221", "E222", "E223", "E224")

            // Additives linked to hyperactivity in children (Southampton Six + others)
            val hyperactivityLinked = setOf("E102", "E104", "E110", "E122", "E124", "E129")

            // Additives linked to digestive issues
            val digestiveIssues = setOf("E220", "E221", "E222", "E223", "E224", "E249", "E250", "E251", "E252", "E954", "E965", "E966", "E967")

            // Additives linked to respiratory problems
            val respiratoryIssues = setOf("E220", "E221", "E222", "E223", "E224", "E225")

            // Additives linked to cardiovascular concerns
            val cardiovascularConcerns = setOf("E621", "E622", "E623", "E624", "E625", "E250", "E251")

            // Additives linked to neurological concerns
            val neurologicalConcerns = setOf("E621", "E951", "E954")

            // Additives linked to hormonal disruption
            val hormonalDisruption = setOf("E320", "E321")

            // Additives potentially harmful during pregnancy
            val pregnancyConcerns = setOf("E951", "E954", "E249", "E250", "E320", "E321")

            return when {
                code in cancerLinked -> "Potential cancer risk - studies suggest possible carcinogenic effects"
                code in hyperactivityLinked -> "ADHD/Hyperactivity - linked to behavioral issues in children"
                code in allergyTriggers -> "Allergic reactions - may cause skin rashes, hives, or breathing difficulties"
                code in digestiveIssues -> "Digestive problems - may cause nausea, stomach upset, or diarrhea"
                code in respiratoryIssues -> "Respiratory issues - may trigger asthma or breathing problems"
                code in cardiovascularConcerns -> "Heart/blood pressure concerns - may affect cardiovascular health"
                code in neurologicalConcerns -> "Neurological effects - potential headaches or brain-related symptoms"
                code in hormonalDisruption -> "Hormonal disruption - may interfere with endocrine system"
                code in pregnancyConcerns -> "Pregnancy concerns - not recommended during pregnancy or breastfeeding"
                code.startsWith("E1") -> "Cosmetic coloring - generally safe but may cause reactions in sensitive individuals"
                code.startsWith("E3") -> "Antioxidant properties - generally beneficial for health"
                code.startsWith("E4") -> "Texture modifier - usually safe, derived from natural sources"
                else -> "No major health concerns reported - considered safe in normal consumption levels"
            }
        }


        // --- Get Full Additive Info with Code Conversion ---
        fun getFullAdditiveInfo(rawTag: String): AdditiveInfo {
            val code = convertTagFormat(rawTag)
            val fullName = getAdditiveFullName(code)
            val category = getAdditiveCategory(code)
            val safetyInfo = getAdditiveSafetyInfo(code)
            val healthConcerns = getAdditiveHealthConcerns(code)
            return AdditiveInfo(code, fullName, category, safetyInfo, healthConcerns)
        }
    }
}
