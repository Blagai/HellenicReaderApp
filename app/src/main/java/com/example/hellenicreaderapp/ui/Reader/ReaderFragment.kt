package com.example.hellenicreaderapp.ui.Reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hellenicreaderapp.AppState
import com.example.hellenicreaderapp.R
import com.example.hellenicreaderapp.databinding.FragmentReaderBinding
import androidx.activity.OnBackPressedCallback


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

        val textId = arguments?.getString("textId") ?: return
        val title = arguments?.getString("title") ?: ""
        val loadedContent = loadTextFromAssets(textId)
        val preparsedContent = loadedContent.lines()
        val parsedTitle = preparsedContent.firstOrNull() ?: ""
        val parsedText = if(preparsedContent.size > 1) {
            preparsedContent.drop(1).joinToString("\n") } else { "" }
        binding.readerTextView.text = parsedText // To change when I figure out the logic
        binding.readerTitle.text = parsedTitle // To change when I figure out the logic
        AppState.currentRead = textId
        AppState.currentReadTitle = title
        AppState.readNoBack = true
        if (!AppState.isReadingThroughHome) {
            AppState.lastRead = textId
        } else {
            AppState.homeCurrentInOrder = textId
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
                val preparsedTranslatedContent = loadedTranslatedContent.lines()
                val parsedTranslatedTitle = preparsedTranslatedContent.firstOrNull() ?: ""
                val parsedTranslatedText = if(preparsedTranslatedContent.size > 1) {
                    preparsedTranslatedContent.drop(1).joinToString("\n") } else { "" }
                binding.readerTextView.text = parsedTranslatedText
                binding.readerTitle.text = parsedTranslatedTitle

                isTranslated = true

                translateButton.setText(R.string.AllOriginal)
            } else {
                // There has to be a better way to do this
                val originalContent = currentId?.let { it1 -> loadTextFromAssets(it1) }
                binding.readerTextView.text = originalContent
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


                // TODO fix titles to use a global list too
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
            requireActivity().onBackPressedDispatcher.onBackPressed()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
