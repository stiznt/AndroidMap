package ru.stiznt.mapinkotlin

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

        val db: DBConnector
        db = DBConnector(this)

        if (db.isEmpty) {
            db.addDB(Cabinet("Буфет", 36))
            db.addDB(Cabinet("Г-301", 1))
            db.addDB(Cabinet("Г-302", 9))
            db.addDB(Cabinet("Г-303", 3))
            db.addDB(Cabinet("Г-304", 5))
            db.addDB(Cabinet("Г-305", 14))
            db.addDB(Cabinet("Г-307", 12))
            db.addDB(Cabinet("Г-309", 18))
            db.addDB(Cabinet("Г-311", 43))
            db.addDB(Cabinet("Г-312", 42))
            db.addDB(Cabinet("Г-313", 41))
            db.addDB(Cabinet("Г-314", 49))
            db.addDB(Cabinet("Г-315", 50))
            db.addDB(Cabinet("Г-316", 51))
            db.addDB(Cabinet("Г-317", 40))
            db.addDB(Cabinet("Г-318", 39))
            db.addDB(Cabinet("Г-319", 52))
            db.addDB(Cabinet("Г-320", 53))
            db.addDB(Cabinet("Г-321", 67))
            db.addDB(Cabinet("Г-322", 70))
            db.addDB(Cabinet("Г-323", 71))
            db.addDB(Cabinet("Г-324", 74))
            db.addDB(Cabinet("Г-325", 86))
            db.addDB(Cabinet("Г-326", 75))
            db.addDB(Cabinet("Г-327", 84))
            db.addDB(Cabinet("Г-328", 82))
            db.addDB(Cabinet("Г-330", 77))
            db.addDB(Cabinet("Г-331", 79))
            db.addDB(Cabinet("Г-332", 97))
            db.addDB(Cabinet("Г-333", 133))
            db.addDB(Cabinet("Г-337", 101))
            db.addDB(Cabinet("Г-338", 103))
            db.addDB(Cabinet("Г-339", 105))
            db.addDB(Cabinet("Г-340", 107))
            db.addDB(Cabinet("Г-341", 110))
            db.addDB(Cabinet("Г-341а", 109))
            db.addDB(Cabinet("Г-342", 113))
            db.addDB(Cabinet("Г-343", 114))
            db.addDB(Cabinet("Г-344", 115))
            db.addDB(Cabinet("Г-345", 117))
            db.addDB(Cabinet("Лестница восток", 130))
            db.addDB(Cabinet("Лестница запад", 7))
            db.addDB(Cabinet("Лестница центр", 33))
            db.addDB(Cabinet("Туалет восток", 99))
            db.addDB(Cabinet("Туалет запад", 10))
            db.addDB(Cabinet("Холл", 37))
        }

        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
    }

}
