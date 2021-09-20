package ru.stiznt.mapinkotlin.ui.pos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import ru.stiznt.mapinkotlin.MainPresenter
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.api.*
import ovh.plrapps.mapview.paths.*
import ru.stiznt.mapinkotlin.Models.Cabinet
import ru.stiznt.mapinkotlin.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PosFragment : Fragment() {

    private var root: View? = null
    private var mapView: MapView? = null
    private var zoomInButton: ImageView? = null
    private var zoomOutButton: ImageView? = null
    private var compassButton: ImageButton? = null

    private var navHelper: LinearLayout? = null
    private var reset_path: ImageView? = null
    private var dist_progress: TextView? = null
    private var remained_path: TextView? = null
    private var dist_time: TextView? = null
    private var dist_min: TextView? = null
    private var status: LinearLayout? = null

    private var progressBar: ProgressBar? = null
    private var progressView: View? = null
    private var presenter: MainPresenter? = null
    private var pathView: PathView? = null

    private var finishMarker: AppCompatImageView? = null
    private var positionMarker: AppCompatImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pos, container, false)
        //presenter init
        presenter = MainPresenter(this)
        mapView = root?.findViewById<MapView>(R.id.mapView)
        zoomInButton = root?.findViewById<ImageView>(R.id.button_zoom_in)
        zoomOutButton = root?.findViewById<ImageView>(R.id.button_zoom_out)
        compassButton = root?.findViewById<ImageButton>(R.id.button_compass)

        navHelper = root?.findViewById<LinearLayout>(R.id.navLayOut)
        reset_path = root?.findViewById<ImageButton>(R.id.button_reset)
        dist_progress = root?.findViewById<TextView>(R.id.dist_progress)
        dist_time = root?.findViewById<TextView>(R.id.dist_time)
        dist_min = root?.findViewById<TextView>(R.id.dist_min)
        remained_path = root?.findViewById<TextView>(R.id.remained_path)
        status = root?.findViewById<LinearLayout>(R.id.status)//Вы ушли с маршрута
        //set configuration to mapView
        mapView?.configure(presenter!!.generateConfig())
        //set coordinates maxmimum and minimum
        mapView?.defineBounds(0.0, 0.0, 1.0, 1.0)
        //navHelper?.visibility = View.INVISIBLE
        //pathView init
        pathView = PathView(mapView!!.context)
        mapView?.addPathView(pathView!!)

        //add referentialListener to mapView
        mapView?.addReferentialListener(presenter!!)
        //bar
        progressBar = root?.findViewById<ProgressBar>(ru.stiznt.mapinkotlin.R.id.progress)
        progressView = root?.findViewById<View>(ru.stiznt.mapinkotlin.R.id.progressView)
        //set clickListener to buttons
        compassButton?.setOnClickListener(presenter)
        zoomInButton?.setOnClickListener(presenter)
        zoomOutButton?.setOnClickListener(presenter)

        reset_path?.setOnClickListener(View.OnClickListener {
            reset_path?.visibility = View.INVISIBLE
            navHelper?.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
            progressView?.visibility = View.INVISIBLE
            dist_progress?.visibility = View.INVISIBLE
            dist_time?.visibility = View.INVISIBLE
            dist_min?.visibility = View.INVISIBLE
            status?.visibility = View.INVISIBLE
            saveState()
            deletePaths()
        })

        finishMarker = AppCompatImageView(this.requireContext()).apply {
            setImageResource(R.drawable.finish_marker_centered)
        }

        positionMarker = AppCompatImageView(this.requireContext()).apply {
            setImageResource(R.drawable.position_marker)
        }

        showNavigation()
        return root
    }

    //TODO:Добавить базу для BLE

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNavigation() {
        val sPref: SharedPreferences
        sPref = requireActivity().getPreferences(MODE_PRIVATE)

        if (sPref.getInt("MY_POS", 33) == sPref.getInt("FINISH", 1)) {
            Toast.makeText(context, "Вы пришли", Toast.LENGTH_LONG).show()
            navHelper?.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
            progressView?.visibility = View.INVISIBLE
            dist_progress?.visibility = View.INVISIBLE
            dist_time?.visibility = View.INVISIBLE
            dist_min?.visibility = View.INVISIBLE
            reset_path?.visibility = View.INVISIBLE
            status?.visibility = View.INVISIBLE
            saveState()
            presenter?.showMarkerInPos(sPref.getInt("MY_POS", 33))
            deletePaths()
            saveState()
        }
        val position = root!!.findViewById<View>(R.id.position) as TextView
        var pos = sPref.getString("position", "")

        if (pos?.length!! > 2) {
            progressView?.visibility = View.VISIBLE
            progressBar?.visibility = View.VISIBLE

            position.text = pos
            navHelper?.visibility = View.VISIBLE
            dist_progress?.visibility = View.VISIBLE
            dist_time?.visibility = View.VISIBLE
            dist_min?.visibility = View.VISIBLE
            reset_path?.visibility = View.VISIBLE

            presenter?.updatePath(sPref.getInt("MY_POS", 33), sPref.getInt("FINISH", 1))

            var travel_time = presenter?.getCurDist()?.div(67) //67 метров в минуту
            var time1: LocalTime? = travel_time?.toLong()?.let { LocalTime.now().plusMinutes(it) }

            dist_time?.text = time1?.format(DateTimeFormatter.ofPattern("HH:mm"))
            if (travel_time != null) {
                if (travel_time < 1)
                    dist_min?.text = "<1мин"
                else
                    dist_min?.text = travel_time.toString() + " мин"
            }

            var tmp = presenter?.ProgressValue()
            if (tmp != null) {
                progressBar?.setProgress(tmp)
                dist_progress?.text = presenter?.getDistMetr().toString() + "м"
                remained_path?.text = presenter?.getCurDist().toString() + "м"
            }
            if (presenter?.getStatus() == false)
                status?.visibility = View.VISIBLE
            else
                status?.visibility = View.INVISIBLE

        } else {
            navHelper?.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
            progressView?.visibility = View.INVISIBLE
            dist_progress?.visibility = View.INVISIBLE
            dist_time?.visibility = View.INVISIBLE
            dist_min?.visibility = View.INVISIBLE
            reset_path?.visibility = View.INVISIBLE
            status?.visibility = View.INVISIBLE
            deletePaths()
        }
    }

    fun saveState() {
        val sPref: SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putString("position", "")
        ed?.apply()
    }

    /*Rotate map to @angle*/
    fun rotate(angle: Float) {
        mapView?.setAngle(angle)
        rotateCompass(angle)
    }

    fun rotateMarker(angle: Float) {
        positionMarker?.rotation = angle;
    }

    // Rotate compassButton to @angel
    fun rotateCompass(angle: Float) {
        compassButton?.rotation = angle
    }

    // Scale mapView to @scale. Value is in range 0 - 1
    fun setScale(scale: Float) {
        mapView?.setScaleFromCenter(scale)
        setPositionMarkerScale(scale)
        setFinishMarkerScale(scale)
    }

    fun setFinishMarkerScale(scale: Float) {
        finishMarker?.scaleX = scale + 0.5f
        finishMarker?.scaleY = scale + 0.5f
    }

    fun moveTo(){
        //mapView?.moveToMarker(positionMarker!!, true)
        mapView?.moveToMarker(positionMarker!!, true)
    }

    fun setPositionMarkerScale(scale: Float) {
        positionMarker?.scaleX = scale + 1f
        positionMarker?.scaleY = scale + 1f
    }

    fun updatePaths(path: PathView.DrawablePath) {
        pathView?.updatePaths(listOf(path))
    }

    fun addFinishMarker(x: Double, y: Double) {
        mapView?.addMarker(finishMarker!!, x, y, -0.5f, -0.5f)
    }

    fun addPositionMarker(x: Double, y: Double) {

        mapView?.addMarker(positionMarker!!, x, y, -0.5f, -0.5f)
    }

    fun deletePaths() {
        mapView?.removeMarker(finishMarker!!)
        //mapView?.removeMarker(positionMarker!!)
        mapView?.removePathView(pathView!!)
        pathView = PathView(mapView!!.context)
    }


}