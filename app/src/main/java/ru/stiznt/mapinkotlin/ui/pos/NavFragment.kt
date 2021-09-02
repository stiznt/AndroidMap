package ru.stiznt.mapinkotlin.ui.pos

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.stiznt.mapinkotlin.R

class NavFragment : Fragment() {

    private var root: View? = null
    private var linearLayout: LinearLayout? = null
    //private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_nav, container, false)
        linearLayout = root!!.findViewById<View>(R.id.navLayOut) as LinearLayout
        //progressBar = root!!.findViewById<View>(R.id.progress) as ProgressBar
        setVisibility()

        return root
    }

    fun setVisibility() {
        if (loadText()) {
            linearLayout!!.visibility = View.VISIBLE
            //root!!.visibility = View.VISIBLE
        } else {
            linearLayout!!.visibility = View.INVISIBLE
            //root!!.visibility = View.INVISIBLE
        }
    }

    fun loadText(): Boolean {
        val sPref: SharedPreferences
        sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val position = root!!.findViewById<View>(R.id.position) as TextView
        position.text = sPref.getString("position", "")
        return sPref.getBoolean("visibility", false)
    }
}
