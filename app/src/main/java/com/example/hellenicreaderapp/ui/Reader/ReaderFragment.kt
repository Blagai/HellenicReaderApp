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

        val backButton = binding.readerBack

        backButton.setOnClickListener {
            AppState.readNoBack = false
            findNavController().navigate(R.id.navigation_dashboard)
        }
    }

    private fun loadTextFromAssets(filename: String): String {
        return try {
            requireContext().assets.open("$filename.txt").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            "Failed to load: ${e.message}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
