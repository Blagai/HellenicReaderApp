package com.example.hellenicreaderapp.ui.Reader

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
import com.example.hellenicreaderapp.AppState
import com.example.hellenicreaderapp.R
import com.example.hellenicreaderapp.databinding.FragmentReaderBinding
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.example.hellenicreaderapp.ui.popups.TranslationDialogFragment
import com.example.hellenicreaderapp.utility.homeLastRead
import com.example.hellenicreaderapp.utility.saveStateData
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

        val originalId = arguments?.getString("textId")
        val textId = ("OriginalTexts/$originalId")
        val loadedContent = loadTextFromAssets(textId)
        val preparsedContent = loadedContent.trim().lines()
        val parsedTitle = preparsedContent.firstOrNull() ?: ""
        val parsedText = if(preparsedContent.size > 1) {
            preparsedContent.drop(1).joinToString("\n") } else { "" }
        makeWordsClickable(binding.readerTextView, parsedText)
        binding.readerTitle.text = parsedTitle
        AppState.currentRead = arguments?.getString("textId")
        AppState.currentReadTitle = parsedTitle
        AppState.readNoBack = true
        if (!AppState.isReadingThroughHome) {
            AppState.lastRead = textId
        } else {
            if (originalId != null) {
                AppState.homeCurrentInOrder = originalId
                lifecycleScope.launch {
                    saveStateData(homeLastRead, originalId)
                }
            }
        }

        val backButton = binding.readerBack
        val translateButton = binding.readerTranslate
        val continueButton = binding.continueButton

        var isTranslated = false

        translateButton.setOnClickListener {
            val currentId = AppState.currentRead
            if (!isTranslated) {
                val translatedId = "${currentId}_eng"
                val loadedTranslatedContent = loadTextFromAssets(translatedId)
                val preparsedTranslatedContent = loadedTranslatedContent.trim().lines()
                val translatedTitle = when (currentId) {
                    "hohy1" -> getString(R.string.hymn1_title_1line)
                    "hohy2" -> getString(R.string.hymn2_title_1line)
                    "hohy3" -> getString(R.string.hymn3_title_1line)
                    "hohy4" -> getString(R.string.hymn4_title_1line)
                    "hohy5" -> getString(R.string.hymn5_title_1line)
                    "hohy6" -> getString(R.string.hymn6_title_1line)
                    else -> ""
                }
                val parsedTranslatedText = if(preparsedTranslatedContent.size > 1) {
                    preparsedTranslatedContent.drop(1).joinToString("\n") } else { "" }
                binding.readerTextView.text = parsedTranslatedText
                binding.readerTitle.text = translatedTitle

                isTranslated = true

                translateButton.setText(R.string.AllOriginal)
            } else {
                val originalTextId = "OriginalTexts/$currentId"
                val loadedContent = loadTextFromAssets(originalTextId)
                val preparsedContent = loadedContent.trim().lines()
                val parsedText = if(preparsedContent.size > 1) {
                    preparsedContent.drop(1).joinToString("\n") } else { "" }
                
                makeWordsClickable(binding.readerTextView, parsedText)
                binding.readerTitle.text = AppState.currentReadTitle
                isTranslated = false

                translateButton.setText(R.string.AllTranslate)
            }
        }

        continueButton.setOnClickListener {
            val currentId = AppState.currentRead
            val readOrder = if (!AppState.isReadingThroughHome) AppState.getCurrentOrder(AppState.readingOrder) else AppState.getCurrentOrder(AppState.homeCurrentReadOrder)

            val currentIndex = readOrder.indexOf(currentId)

            if (currentIndex != -1 && currentIndex < readOrder.size - 1) {
                val nextId = readOrder[currentIndex + 1]
                AppState.currentRead = nextId
                if (!AppState.isReadingThroughHome) {
                    AppState.lastRead = nextId
                } else {
                    AppState.homeCurrentInOrder = nextId
                }

                // TODO fix titles to use a global list too - should use a map
                val nextTitle = when (nextId) {
                    "hohy1" -> getString(R.string.hymn1_greek)
                    "hohy2" -> getString(R.string.hymn2_greek)
                    "hohy3" -> getString(R.string.hymn3_greek)
                    "hohy4" -> getString(R.string.hymn4_greek)
                    "hohy5" -> getString(R.string.hymn5_greek)
                    "hohy6" -> getString(R.string.hymn6_greek)
                    else -> ""
                }

                AppState.currentReadTitle = nextTitle

                val bundle = Bundle().apply {
                    putString("textId", nextId)
                    putString("title", nextTitle)
                }

                findNavController().navigate(R.id.readerFragment, bundle)

                AppState.readTextsNum++
            } else {
                // Show error
            }
        }

        // Should make the system back take to last text
        // and the back button take the user to the text picker
        // System back should not switch user between tabs ever
        backButton.setOnClickListener {
            AppState.readNoBack = false
            findNavController().navigate(R.id.navigation_dashboard)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AppState.readNoBack = false
                findNavController().popBackStack()
            }
        })

    }

    private fun loadTextFromAssets(filename: String): String {
        return try {
            // Check if it's an English version (contains "_eng")
            val path = if (filename.contains("_eng")) {
                "EnglishVersions/$filename.txt"
            } else {
                "$filename.txt"
            }

            requireContext().assets.open(path).bufferedReader().use { it.readText() }
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
