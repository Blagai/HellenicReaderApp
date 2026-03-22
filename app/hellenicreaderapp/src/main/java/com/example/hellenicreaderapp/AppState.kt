package com.example.hellenicreaderapp

import com.example.hellenicreaderapp.utility.Converters.toOrderOfReading
import com.example.hellenicreaderapp.utility.dataLastRead
import com.example.hellenicreaderapp.utility.dataReadTextsNum
import com.example.hellenicreaderapp.utility.getIntData
import com.example.hellenicreaderapp.utility.getStringData
import com.example.hellenicreaderapp.utility.homeLastRead
import com.example.hellenicreaderapp.utility.homeReadOrder
import com.example.hellenicreaderapp.utility.readOrder

object AppState {
    var currentRead: String? = null
    var readNoBack: Boolean = false
    var isReadingThroughHome = false
    // var defaultAllTextReadingOrder = listOf() - to populate when I have more texts and figure out what I think is a good order

    var readTextsNum: Int = 0
    var homeReadingOrder: OrderOfReading = OrderOfReading.NULL
    var homeCurrentInOrder: String = "hohy1" // Replace with check for selected read order
    var lastRead: String? = null // for Dashboard continue button
    var readingOrder: OrderOfReading = OrderOfReading.DEFAULTREAD // Replace with check for selected read order

    suspend fun loadData() {
        homeReadingOrder = if (getStringData(homeReadOrder) == "") {
            OrderOfReading.NULL
        } else {
            toOrderOfReading(getStringData(homeReadOrder))
        }

        homeCurrentInOrder = if (getStringData(homeLastRead) == "") {
            "hohy1" // Replace with check for selected read order
        } else {
            getStringData(homeLastRead)
        }

        lastRead = if (getStringData(dataLastRead) == "") {
            null
        } else {
            getStringData(dataLastRead)
        }

        readingOrder = if (getStringData(readOrder) == "") {
            OrderOfReading.DEFAULTREAD
        } else {
            toOrderOfReading(getStringData(readOrder))
        }

        readTextsNum = getIntData(dataReadTextsNum)
    }

    enum class OrderOfReading {
        DEFAULTREAD,
        BASICHYMNS,
        NULL // Needed so that the function below would work
    }

    // Might be better to work off a file I only load if a certain order is needed
    // Would require a pretty big refactoring of the code, so I'll have to think about it
    var basicHymnReadingOrder: List<String> = listOf("hohy1", "hohy2", "hohy3", "hohy4", "hohy5", "hohy6")

    fun getCurrentOrder(order: OrderOfReading): List<String> {
        return when (order) {
            OrderOfReading.DEFAULTREAD -> basicHymnReadingOrder // For testing - change to defaultAllTextReadingOrder once it exists
            OrderOfReading.BASICHYMNS -> basicHymnReadingOrder
            OrderOfReading.NULL -> emptyList()
        }
    }
}