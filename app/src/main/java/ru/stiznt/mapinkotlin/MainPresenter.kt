package ru.stiznt.mapinkotlin

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.ReferentialData
import ovh.plrapps.mapview.ReferentialListener
import ovh.plrapps.mapview.core.TileStreamProvider
import ovh.plrapps.mapview.paths.PathPoint
import ovh.plrapps.mapview.paths.PathView
import ovh.plrapps.mapview.paths.toFloatArray
import ru.stiznt.mapinkotlin.navigation.Navigation
import java.io.InputStream
import java.lang.Exception

class MainPresenter(activity: MainActivity) : View.OnClickListener, ReferentialListener, TileStreamProvider {

    private val activity : MainActivity
    private var refData : ReferentialData ?= null

    private var newScale = 0f
    private var levelCount = 4
    private var maxScale = 4f
    private var nav : Navigation
    private var Path : FloatArray ?= null
    private var p = Paint()
    private var widthMax = 50f
    private var widthMin = 10f

    init {
        this.activity = activity
        nav = Navigation()
        var json = activity.assets?.open("map.json")?.reader().use { it?.readText() }
        nav.loadMapFromJson(json!!)
        p.color = Color.GREEN
        p.strokeCap = Paint.Cap.ROUND
    }

    //When MapView is change, this method set changed parameters
    override fun onReferentialChanged(refData: ReferentialData) {
        this.refData = refData
        activity.rotateCompass(refData.angle)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_compass -> mapCentre()
            R.id.button_zoom_out -> zoomOut()
            R.id.button_zoom_in -> zoomIn()
        }
    }

    //compass button click logic
    private fun mapCentre(){
        activity.rotate(0f)
        var kek = nav.path(2, 79)
        Path = kek
        if(kek != null){
            var drawablePath = object : PathView.DrawablePath {
                override val visible: Boolean = true
                override var path: FloatArray = kek
                override var paint: Paint? = p
                override val width: Float? = widthMin + (widthMax-widthMin)/maxScale*newScale
            }
            activity.updatePaths(drawablePath)
        }else{
            activity.showText("Can't find path");
        }
    }

    //zoomIn button click logic
    private fun zoomIn(){
        newScale += maxScale/levelCount
        if(newScale > maxScale) newScale = maxScale
        activity.setScale(newScale)
        if(Path != null){
            var drawablePath = object : PathView.DrawablePath {
                override val visible: Boolean = true
                override var path: FloatArray = Path as FloatArray
                override var paint: Paint? = p
                override val width: Float? = widthMin + (widthMax-widthMin)/maxScale*newScale
            }
            activity?.updatePaths(drawablePath)
        }
    }

    //zoomOut button click logic
    private fun zoomOut(){
        newScale -= maxScale/levelCount
        if(newScale < 0) newScale = 0f
        activity.setScale(newScale)
        if(Path != null){
            var drawablePath = object : PathView.DrawablePath {
                override val visible: Boolean = true
                override var path: FloatArray = Path as FloatArray
                override var paint: Paint? = p
                override val width: Float? = widthMin + (widthMax-widthMin)/maxScale*newScale
            }
            activity?.updatePaths(drawablePath)
        }
    }

    //generate MapViewConfiguration and set some properties
    fun generateConfig() : MapViewConfiguration{
        return MapViewConfiguration(levelCount, 1980,1080,256,this).setMaxScale(maxScale).enableRotation().setStartScale(0f)
    }

    //get tile from it's row, col and zoomLvl
    override fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
        try {
            return activity.assets?.open("tiles/$zoomLvl/$row/$col.jpg")
        }catch (e : Exception){
            return activity.assets?.open("tiles/blank.jpg")
        }
    }
}