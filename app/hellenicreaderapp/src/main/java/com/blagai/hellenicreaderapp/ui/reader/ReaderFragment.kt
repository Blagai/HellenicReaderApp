package com.blagai.hellenicreaderapp.ui.reader

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.blagai.hellenicreaderapp.AppState
import com.blagai.hellenicreaderapp.R
import com.blagai.hellenicreaderapp.databinding.FragmentReaderBinding
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.blagai.hellenicreaderapp.AppState.readTextsNum
import com.blagai.hellenicreaderapp.ui.popups.TranslationDialogFragment
import com.blagai.hellenicreaderapp.utility.TitleMap
import com.blagai.hellenicreaderapp.utility.TranslatedTitleMap
import com.blagai.hellenicreaderapp.utility.dataLastRead
import com.blagai.hellenicreaderapp.utility.dataReadTextsNum
import com.blagai.hellenicreaderapp.utility.homeLastRead
import com.blagai.hellenicreaderapp.utility.saveIntData
import com.blagai.hellenicreaderapp.utility.saveStringData
import kotlinx.coroutines.launch

class ReaderFragment : Fragment() {
    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val originalId = arguments?.getString("textId").toString() // Fucking stupid that I have to change a "maybe string" to a string
        val originalTitle = TitleMap.mappedTitles[originalId]

        val textId = ("OriginalTexts/$originalId.txt")

        val loadedContent = loadTextFromAssets(textId)
        val parsedContent = loadedContent.trim().lines().joinToString()

        val translatedId = "Translations/${originalId}_eng.txt"
        val translatedTitle = TranslatedTitleMap.mappedTitles[originalId]
        val loadedTranslatedContent = loadTextFromAssets(translatedId)
        val parsedTranslatedContent = loadedTranslatedContent.trim().lines().joinToString()
        var isTranslated = false

        makeWordsClickable(binding.readerTextView, parsedContent)
        binding.readerTitle.text = originalTitle

        AppState.readNoBack = true
        AppState.currentRead = originalId

        if (!AppState.isReadingThroughHome) {
            AppState.lastRead = originalId
            lifecycleScope.launch {
                saveStringData(dataLastRead, originalId)
            }
        } else {
            if (originalId != "") {
                AppState.homeCurrentInOrder = originalId
                lifecycleScope.launch {
                    saveStringData(homeLastRead, originalId)
                }
            }
        }

        val backButton = binding.readerBack
        val translateButton = binding.readerTranslate
        val continueButton = binding.continueButton

        translateButton.setOnClickListener {
            if (!isTranslated) {
                binding.readerTextView.text = parsedTranslatedContent
                binding.readerTitle.text = translatedTitle

                isTranslated = true

                translateButton.setText(R.string.AllOriginal)
            } else {
                makeWordsClickable(binding.readerTextView, parsedContent)
                binding.readerTitle.text = originalTitle

                isTranslated = false

                translateButton.setText(R.string.AllTranslate)
            }
        }

        continueButton.setOnClickListener {
            val readOrder = if (!AppState.isReadingThroughHome) AppState.getCurrentOrder(AppState.readingOrder) else AppState.getCurrentOrder(AppState.homeReadingOrder)
            val currentIndex = readOrder.indexOf(originalId)

            if (currentIndex != -1 && currentIndex < readOrder.size - 1) {
                val nextId = readOrder[currentIndex + 1]

                val bundle = Bundle().apply {
                    putString("textId", nextId)
                }

                findNavController().navigate(R.id.readerFragment, bundle)

                readTextsNum++ // TODO add check for each text if it has been read already
                lifecycleScope.launch {
                    saveIntData(dataReadTextsNum, readTextsNum)
                }
            } else {
                // Show error
            }
        }

        backButton.setOnClickListener {
            AppState.readNoBack = false
            findNavController().navigate(R.id.navigation_dashboard)
        }

        // System back should not switch user between tabs ever
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AppState.readNoBack = false
                findNavController().popBackStack()
            }
        })

    }

    private fun loadTextFromAssets(filename: String): String {
        return try {
            requireContext().assets.open(filename).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to load: ${e.message}"
        }
    }

    private fun makeWordsClickable(textView: TextView, text: String) {
        val spannable = SpannableString(text)
        val wordRegex = Regex("\\S+")
        val punctuationToTrim = ".,;·!?:()[]{}«»\"—".toCharArray()

        wordRegex.findAll(text).forEach { matchResult ->
            val fullWord = matchResult.value
            val word = fullWord.trim { it in punctuationToTrim }
            
            if (word.isNotEmpty()) {
                val start = matchResult.range.first + fullWord.indexOf(word)
                val end = start + word.length

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val dialog = TranslationDialogFragment.newInstance(word)
                        dialog.show(parentFragmentManager, "TranslationDialog")
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = requireContext().getColor(R.color.white)
                    }
                }
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = spannable
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
