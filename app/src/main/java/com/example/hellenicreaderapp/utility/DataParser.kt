package com.example.hellenicreaderapp.utility

import java.io.InputStream

object DataParser {

    var litTranslations: List<String> = emptyList()
    var meaningTranslations: List<String> = emptyList()
    var grammarDetails: List<String> = emptyList()

    fun loadLitTranslations(inputStream: InputStream) {
        litTranslations = parseCsvToLines(inputStream)
    }

    fun loadMeaningTranslations(inputStream: InputStream) {
        meaningTranslations = parseCsvToLines(inputStream)
    }

    fun loadGrammarDetails(inputStream: InputStream) {
        grammarDetails = parseCsvToLines(inputStream)
    }

    fun parseCsvToLines(inputStream: InputStream): List<String> {
        val lines = inputStream.bufferedReader().use { it.readLines() }.filter { it.isNotBlank() }
        return lines
    }
}