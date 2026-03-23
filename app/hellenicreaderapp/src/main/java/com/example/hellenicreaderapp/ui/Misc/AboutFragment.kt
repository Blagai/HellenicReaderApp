package com.example.hellenicreaderapp.ui.Misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hellenicreaderapp.R
import com.google.android.material.tabs.TabLayout


class AboutFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity?.findViewById<TabLayout>(R.id.top_tab_layout))?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity?.findViewById<TabLayout>(R.id.top_tab_layout))?.visibility = View.VISIBLE
    }
}