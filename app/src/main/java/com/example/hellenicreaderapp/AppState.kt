package com.example.hellenicreaderapp

object AppState {
    var currentRead: String? = null
    var currentReadTitle: String? = null
    var readNoBack: Boolean = false

    var lastRead: String? = null

    var readTextsNum: Int? = 0

    var basicHymnReadingOrder = listOf("hohy1", "hohy2", "hohy3", "hohy4", "hohy5", "hohy6")

    var readingOrder = orderOfReading.BASICHYMNS // Hardcoded test case

    enum class orderOfReading {
        BASICHYMNS
    }

    fun getCurrentOrder(): List<String> {
        return when (readingOrder) {
            orderOfReading.BASICHYMNS -> basicHymnReadingOrder
        }
    }
}