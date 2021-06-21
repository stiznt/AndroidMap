package ru.stiznt.mapinkotlin


import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.api.setAngle


class MainActivity : AppCompatActivity() {

    private var mapView : MapView ?= null
    private var zoomInButton  : Button ?= null
    private var zoomOutButton : Button ?= null
    private var compassButton : ImageButton ?= null
    private var presenter : MainPresenter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //presenter init
        presenter = MainPresenter(this)

        //activity components init
        mapView = findViewById<MapView>(R.id.mapView)
        zoomInButton = findViewById<Button>(R.id.button_zoom_in)
        zoomOutButton = findViewById<Button>(R.id.button_zoom_out)
        compassButton = findViewById<ImageButton>(R.id.button_compass)

        //set configuration to mapView
        mapView?.configure(presenter!!.generateConfig())

        //add referentialListener to mapView
        mapView?.addReferentialListener(presenter!!)

        //set clickListener to buttons
        compassButton?.setOnClickListener(presenter)
        zoomInButton?.setOnClickListener(presenter)
        zoomOutButton?.setOnClickListener(presenter)
    }

    /*Rotate map to @angle*/
    fun rotate(angle : Float){
        mapView?.setAngle(angle)
        rotateCompass(angle)
    }
    // Rotate compassButton to @angel
    fun rotateCompass(angle: Float){
        compassButton?.rotation = angle - 45f
    }
    // Scale mapView to @scale. Value is in range 0 - 1
    fun setScale(scale : Float){
        mapView?.setScaleFromCenter(scale)
    }
}
