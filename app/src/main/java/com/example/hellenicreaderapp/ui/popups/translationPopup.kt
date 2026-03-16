package com.example.hellenicreaderapp.ui.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.hellenicreaderapp.R
import com.example.hellenicreaderapp.utility.LitTranslationMap.mappedLitTranslations

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

        val translationText = view.findViewById<TextView>(R.id.wordTranslation)
        val translations = mappedLitTranslations[word]
        if (translations != null) {
            translationText.text = "Lit. " + translations.joinToString(", ")
        } else {
            translationText.text = "No translation found"
        }
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
