package com.example.hellenicreaderapp.utility

object LitTranslationMap {
    private var lastSource: List<String>? = null
    private var cachedMap: Map<String, List<String>> = emptyMap()

    val mappedLitTranslations: Map<String, List<String>>
        get() {
            if (lastSource !== DataParser.litTranslations) {
                lastSource = DataParser.litTranslations
                cachedMap = lastSource!!.mapNotNull { line ->
                    val parts = line.split(";")
                    if (parts.size >= 2) {
                        // Trim both to avoid issues with spaces in CSV or text
                        parts[0].trim() to parts[1].trim()
                    } else null
                }.groupBy({ it.first }) { it.second }
            }
            return cachedMap
        }
}
