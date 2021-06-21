package ru.stiznt.mapinkotlin


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.core.TileStreamProvider
import java.io.InputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var newScale = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapView = findViewById<MapView>(R.id.mapView)
        val tileStreamProvider =  object : TileStreamProvider {
            override fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
                return try {
                    assets?.open("tiles/$zoomLvl/$row/$col.jpg")
                } catch (e: Exception) {
                    null
                }
            }
        }
        val config = MapViewConfiguration(2,1980,1080,1024,tileStreamProvider).setMaxScale(2f).setStartScale(0f)
        mapView.configure(config)

        val button = findViewById<Button>(R.id.scale_up)

        button.setOnClickListener {
            if(newScale == 2f) newScale = 0f
            newScale += 0.5f
            mapView.setScaleFromCenter(newScale)
        }

    }
}
