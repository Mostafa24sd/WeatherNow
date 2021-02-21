package com.mostafasadati.weathernow.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.widgets.WidgetConfigActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        setSupportActionBar(main_toolbar)

        navigation_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, main_toolbar, R.string.open, R.string.close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                navController.navigate(R.id.action_mainFragment_to_mapFragment)
            }
            R.id.setting -> navController.navigate(R.id.action_mainFragment_to_settingsFragment)
            R.id.rate -> navController.navigate(R.id.action_mainFragment_to_blankFragment)
            R.id.report -> Toast.makeText(this, "report", Toast.LENGTH_SHORT).show()
            R.id.contact -> Toast.makeText(this, "contact", Toast.LENGTH_SHORT).show()
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}