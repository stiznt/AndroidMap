package ru.stiznt.mapinkotlin.navigation

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer
import org.json.JSONTokener

class Map {
    val dots = ArrayList<Dot>()
    val map : JSONObject ?= null

    fun loadFromString(json : String){
        val map = JSONTokener(json).nextValue() as JSONObject
        val jsonDots = map.getJSONArray("dots")

        var i = 0
        while(i++ < jsonDots.length()){
            var jsonDot = jsonDots.getJSONObject(i)
            var id = jsonDot.getInt("id")
            var count = jsonDot.getJSONArray("connected").length()
            Log.d("test", "id:${id} nei_count: ${count}")
        }

    }


    class Dot{
        private var x = 0f
        private var y = 0f
        private var id = 0
        private var nei = ArrayList<Int>()

        fun setPos(x: Float, y: Float){
            this.x = x;
            this.y = y;
        }

        fun setId(id : Int){
            this.id = id;
        }

        constructor(x: Float, y: Float) {
            this.x = x;
            this.y = y;
        }
    }
}