package ru.stiznt.mapinkotlin.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import ru.stiznt.mapinkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.stiznt.mapinkotlin.Adapter.HistoryAdapter
import ru.stiznt.mapinkotlin.DataBase.officeDB.DBConnector
import ru.stiznt.mapinkotlin.Models.Cabinet
import java.util.*
import kotlin.collections.ArrayList

//TODO: Поднимать элемент списка вверх при его повторном упоминании.

class SearchFragment : Fragment() {

    private var sPref: SharedPreferences? = null
    private var pos = ArrayList<Cabinet>()
    private var historyAdapter: HistoryAdapter? = null
    private var listView: ListView? = null
    private var dbConnector: ru.stiznt.mapinkotlin.DataBase.searchDB.DBConnector? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root: View = inflater.inflate(R.layout.fragment_search, container, false)
        retainInstance = true
        dbConnector = ru.stiznt.mapinkotlin.DataBase.searchDB.DBConnector(context)
        listView = root.findViewById<View>(R.id.listStory) as ListView
        val search = root.findViewById<View>(R.id.etSearch) as EditText

        val db : DBConnector
        db = DBConnector(context)
        pos = db.readDB()
        onShowStory()

        listView!!.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                saveState(listView!!.getItemAtPosition(position) as Cabinet)
                dbConnector!!.addDB(listView!!.getItemAtPosition(position) as Cabinet)

                val bottomNavigationView =
                    activity?.findViewById<View>(R.id.nav_view) as BottomNavigationView
                bottomNavigationView.selectedItemId = bottomNavigationView.menu.getItem(1).itemId
            }
        /*search.setOnKeyListener { v, keyCode, event ->
            var consumed = false
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                dbConnector!!.addDB(search.text.toString(), 3)
                consumed = true
            }
            consumed
        }*/
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val command = search.text.toString()
                if (command != "") {
                    val filter = ArrayList<Cabinet>()
                    for (i in pos.indices) {
                        if (pos[i].name.toLowerCase().contains(command.toLowerCase())) filter.add(pos[i])
                    }
                    historyAdapter = HistoryAdapter(context, R.layout.list_item, filter)
                    listView!!.adapter = historyAdapter
                } else onShowStory()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return root
    }

    fun saveState(position: Cabinet) {
        sPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putString("position", position.name)
        ed?.putInt("FINISH",position.id);
        ed?.apply()
    }

    fun onShowStory() {
        val tmp: ArrayList<Cabinet> = dbConnector!!.readDB()
        Collections.reverse(tmp)
        historyAdapter = HistoryAdapter(context, R.layout.list_item, tmp)
        listView!!.adapter = historyAdapter
    }
}