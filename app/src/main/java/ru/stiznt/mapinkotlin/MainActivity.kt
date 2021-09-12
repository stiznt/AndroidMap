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
            db.addDB(Cabinet("Г-301", 0))
            db.addDB(Cabinet("Г-302", 8))
            db.addDB(Cabinet("Г-303", 4))
            db.addDB(Cabinet("Г-304", 6))
            db.addDB(Cabinet("Г-305", 15))
            db.addDB(Cabinet("Г-307", 13))
            db.addDB(Cabinet("Г-309", 17))
            db.addDB(Cabinet("Г-311", 44))
            db.addDB(Cabinet("Г-312", 45))
            db.addDB(Cabinet("Г-313", 46))
            db.addDB(Cabinet("Г-314", 29))
            db.addDB(Cabinet("Г-315", 30))
            db.addDB(Cabinet("Г-316", 31))
            db.addDB(Cabinet("Г-317", 47))
            db.addDB(Cabinet("Г-318", 48))
            db.addDB(Cabinet("Г-319", 63))
            db.addDB(Cabinet("Г-320", 32))
            db.addDB(Cabinet("Г-321", 68))
            db.addDB(Cabinet("Г-322", 69))
            db.addDB(Cabinet("Г-323", 72))
            db.addDB(Cabinet("Г-324", 73))
            db.addDB(Cabinet("Г-325", 85))
            db.addDB(Cabinet("Г-326", 76))
            db.addDB(Cabinet("Г-327", 83))
            db.addDB(Cabinet("Г-328", 81))
            db.addDB(Cabinet("Г-330", 78))
            db.addDB(Cabinet("Г-331", 80))
            db.addDB(Cabinet("Г-332", 98))
            db.addDB(Cabinet("Г-333", 120))
            db.addDB(Cabinet("Г-337", 102))
            db.addDB(Cabinet("Г-338", 104))
            db.addDB(Cabinet("Г-339", 106))
            db.addDB(Cabinet("Г-340", 108))
            db.addDB(Cabinet("Г-341", 111))
            db.addDB(Cabinet("Г-341а", 128))
            db.addDB(Cabinet("Г-342", 129))
            db.addDB(Cabinet("Г-343", 134))
            db.addDB(Cabinet("Г-344", 116))
            db.addDB(Cabinet("Г-345", 118))
            db.addDB(Cabinet("Лестница восток", 130))
            db.addDB(Cabinet("Лестница запад", 7))
            db.addDB(Cabinet("Лестница центр", 33))
            db.addDB(Cabinet("Туалет восток", 100))
            db.addDB(Cabinet("Туалет запад", 11))
            db.addDB(Cabinet("Холл", 37))
        }

        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
    }

}
