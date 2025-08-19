package com.example.hellenicreaderapp.ui.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.hellenicreaderapp.AppState
import com.example.hellenicreaderapp.R

class SelectModeDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_order_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button1 = view.findViewById<Button>(R.id.buttonOrderDefault)
        val button2 = view.findViewById<Button>(R.id.buttonOrder2)

        button1.setOnClickListener {
            AppState.readingOrder = AppState.orderOfReading.DEFAULTREAD // Remove once ReaderFragment is updated accordingly
            AppState.homeCurrentReadOrder = AppState.orderOfReading.DEFAULTREAD
            AppState.isReadingThroughHome = true
            // Navigate to reader fragment with the first item of the selected order
            val firstId = AppState.getCurrentOrder().first()
            val firstTitle = "Εἲς Διώνυσον"
            val bundle = Bundle().apply {
                putString("textId", firstId)
                putString("title", firstTitle) // I should really fix the title logic to take from a global list
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dismiss()
        }

        button2.setOnClickListener {
            // Same as the former but for second order
        }
    }
}
