package com.example.hellenicreaderapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.hellenicreaderapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.example.hellenicreaderapp.utility.DataParser
import com.example.hellenicreaderapp.utility.dataStoreManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var ignoreTabSelection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager.dataStoreInit(this)
        lifecycleScope.launch {
            AppState.loadReadData()
        }

        AppState.readNoBack = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load translations from assets on startup
        try {
            assets.open("Data/lit_translations.csv").use { inputStream ->
                DataParser.loadLitTranslations(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            assets.open("Data/meaning_translations.csv").use { inputStream ->
                DataParser.loadMeaningTranslations(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            assets.open("Data/grammar_details.csv").use { inputStream ->
                DataParser.loadGrammarDetails(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val actionBar = supportActionBar

        actionBar!!.title = " Hellenic Reader App"
        actionBar.subtitle = " Read Ancient Greek texts for free!"
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        val tabLayout = findViewById<TabLayout>(R.id.top_tab_layout)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        tabLayout.addTab(tabLayout.newTab().setText("Home"))
        tabLayout.addTab(tabLayout.newTab().setText("Choose text"))
        tabLayout.addTab(tabLayout.newTab().setText("Saved"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (ignoreTabSelection) return

                when (tab.position) {
                    0 -> navController.navigate(R.id.navigation_home)
                    1 -> { 
                        if (AppState.readNoBack && AppState.currentRead != null) {
                            val bundle = Bundle().apply {
                                putString("textId", AppState.currentRead)
                                putString("title", AppState.currentReadTitle)
                            }
                            navController.navigate(R.id.readerFragment, bundle)
                        } else {
                            navController.navigate(R.id.navigation_dashboard)
                        }
                    }

                    2 -> navController.navigate(R.id.navigation_notifications)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> selectTabSafely(tabLayout, 0)
                R.id.navigation_dashboard -> selectTabSafely(tabLayout, 1)
                R.id.readerFragment -> selectTabSafely(tabLayout, 1)
                R.id.navigation_notifications -> selectTabSafely(tabLayout, 2)
            }
        }
    }

    private fun selectTabSafely(tabLayout: TabLayout, index: Int) {
        val tab = tabLayout.getTabAt(index) ?: return
        if (!tab.isSelected) {
            ignoreTabSelection = true
            tabLayout.selectTab(tab)
            ignoreTabSelection = false
        }
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