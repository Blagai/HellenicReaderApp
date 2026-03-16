package com.example.hellenicreaderapp.utility

import java.io.InputStream

object DataParser {
    var litTranslation: String = """"""
    var litTranslations: List<String> = emptyList()

    fun parseCsvToLines(inputStream: InputStream): List<String> {
        val lines = inputStream.bufferedReader().use { it.readLines() }.filter { it.isNotBlank() }
        litTranslations = lines
        litTranslation = lines.joinToString("\n")
        return lines
    }
}