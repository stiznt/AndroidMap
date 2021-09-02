package ru.stiznt.mapinkotlin.ui.pos

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.stiznt.mapinkotlin.MainPresenter
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.api.setAngle
import ovh.plrapps.mapview.paths.PathView
import ovh.plrapps.mapview.paths.addPathView
import ru.stiznt.mapinkotlin.R


class PosFragment : Fragment() {

    private var root: View? = null
    private var mapView: MapView? = null
    private var zoomInButton: ImageView? = null
    private var zoomOutButton: ImageView? = null
    private var compassButton: ImageButton? = null
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
        return root
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

    fun scrollTo(x : Int, y : Int){
        mapView?.scrollTo(x, y)
    }
}
