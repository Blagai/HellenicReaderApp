package com.example.hellenicreaderapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hellenicreaderapp.AppState
import com.example.hellenicreaderapp.databinding.FragmentHomeBinding
import com.example.hellenicreaderapp.ui.popups.SelectModeDialogFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val readTextsNum = binding.textsReadNum
        homeViewModel.readTextsNum.observe(viewLifecycleOwner) {
            readTextsNum.text = it
        }


        val startButton = binding.homeStartButton
        startButton.setOnClickListener {
            SelectModeDialogFragment().show(parentFragmentManager, "SelectMode")
        }

        // TODO #2 add a separate continue button because I think it's better UX
        val continueButton = binding.homeContinueButton
        continueButton.visibility = if (AppState.homeCurrentReadOrder != AppState.orderOfReading.NULL) View.VISIBLE else View.GONE
        // The button functionality will also need AppState to save which was the last text
        // the user was on (in-order last text should be separate from regular last text)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}