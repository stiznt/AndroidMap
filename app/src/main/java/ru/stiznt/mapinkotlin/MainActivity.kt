package ru.stiznt.mapinkotlin

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        saveText(false)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        supportActionBar!!.hide()
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_search, R.id.navigation_pos, R.id.navigation_scan, R.id.navigation_chat
        )
            .build()

        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
    }

    fun saveText(visibility: Boolean?) {
        val sPref: SharedPreferences
        sPref = getPreferences(MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putBoolean("visibility", visibility!!)
        ed.commit()
    }
}