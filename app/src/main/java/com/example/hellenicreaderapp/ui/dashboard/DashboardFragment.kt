package com.example.hellenicreaderapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hellenicreaderapp.R
import com.example.hellenicreaderapp.databinding.FragmentDashboardBinding

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
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val hymn1: Button = binding.hoHy1
        val hymn2: Button = binding.hoHy2
        val hymn3: Button = binding.hoHy3
        val hymn4: Button = binding.hoHy4
        val hymn5: Button = binding.hoHy5

        hymn1.setOnClickListener {
            val bundle = Bundle().apply {
                putString("textId", "hohy1")
                putString("title", "Εἲς Διώνυσον")
            }
            findNavController().navigate(R.id.readerFragment, bundle)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}