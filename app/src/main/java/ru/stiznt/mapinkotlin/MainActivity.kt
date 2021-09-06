package ru.stiznt.mapinkotlin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.stiznt.mapinkotlin.DataBase.officeDB.DBConnector
import ru.stiznt.mapinkotlin.Models.Cabinet


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        supportActionBar!!.hide()
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_search, R.id.navigation_pos, R.id.navigation_scan, R.id.navigation_chat
        )
            .build()

/*        val db: DBConnector
        db = DBConnector(this)

        db.addDB(Cabinet("Г-341",4))
        db.addDB(Cabinet("Лестница восток",5))
        db.addDB(Cabinet("Г-342",7))
        db.addDB(Cabinet("Г-340",10))
        db.addDB(Cabinet("Г-339",14))
        db.addDB(Cabinet("Г-343",15))
        db.addDB(Cabinet("Г-344",16))
        db.addDB(Cabinet("Г-338",19))
        db.addDB(Cabinet("Г-337",20))
        db.addDB(Cabinet("Туалет восток",23))
        db.addDB(Cabinet("Г-345",25))
        db.addDB(Cabinet("Г-333",27))
        db.addDB(Cabinet("Г-332",30))
        db.addDB(Cabinet("Г-341",32))
        db.addDB(Cabinet("Г-330",34))
        db.addDB(Cabinet("Г-326",36))
        db.addDB(Cabinet("Г-324",38))
        db.addDB(Cabinet("Г-323",40))
        db.addDB(Cabinet("Буфет",42))
        db.addDB(Cabinet("Хол",45))
        db.addDB(Cabinet("Лестница центр",47))
        db.addDB(Cabinet("Г-318",48))
        db.addDB(Cabinet("Г-317",50))
        db.addDB(Cabinet("Г-313",52))
        db.addDB(Cabinet("Г-312",54))
        db.addDB(Cabinet("Г-311",56))
        db.addDB(Cabinet("Г-309",59))
        db.addDB(Cabinet("Г-328",61))
        db.addDB(Cabinet("Г-314",72))
        db.addDB(Cabinet("Г-315",73))
        db.addDB(Cabinet("Г-316",74))
        db.addDB(Cabinet("Г-319",75))
        db.addDB(Cabinet("Г-320",76))
        db.addDB(Cabinet("Г-321",77))
        db.addDB(Cabinet("Г-322",78))
        db.addDB(Cabinet("Г-327",79))
        db.addDB(Cabinet("Г-325",80))
        db.addDB(Cabinet("Туалет запад",81))
        db.addDB(Cabinet("Г-307",84))
        db.addDB(Cabinet("Г-305",86))
        db.addDB(Cabinet("Г-302",88))
        db.addDB(Cabinet("Г-304",89))
        db.addDB(Cabinet("Г-303",91))
        db.addDB(Cabinet("Г-301",95))
        db.addDB(Cabinet("Лестница запад",96))*/



        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
    }

}