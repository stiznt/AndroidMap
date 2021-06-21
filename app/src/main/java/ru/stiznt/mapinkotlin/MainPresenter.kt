package ru.stiznt.mapinkotlin

import android.view.View
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.ReferentialData
import ovh.plrapps.mapview.ReferentialListener
import ovh.plrapps.mapview.core.TileStreamProvider
import java.io.InputStream

class MainPresenter(activity: MainActivity) : View.OnClickListener, ReferentialListener, TileStreamProvider {

    private val activity : MainActivity
    private var refData : ReferentialData ?= null

    private var newScale = 0f
    private var levelCount = 2
    private var maxScale = 2f

    init {
       this.activity = activity
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
    }

    //zoomIn button click logic
    private fun zoomIn(){
        newScale += maxScale/levelCount
        if(newScale > maxScale) newScale = maxScale
        activity.setScale(newScale)
    }

    //zoomOut button click logic
    private fun zoomOut(){
        newScale -= maxScale/levelCount
        if(newScale < 0) newScale = 0f
        activity.setScale(newScale)
    }

    //generate MapViewConfiguration and set some properties
    fun generateConfig() : MapViewConfiguration{
        return MapViewConfiguration(levelCount, 1980,1080,1024,this).setMaxScale(maxScale).enableRotation().setStartScale(0f)
    }

    //get tile from it's row, col and zoomLvl
    override fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
        return activity.assets?.open("tiles/$zoomLvl/$row/$col.jpg")
    }
}