package com.example.hellenicreaderapp.ui.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.hellenicreaderapp.R

class TranslationDialogFragment : DialogFragment() {
    private var word: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            word = it.getString(ARG_WORD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_translation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.originalGreekWord)
        textView.text = word
    }

    companion object {
        private const val ARG_WORD = "word"

        @JvmStatic
        fun newInstance(word: String) =
            TranslationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WORD, word)
                }
            }
    }
}

// 1 - Take word from Reader as string - Works
// 2 - Compare word to dictionary
// 3 - If only one definition, show that with all its descriptions
// 4 - If multiple definitions, more complex logic - I'll figure it out later