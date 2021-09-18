package ru.stiznt.mapinkotlin

import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.ReferentialData
import ovh.plrapps.mapview.ReferentialListener
import ovh.plrapps.mapview.core.TileStreamProvider
import ovh.plrapps.mapview.paths.PathView
import ru.stiznt.mapinkotlin.navigation.Navigation
import ru.stiznt.mapinkotlin.ui.pos.PosFragment
import java.io.InputStream
import kotlin.math.abs
import kotlin.math.atan2

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
    private var angleDegree = 0f
    private var positionMarkerAngle = 0f

    private var start_dist = 1;//начальное расстояние
    private var fin = -1;// финишная точка
    private var cur_dist = 0;
    private var prev_dist = 0;
    private var status = true;

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
        angleDegree = refData.angle
        activity.rotateCompass(refData.angle)
        activity.rotateMarker(-positionMarkerAngle + refData.angle)
        if (realMinScale == 0f) realMinScale = refData.scale
        if (refData.scale != newScale) {
            newScale = refData.scale
            activity?.setPositionMarkerScale(newScale)
            activity?.setFinishMarkerScale(newScale)
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

    fun updatePath(start: Int, finish: Int) = try {
        var start1 = start
        if (start > 133)
            start1 = 33

        val sPref = activity?.requireActivity().getPreferences(MODE_PRIVATE)
        fin = sPref.getInt("fin", -1)
        start_dist = sPref.getInt("start_dist", 1)
        cur_dist = sPref.getInt("cur_dist", 0)

        if (fin == finish) {
            prev_dist = cur_dist
            cur_dist = nav.dist(start1, finish).toInt()
        } else {
            cur_dist = nav.dist(start1, finish).toInt()
            start_dist = nav.dist(start1, finish).toInt()
            prev_dist = start_dist
            fin = finish
        }

        if (cur_dist > prev_dist) {
            status = false;
        }


        var editor = sPref.edit()
        editor.putInt("fin", fin)
        editor.putInt("start_dist", start_dist)
        editor.putInt("cur_dist", cur_dist)
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

        val kek = sPref.getString("position", "")

        var dx: Double = 0.0
        var dy: Double = 0.0
        if (kek == "") {
            dx = Path!![0].toDouble()
            dy = Path!![1].toDouble()
            activity?.addPositionMarker(dx / 3840, dy / 2160)
        } else {
            var dx = Path!![Path.size - 2].toDouble()
            var dy = Path!![Path.size - 1].toDouble()
            activity?.addFinishMarker(dx / 3840, dy / 2160)

            dx = Path!![0].toDouble()
            dy = Path!![1].toDouble()
            activity?.addPositionMarker(dx / 3840, dy / 2160)
            positionMarkerAngle =
                Math.toDegrees(atan2(dx.toFloat() - Path!![2], dy.toFloat() - Path!![3]).toDouble())
                    .toFloat()
            activity?.rotateMarker(-positionMarkerAngle + angleDegree)
        }

    } catch (ex: Exception) {
    }


    fun ProgressValue(): Int {
        var tmp = (100 - (cur_dist * 100 / start_dist))
        if (tmp > 0)
            return tmp
        return 0
    }

    fun getDistMetr(): Int {
        return start_dist / 23
    }

    fun getCurDist(): Int {
        Log.i("init", cur_dist.toString())
        return cur_dist / 23
    }

    fun getStatus(): Boolean {
        return status
    }
}