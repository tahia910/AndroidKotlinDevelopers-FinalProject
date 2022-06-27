package com.example.android.nextreminder.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.nextreminder.R
import com.example.android.nextreminder.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.randomFragment, R.id.bookmarkFragment)
        )
        // Update the AppBar title with the correct screen name
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Set up the bottom bar
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            // If the user opens another tab while making a request, cancel it
            viewModel.cancelRandomRequest()
        }

        // Observe the database to have the data ready as early as possible
        viewModel.bookmarkList.observe(this) {
            // TODO: suggest to have a look at bookmarks?
        }
    }

    // Display fragment's SnackBars above the bottom bar
    fun getBottomNavView(): View {
        return binding.bottomNavView
    }
}