package com.blagai.hellenicreaderapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.blagai.hellenicreaderapp.AppState
import com.blagai.hellenicreaderapp.R
import com.blagai.hellenicreaderapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textLastRead = binding.lastRead
        dashboardViewModel.lastReadText.observe(viewLifecycleOwner) {
            textLastRead.text = it
        }

        val dashContButton = binding.dashContinueButton
        dashContButton.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", AppState.lastRead)
            }
            findNavController().navigate(R.id.readerFragment, bundle)
        }

        dashboardViewModel.refreshLastRead()

        val hymn1: Button = binding.hoHy1
        val hymn2: Button = binding.hoHy2
        val hymn3: Button = binding.hoHy3
        val hymn4: Button = binding.hoHy4
        val hymn5: Button = binding.hoHy5
        val hymn6: Button = binding.hoHy6

        hymn1.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy1")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        hymn2.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy2")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        hymn3.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy3")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        hymn4.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy4")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        hymn5.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy5")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        hymn6.setOnClickListener {
            AppState.isReadingThroughHome = false
            val bundle = Bundle().apply {
                putString("textId", "hohy6")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
            dashboardViewModel.refreshLastRead()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}