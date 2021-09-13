package ru.stiznt.mapinkotlin

import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.ReferentialData
import ovh.plrapps.mapview.ReferentialListener
import ovh.plrapps.mapview.core.TileStreamProvider
import ovh.plrapps.mapview.paths.PathView
import ru.stiznt.mapinkotlin.navigation.Navigation
import ru.stiznt.mapinkotlin.ui.pos.PosFragment
import java.io.InputStream

class MainPresenter(activity: PosFragment) : View.OnClickListener, ReferentialListener,
    TileStreamProvider {

    private val activity: PosFragment
    private var refData: ReferentialData? = null

    private var newScale = 0f
    private var showPath = false;
    private var levelCount = 5
    private var maxScale = 2f
    private var minScale = 0f//0.5f
    private var nav: Navigation
    private var p = Paint()
    private var widthMax = 60f
    private var widthMin = 2f//10f
    private var realMinScale = 0f

    private var start_dist = 1;//начальное расстояние
    private var fin = 1;// финишная точка
    private var cur_dist = 1;

    init {
        this.activity = activity
        nav = Navigation()
        var json = activity.activity?.assets?.open("map.json")?.reader().use { it?.readText() }
        nav.loadMapFromJson(json!!)
        p.color = Color.rgb(6, 82, 253)
        p.strokeCap = Paint.Cap.ROUND
    }

    //When MapView is change, this method set changed parameters
    override fun onReferentialChanged(refData: ReferentialData) {
        this.refData = refData
        activity.rotateCompass(refData.angle)
        if (realMinScale == 0f) realMinScale = refData.scale
        if (refData.scale != newScale) {
            newScale = refData.scale
            val sPref = activity?.requireActivity().getPreferences(MODE_PRIVATE)
            updatePath(sPref.getInt("MY_POS", 33), sPref.getInt("FINISH", 1))
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_compass -> mapCentre()
            R.id.button_zoom_out -> zoomOut()
            R.id.button_zoom_in -> zoomIn()
        }
    }

    //compass button click logic
    private fun mapCentre() {
        activity.rotate(0f)
    }

    //zoomIn button click logic
    private fun zoomIn() {
        val sPref = activity?.requireActivity().getPreferences(MODE_PRIVATE)
        newScale += (maxScale - minScale) / levelCount
        if (newScale > maxScale) newScale = maxScale
        activity.setScale(newScale)
        updatePath(sPref.getInt("MY_POS", 33), sPref.getInt("FINISH", 1))

    }

    //zoomOut button click logic
    private fun zoomOut() {
        val sPref = activity?.requireActivity().getPreferences(MODE_PRIVATE)
        newScale -= (maxScale - minScale) / levelCount
        if (newScale < realMinScale) newScale = realMinScale
        activity.setScale(newScale)
        Log.d("test", "" + newScale)
        updatePath(sPref.getInt("MY_POS", 33), sPref.getInt("FINISH", 1))
    }

    //generate MapViewConfiguration and set some properties
    fun generateConfig(): MapViewConfiguration {
        return MapViewConfiguration(levelCount, 3840, 2160, 256, this).setMaxScale(maxScale)
            .enableRotation().setStartScale(0f)
    }

    //get tile from it's row, col and zoomLvl
    override fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
        try {
            return activity.activity?.assets?.open("tiles/$zoomLvl/$row/$col.jpg")
        } catch (e: Exception) {
            return activity.activity?.assets?.open("tiles/blank.png")
        }
    }

    fun updatePath(start: Int, finish: Int) {
        try {
            var start1 = start
            if (start > 133)
                start1 = 33

            val sPref = activity?.requireActivity().getPreferences(MODE_PRIVATE)
            fin = sPref.getInt("fin", 1)
            start_dist = sPref.getInt("start_dist", 1)

            if (fin.toInt() == finish) {
                cur_dist = nav.dist(start1, finish).toInt()
            } else {
                cur_dist = nav.dist(start1, finish).toInt()
                start_dist = nav.dist(start1, finish).toInt()
                fin = finish
            }

            if (cur_dist > start_dist)
                start_dist = cur_dist

            var editor = sPref.edit()
            editor.putInt("fin", fin)
            editor.putInt("start_dist", start_dist)
            editor?.apply()


            var Path = nav.path(start1, finish)
            var temp = widthMin + (widthMax - widthMin) * newScale / maxScale
            if (newScale == minScale) temp = widthMin
            else if (newScale == maxScale) temp = widthMax
            var drawablePath = object : PathView.DrawablePath {
                override val visible: Boolean = true
                override var path: FloatArray = Path as FloatArray
                override var paint: Paint? = p
                override val width: Float? = temp
            }
            activity?.updatePaths(drawablePath)
        } catch (ex: Exception) {
        }
    }

    fun ProgressValue(): Int {
        return (100 - (cur_dist * 100 / start_dist))

    }

    fun getDistMetr(): Int {
        return start_dist / 52
    }

    fun getCurDist(): Int {
        return cur_dist / 52
    }
}