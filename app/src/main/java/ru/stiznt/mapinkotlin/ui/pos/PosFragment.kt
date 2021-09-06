package ru.stiznt.mapinkotlin.ui.pos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.stiznt.mapinkotlin.MainPresenter
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.api.setAngle
import ovh.plrapps.mapview.paths.PathView
import ovh.plrapps.mapview.paths.addPathView
import ru.stiznt.mapinkotlin.Models.Cabinet
import ru.stiznt.mapinkotlin.R

class PosFragment : Fragment() {

    private var root: View? = null
    private var mapView: MapView? = null
    private var zoomInButton: ImageView? = null
    private var zoomOutButton: ImageView? = null
    private var compassButton: ImageButton? = null
    private var navHelper: LinearLayout? = null
    private var presenter: MainPresenter? = null
    private var pathView: PathView? = null

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
        //set configuration to mapView
        mapView?.configure(presenter!!.generateConfig())
        //set coordinates maxmimum and minimum
        //mapView?.defineBounds(0.0,0.0,1.0,1.0)
        //navHelper?.visibility = View.INVISIBLE
        //pathView init
        pathView = PathView(mapView!!.context)
        mapView?.addPathView(pathView!!)

        //add referentialListener to mapView
        mapView?.addReferentialListener(presenter!!)

        //set clickListener to buttons
        compassButton?.setOnClickListener(presenter)
        zoomInButton?.setOnClickListener(presenter)
        zoomOutButton?.setOnClickListener(presenter)

        showNavigation()
        return root
    }

    fun showNavigation(){
        val sPref: SharedPreferences
        sPref = requireActivity().getPreferences(MODE_PRIVATE)
        val position = root!!.findViewById<View>(R.id.position) as TextView
        var pos = sPref.getString("position", "")
        if(pos?.length!! > 2){
            position.text = pos
            navHelper?.visibility = View.VISIBLE
            presenter?.updatePath(true)
            saveState()
        }else navHelper?.visibility = View.INVISIBLE
    }

    fun saveState() {
        val sPref:SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putString("position", "")
        ed?.apply()
    }

    /*Rotate map to @angle*/
    fun rotate(angle : Float){
        mapView?.setAngle(angle)
        rotateCompass(angle)
    }
    // Rotate compassButton to @angel
    fun rotateCompass(angle: Float){
        compassButton?.rotation = angle
    }
    // Scale mapView to @scale. Value is in range 0 - 1
    fun setScale(scale : Float){
        mapView?.setScaleFromCenter(scale)
    }

    fun updatePaths(path : PathView.DrawablePath){
        pathView?.updatePaths(listOf(path))
    }

}
