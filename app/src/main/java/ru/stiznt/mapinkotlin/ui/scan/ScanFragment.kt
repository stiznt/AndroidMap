package ru.stiznt.mapinkotlin.ui.scan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.stiznt.mapinkotlin.R

class ScanFragment : Fragment() {
    val CODE = 3
    var root: View? = null
    var sPref: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_scan, container, false)
        val intent = Intent(context, QR_Scanner::class.java)
        startActivityForResult(intent, CODE)
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 3) {
            val link = data!!.getStringExtra("link")
            saveText(link)
        }
        val bottomNavigationView =
            requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView
        bottomNavigationView.selectedItemId = bottomNavigationView.menu.getItem(1).itemId
    }

    fun saveText(link: String?) {
        sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putInt("MY_POS", 47/*Integer.parseInt(link)*/)
        ed?.apply()
    }
}
