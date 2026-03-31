package com.blagai.hellenicreaderapp.utility

import java.io.InputStream

@Suppress("RedundantSuspendModifier")

object DataParser {

    suspend fun parseFile(inputStream : InputStream) : List<String> {
        val res: List<String> = parseCsvToLines(inputStream)
        return res
    }

    private suspend fun parseCsvToLines(inputStream: InputStream): List<String> {
        val lines = inputStream.bufferedReader().use { it.readLines() }.filter { it.isNotBlank() }
        return lines
    }
}