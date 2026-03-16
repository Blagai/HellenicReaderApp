package com.example.hellenicreaderapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hellenicreaderapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        actionBar!!.title = " Hellenic Reader App"
        actionBar.subtitle = " Read Ancient Greek texts for free!"
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        val tabLayout = findViewById<TabLayout>(R.id.top_tab_layout)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val tabStrip = tabLayout.getChildAt(0) as ViewGroup

        tabLayout.addTab(tabLayout.newTab().setText("Home"))
        tabLayout.addTab(tabLayout.newTab().setText("Choose text"))
        tabLayout.addTab(tabLayout.newTab().setText("Saved"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> navController.navigate(R.id.navigation_home)
                    1 -> navController.navigate(R.id.navigation_dashboard)
                    2 -> navController.navigate(R.id.navigation_notifications)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()
            R.id.about -> Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}