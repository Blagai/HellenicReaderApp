package com.blagai.hellenicreaderapp.ui.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blagai.hellenicreaderapp.R
import com.blagai.hellenicreaderapp.utility.GrammarDetailsMap.mappedGrammarDetails
import com.blagai.hellenicreaderapp.utility.LitTranslationMap.mappedLitTranslations
import com.blagai.hellenicreaderapp.utility.MeaningTranslationMap.mappedMeaningTranslations

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

        val litTranslationText = view.findViewById<TextView>(R.id.wordTranslation)
        val litTranslations = mappedLitTranslations[word]
        if (litTranslations != null) {
            litTranslationText.text = "Lit. " + litTranslations.joinToString(", ")
        } else {
            litTranslationText.text = "No translation found"
        }

        val meaningTranslationText = view.findViewById<TextView>(R.id.wordTranslationMeaning)
        val meaningTranslations = mappedMeaningTranslations[word]
        if (meaningTranslations != null) {
            meaningTranslationText.text = "Meaning " + meaningTranslations.joinToString(", ") + " here"
        } else {
            meaningTranslationText.text = "No translation found"
        }

        val grammarDetailsText = view.findViewById<TextView>(R.id.wordGrammmarDetails)
        val grammarDetails = mappedGrammarDetails[word]
        if (grammarDetails != null) {
            grammarDetailsText.text = grammarDetails.joinToString(", ")
        } else {
            grammarDetailsText.text = "No details found"
        }

        //TODO save button functionality
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
