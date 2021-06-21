package ru.stiznt.mapinkotlin


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.ReferentialData
import ovh.plrapps.mapview.ReferentialListener
import ovh.plrapps.mapview.api.setAngle
import ovh.plrapps.mapview.core.TileStreamProvider
import java.io.InputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var newScale = 0f
    private var levelCount = 2
    private var maxScale = 2f

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

        val config = MapViewConfiguration(levelCount,1980,1080,1024,tileStreamProvider).setMaxScale(maxScale).setStartScale(0f).enableRotation()
        mapView.configure(config)

        val zoomInButton = findViewById<Button>(R.id.button_zoom_in)
        val zoomOutButton = findViewById<Button>(R.id.button_zoom_out)
        val compassButton = findViewById<ImageButton>(R.id.button_compass)

        val refOwner = object : ReferentialListener {
            override fun onReferentialChanged(refData: ReferentialData) {
                compassButton.rotation = refData.angle - 45f
            }
        }

        mapView.addReferentialListener(refOwner)

        Log.d("kek",  compassButton.rotation.toString())

        zoomInButton.setOnClickListener{
            newScale += maxScale/levelCount
            if(newScale > maxScale) newScale = maxScale
            mapView.setScaleFromCenter(newScale)
        }

        zoomOutButton.setOnClickListener{
            newScale -= maxScale/levelCount
            if(newScale < 0) newScale = 0f
            mapView.setScaleFromCenter(newScale)
        }
        compassButton.setOnClickListener{
            mapView.setAngle(0f)
        }

    }
}
