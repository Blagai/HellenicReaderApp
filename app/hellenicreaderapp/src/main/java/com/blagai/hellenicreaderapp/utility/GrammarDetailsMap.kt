package com.blagai.hellenicreaderapp.utility

object GrammarDetailsMap {
    private var lastSource: List<String>? = null
    private var cachedMap: Map<String, List<String>> = emptyMap()

    val mappedGrammarDetails: Map<String, List<String>>
        get() {
            if (lastSource !== DataParser.grammarDetails) {
                lastSource = DataParser.grammarDetails
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