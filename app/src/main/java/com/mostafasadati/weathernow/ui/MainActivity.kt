package com.mostafasadati.weathernow.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mostafasadati.weathernow.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment), drawer_layout
        )

        setSupportActionBar(main_toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigation_view.setupWithNavController(navController)
        navigation_view.setNavigationItemSelectedListener(this)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            drawer_layout
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapFragment -> {
                navController.navigate(R.id.mapFragment)
            }
            R.id.settingsFragment -> {
                navController.navigate(R.id.settingsFragment)
            }
            R.id.rate -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}