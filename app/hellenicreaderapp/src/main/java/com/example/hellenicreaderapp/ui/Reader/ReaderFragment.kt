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
import com.example.hellenicreaderapp.AppState.readTextsNum
import com.example.hellenicreaderapp.ui.popups.TranslationDialogFragment
import com.example.hellenicreaderapp.utility.TitleMap
import com.example.hellenicreaderapp.utility.TranslatedTitleMap
import com.example.hellenicreaderapp.utility.dataLastRead
import com.example.hellenicreaderapp.utility.dataReadTextsNum
import com.example.hellenicreaderapp.utility.homeLastRead
import com.example.hellenicreaderapp.utility.saveIntData
import com.example.hellenicreaderapp.utility.saveStringData
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
            AppState.lastRead = textId
            lifecycleScope.launch {
                saveStringData(dataLastRead, textId)
            }
        } else {
            if (originalId != null) {
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

                readTextsNum++
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
