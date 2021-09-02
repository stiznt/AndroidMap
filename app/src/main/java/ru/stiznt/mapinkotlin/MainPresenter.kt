package ru.stiznt.mapinkotlin

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
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
import ru.stiznt.mapinkotlin.ui.pos.PosFragment
import java.io.InputStream
import java.lang.Exception

class MainPresenter(activity: PosFragment) : View.OnClickListener, ReferentialListener, TileStreamProvider {

    private val activity : PosFragment
    private var refData : ReferentialData ?= null

    private var newScale = 0f
    private var showPath = false;
    private var levelCount = 4
    private var maxScale = 4f
    private var minScale = 0.5f
    private var nav : Navigation
    private var p = Paint()
    private var widthMax = 60f
    private var widthMin = 10f

    init {
        this.activity = activity
        nav = Navigation()
        var json = activity.activity?.assets?.open("map.json")?.reader().use { it?.readText() }
        nav.loadMapFromJson(json!!)
        p.color = Color.GREEN
        p.strokeCap = Paint.Cap.ROUND
    }

    //When MapView is change, this method set changed parameters
    override fun onReferentialChanged(refData: ReferentialData) {
        this.refData = refData
        activity.rotateCompass(refData.angle)
        if(minScale == null) minScale = refData.scale
        if(refData.scale != newScale){
            newScale = refData.scale
            updatePath()
        }

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
        showPath = true;
        updatePath()
    }

    //zoomIn button click logic
    private fun zoomIn(){
        newScale += maxScale/levelCount
        if(newScale > maxScale) newScale = maxScale
        activity.setScale(newScale)
        updatePath()
    }

    //zoomOut button click logic
    private fun zoomOut(){
        newScale -= maxScale/levelCount
        if(newScale < minScale!!) newScale = minScale!!
        activity.setScale(newScale)
        updatePath()
    }

    //generate MapViewConfiguration and set some properties
    fun generateConfig() : MapViewConfiguration{
        return MapViewConfiguration(levelCount, 1980,1080,256,this).setMaxScale(maxScale).enableRotation().setStartScale(0f)
    }

    //get tile from it's row, col and zoomLvl
    override fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
        try {
            return activity.activity?.assets?.open("tiles/$zoomLvl/$row/$col.jpg")
        }catch (e : Exception){
            return activity.activity?.assets?.open("tiles/blank.jpg")
        }
    }

    private fun updatePath(){
        if(!showPath) return
        var Path = nav.path(2, 79)
        var drawablePath = object : PathView.DrawablePath {
            override val visible: Boolean = true
            override var path: FloatArray = Path as FloatArray
            override var paint: Paint? = p
            override val width: Float? = widthMin + (widthMax-widthMin)*newScale/maxScale
        }
        activity?.updatePaths(drawablePath)
    }
}