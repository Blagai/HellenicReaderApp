package com.example.hellenicreaderapp.utility

import java.io.InputStream

object DataParser {

    var litTranslations: List<String> = emptyList()
    fun loadLitTranslations(inputStream: InputStream) {
        litTranslations = parseCsvToLines(inputStream)
    }

    fun parseCsvToLines(inputStream: InputStream): List<String> {
        val lines = inputStream.bufferedReader().use { it.readLines() }.filter { it.isNotBlank() }
        return lines
    }
}