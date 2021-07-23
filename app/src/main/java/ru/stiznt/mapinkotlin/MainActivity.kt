package ru.stiznt.mapinkotlin


import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.api.setAngle
import ovh.plrapps.mapview.paths.PathPoint
import ovh.plrapps.mapview.paths.PathView
import ovh.plrapps.mapview.paths.addPathView
import ovh.plrapps.mapview.paths.toFloatArray


class MainActivity : AppCompatActivity() {

    private var mapView : MapView ?= null
    private var zoomInButton  : Button ?= null
    private var zoomOutButton : Button ?= null
    private var compassButton : ImageButton ?= null
    private var presenter : MainPresenter ?= null
    private var pathView : PathView?= null

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

        //set coordinates maxmimum and minimum
        //mapView?.defineBounds(0.0,0.0,1.0,1.0)

        //pathView init
        pathView = PathView(mapView!!.context)
        mapView?.addPathView(pathView!!)

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

    fun updatePaths(path : PathView.DrawablePath){
        pathView?.updatePaths(listOf(path))
    }

    fun showText(text : String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT)
    }
}
