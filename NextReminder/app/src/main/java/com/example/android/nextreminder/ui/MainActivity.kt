package com.example.android.nextreminder.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.android.nextreminder.R
import com.example.android.nextreminder.databinding.ActivityMainBinding
import com.example.android.nextreminder.ui.bookmark.BookmarkFragment
import com.example.android.nextreminder.ui.home.HomeResultFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragment)
        navController.setGraph(R.navigation.main_navigation)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val currentlySelectedTab = binding.tabLayout.selectedTabPosition
            // Navigating back from the bookmark screen does not automatically change the tab,
            // so we manually change it
            if (destination.id == R.id.homeResultFragment && currentlySelectedTab != 0) {
                changeTab(0)
            }
        }

        // Handle the bottom back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackButton()
            }
        })

        setTabLayoutListener()
    }

    /**
     * Set up TabLayout with the corresponding Fragments.
     * Keep the logic simple instead of using a ViewPager since we want to use Navigation library.
     * https://material.io/components/tabs/android#using-tabs
     * https://stackoverflow.com/a/67004726
     */
    private fun setTabLayoutListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = getCurrentlyVisibleFragment()
                when (tab?.position) {
                    0 -> {
                        // Use navigateUp() for BookmarkFragment too so the user can go back to
                        // the search result if they originally came from there
                        if (fragment is BookmarkFragment || fragment is HomeResultFragment) {
                            navController.navigateUp()
                        } else {
                            navController.navigate(R.id.homeFragment)
                        }
                    }
                    1 -> navController.navigate(R.id.bookmarkFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            /**
             *  If a currently selected tab is tapped again, only handle the search result screen
             *  case (where we bring the user back to the home/search screen)
             */
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val fragment = getCurrentlyVisibleFragment()
                if (tab?.position == 0 && fragment is HomeResultFragment) {
                    navController.navigateUp()
                }
            }
        })
    }

    // Manually change the selected tab
    fun changeTab(position: Int) {
        binding.tabLayout.getTabAt(position)?.select()
    }

    // NavigationUI method to handle the AppBar's back button
    override fun onSupportNavigateUp(): Boolean {
        return handleBackButton()
    }

    // Common method for the AppBar and bottom back button
    private fun handleBackButton(): Boolean {
        val fragment = getCurrentlyVisibleFragment()
        return if (fragment is BookmarkFragment) {
            changeTab(0)
            true
        } else {
            navController.navigateUp()
        }
    }

    private fun getCurrentlyVisibleFragment(): Fragment? {
        val stack =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .childFragmentManager.fragments
        for (fragment in stack) {
            if (fragment != null && fragment.isVisible) {
                return fragment
            }
        }
        return null
    }
}