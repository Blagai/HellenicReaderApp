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
        val content = loadTextFromAssets(textId)
        binding.readerTextView.text = content
        binding.readerTitle.text = title

        AppState.currentRead = textId
        AppState.currentReadTitle = title
        AppState.readNoBack = true
        AppState.lastRead = textId

        val backButton = binding.readerBack
        val translateButton = binding.readerTranslate
        val continueButton = binding.continueButton

        var isTranslated = false

        translateButton.setOnClickListener {
            val currentId = AppState.currentRead
            if (!isTranslated) {
                val translatedId = "${currentId}_eng"
                val translatedContent = loadTextFromAssets(translatedId)
                binding.readerTextView.text = translatedContent

                val translatedTitle = when (translatedId) {
                    "hohy1_eng" -> "To Dionysus"

                    else -> binding.readerTitle.text.toString()
                }
                binding.readerTitle.text = translatedTitle
                isTranslated = true

                translateButton.setText(R.string.AllOriginal)
            } else {
                val originalContent = currentId?.let { it1 -> loadTextFromAssets(it1) }
                binding.readerTextView.text = originalContent
                binding.readerTitle.text = AppState.currentReadTitle
                isTranslated = false

                translateButton.setText(R.string.AllTranslate)
            }
        }

        continueButton.setOnClickListener {
            val currentId = AppState.currentRead
            val readOrder = AppState.getCurrentOrder()
            val currentIndex = readOrder.indexOf(currentId)

            if (currentIndex != -1 && currentIndex < readOrder.size - 1) {
                val nextId = readOrder[currentIndex + 1]
                AppState.currentRead = nextId
                AppState.lastRead = nextId

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

        // Should make the system back and the back button actually take the user to the text picker
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
