package ru.stiznt.mapinkotlin.navigation

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.sqrt

class Map {
    private val dots = ArrayList<Dot>()
    private var width = 0
    private var height = 0

    fun loadFromString(json : String){
        val map = JSONTokener(json).nextValue() as JSONObject
        val jsonDots = map.getJSONArray("dots")
        width = map.getInt("width")
        height = map.getInt("height")
        var i = -1
        while(++i < jsonDots.length()) {
            var jsonDot = jsonDots.getJSONObject(i)
            var dot = Dot(jsonDot.getDouble("x").toFloat(), jsonDot.getDouble("y").toFloat())
            dot.setId(jsonDot.getInt("id"))
            dot.setConnected(jsonDot.getJSONArray("connected"))
            dots.add(dot)
        }
    }

    fun getDot(id :Int) : Dot{
        return dots[id]
    }

    fun dist(dot1 :Int, dot2 :Int) : Float{
        if(dots.isEmpty()) return -1f
        if(dot1 < 0 || dot2 < 0) return -1f
        if(dot1 > dots.size || dot2 > dots.size) return -1f
        var dX1 = width*dots.get(dot1).getX()
        var dX2 = width*dots.get(dot2).getX()
        var dY1 = height*dots.get(dot1).getY()
        var dY2 = height*dots.get(dot2).getY()

        return sqrt((dX1-dX2)*(dX1-dX2) + (dY1-dY2)*(dY1-dY2))
    }

    class Dot{
        private var x = 0f
        private var y = 0f
        private var id = 0
        private var g = 0f
        private var h = 0f
        private var visited = false
        private var fromId = -1
        private var nei = ArrayList<Int>()

        fun setPos(x: Float, y: Float){
            this.x = x;
            this.y = y;
        }

        fun setG(g : Float){this.g = g}
        fun getG(): Float{return g}

        fun setH(h : Float){this.h = h}
        fun getH(): Float{return h}

        fun setFromId(id : Int){fromId = id}
        fun getFromId() : Int{return fromId}

        fun setVisited(x : Boolean){visited = x}
        fun isVisited():Boolean{return visited}
        fun setId(id : Int){
            this.id = id;
        }
        fun getId() : Int {return id}
        fun getX() : Float{
            return x
        }

        fun getF() : Float {return g+h}

        fun getY() :Float{
            return y
        }

        fun setX(x : Float){ this.x = x}
        fun setY(y : Float){ this.y = y}

        fun setConnected(nei : JSONArray){
            var i = -1
            while(++i < nei.length()){
                this.nei.add(nei.getInt(i));
            }
        }
        fun getConnected():ArrayList<Int>{return nei}

        constructor(x: Float, y: Float) {
            this.x = x;
            this.y = y;
        }
    }
}