package com.friendspharma.app.core.util

import com.friendspharma.app.R


object Common {

    fun isValidMobile(mobile: String): Boolean {

        return mobile.isNotEmpty() && mobile.length == 11 && !mobile.startsWith(
            "011"
        ) && !mobile.startsWith("012") && mobile.startsWith("01")
    }

    fun isNumeric(string: String): Boolean {
        return string.toDoubleOrNull() != null
    }

    val bannersMap: HashMap<String, Int> = hashMapOf(
        "ACI LIMITETD" to R.drawable.banner_100,
        "Globe" to R.drawable.banner_101,
        "Labaid" to R.drawable.banner_102,
        "ORION" to R.drawable.banner_103,
        "DABUR" to R.drawable.banner_104,
        "IBN SINA" to R.drawable.banner_105,
        "Albion Laboratories Limited" to R.drawable.banner_106,
        "ACME" to R.drawable.banner_107,
        "Aristopharma LTD" to R.drawable.banner_108,
        "EDRUC PHAMACEUTICAL LTD." to R.drawable.banner_109,
        "General Pharmaceuticals Limited" to R.drawable.banner_110,
        "SERVIER" to R.drawable.banner_111,
        "White Horse Pharmaceuticals Ltd." to R.drawable.banner_112,
        "Square Pharmaceutical" to R.drawable.banner_113,
        "Unimed" to R.drawable.banner_115,
        "NIPRO JMI Pharma Ltd" to R.drawable.banner_116,
        "HAMDARD LABORATORIS BANGLADESH" to R.drawable.banner_117,
        "PHARMASIA" to R.drawable.banner_118,
        "BELSEN" to R.drawable.banner_119,
        "nuvista pharma limited" to R.drawable.banner_120,
        "Drug International Limited" to R.drawable.banner_28,
        "Healthcare Pharmaceuticals Limited" to R.drawable.banner_29,
        "Popular Pharmaceuticals Ltd." to R.drawable.banner_30,
        "Renata Limited" to R.drawable.banner_31,
        "Incepta Pharmaceuticals Ltd." to R.drawable.banner_32,
        "Radiant Pharmaceuticals Limited" to R.drawable.banner_33,
        "Beximco" to R.drawable.banner_34,
        "OPSONIN PHARMA LTD" to R.drawable.banner_35,
        "Beacon Pharma" to R.drawable.banner_36,
        "Eskayef Pharmaceuticals Ltd." to R.drawable.banner_37,
        "S M C" to R.drawable.banner_91,
        "Fresh Mgi" to R.drawable.banner_92,
        "Jayson" to R.drawable.banner_93,
        "NAVANA" to R.drawable.banner_94,
        "SANOFI" to R.drawable.banner_95,
        "SQUARE TOILETRIES LTD" to R.drawable.banner_96,
        "ZISKA" to R.drawable.banner_97,
        "Botanic laboratories" to R.drawable.banner_98,
        "BASHUNDHARA PEPAR" to R.drawable.banner_99
    )

    val companyMap: HashMap<String, Int> = hashMapOf(
        "Renata Limited" to R.drawable.company_1,
        "Beximco" to R.drawable.company_2,
        "Incepta Pharmaceuticals Ltd." to R.drawable.company_3,
        "Botanic laboratories" to R.drawable.company_4,
        "BASHUNDHARA PEPAR" to R.drawable.company_5,
        "ACI LIMITETD" to R.drawable.company_6,
        "Globe" to R.drawable.company_7,
        "Labaid" to R.drawable.company_8,
        "ORION" to R.drawable.company_9,
        "DABUR" to R.drawable.company_10,
        "Pacific limited" to R.drawable.company_11,
        "IBN SINA" to R.drawable.company_12,
        "Albion Laboratories Limited" to R.drawable.company_13,
        "ACME" to R.drawable.company_14,
        "Aristopharma LTD" to R.drawable.company_15,
        "EDRUC PHAMACEUTICAL LTD." to R.drawable.company_16,
        "THAI PLASTIC INDUSTRIES" to R.drawable.company_17,
        "General Pharmaceuticals Limited" to R.drawable.company_18,
        "SERVIER" to R.drawable.company_19,
        "White Horse Pharmaceuticals Ltd." to R.drawable.company_20,
        "Square Pharmaceutical" to R.drawable.company_21,
        "Unimed" to R.drawable.company_22,
        "BENGAL" to R.drawable.company_23,
        "NIPRO JMI Pharma Ltd" to R.drawable.company_24,
        "HAMDARD LABORATORIS BANGLADESH" to R.drawable.company_25,
        "PHARMASIA" to R.drawable.company_26,
        "BELSEN" to R.drawable.company_28,
        "nuvista pharma limited" to R.drawable.company_29,
        "SQUARE TOILETRIES LTD" to R.drawable.company_30,
        "SANOFI" to R.drawable.company_31,
        "NAVANA" to R.drawable.company_32,
        "Jayson" to R.drawable.company_33,
        "S M C" to R.drawable.company_34,
        "Fresh Mgi" to R.drawable.company_35,
        "Popular Pharmaceuticals Ltd." to R.drawable.company_36,
        "ZISKA" to R.drawable.company_37,
        "OPSONIN PHARMA LTD" to R.drawable.company_38,
        "Eskayef Pharmaceuticals Ltd." to R.drawable.company_39,
        "Beacon Pharma" to R.drawable.company_40,
        "Drug International Limited" to R.drawable.company_41,
        "Healthcare Pharmaceuticals Limited" to R.drawable.company_42,
        "Radiant Pharmaceuticals Limited" to R.drawable.company_43
    )

}