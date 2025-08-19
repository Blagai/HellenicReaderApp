package com.example.hellenicreaderapp

object AppState {
    var currentRead: String? = null
    var currentReadTitle: String? = null
    var readNoBack: Boolean = false

    var lastRead: String? = null // for Dashboard continue button

    var readTextsNum: Int = 0

    // var defaultAllTextReadingOrder = listOf() - to populate when I have more texts and figure out what I think is a good order
    var basicHymnReadingOrder = listOf("hohy1", "hohy2", "hohy3", "hohy4", "hohy5", "hohy6")

    var readingOrder = orderOfReading.NULL // Hardcoded test case

    var homeCurrentReadOrder = orderOfReading.NULL
    var homeCurrentInOrder: String? = null
    var isReadingThroughHome = false

    enum class orderOfReading {
        DEFAULTREAD,
        BASICHYMNS,
        NULL // Needed so that the function below would work
    }

    fun getCurrentOrder(): List<String> {
        return when (readingOrder) {
            orderOfReading.DEFAULTREAD -> basicHymnReadingOrder // For testing - change to defaultAllTextReadingOrder once it exists
            orderOfReading.BASICHYMNS -> basicHymnReadingOrder
            orderOfReading.NULL -> emptyList()
        }
    }
}