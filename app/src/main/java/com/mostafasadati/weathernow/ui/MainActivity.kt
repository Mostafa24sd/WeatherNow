package com.mostafasadati.weathernow.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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
import com.mostafasadati.weathernow.widgets.ForecastCalculator
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
            R.id.rate -> rateToApp()
            R.id.contactMe -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, "mostafa24sadati@live.com")
                intent.putExtra(Intent.EXTRA_SUBJECT, "About WeatherNow 2")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            R.id.githubPage -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/Mostafa24sd")
                startActivity(i)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun rateToApp() {
        Toast.makeText(this, "Thanks:)", Toast.LENGTH_SHORT).show()

    }
}