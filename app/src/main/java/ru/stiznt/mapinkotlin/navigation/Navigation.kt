package ru.stiznt.mapinkotlin.navigation

import java.util.*
import kotlin.collections.ArrayList

class Navigation {

    private lateinit var map : Map

    init{
        map = Map()
    }

    fun path(start : Int, finish :Int) : ArrayList<Int>? {
       var que = ArrayList<Map.Dot>()
        map.getDot(start).setG(0f)
        map.getDot(start).setH(map.dist(start, finish))

        que.add(map.getDot(start))

        while(!que.isEmpty()){
            que.sortBy { it.getF() }
            var x : Map.Dot = que[0]
            que.removeAt(0)
            if(x.getId() == finish){
                return reconstructPath(x.getId())
            }

            x.setVisited(true)
            for(y in x.getConnected()){
                if(map.getDot(y).isVisited()) continue

                var tentative_is_better = false

                var tentative_g_score = x.getG() + map.dist(x.getId(), y)
                if(!que.contains(map.getDot(y))){
                    tentative_is_better = true
                }else if(tentative_g_score < map.getDot(y).getG()){
                    tentative_is_better = true
                }

                if(tentative_is_better){
                    map.getDot(y).setG(tentative_g_score)
                    map.getDot(y).setH(map.dist(y, finish))
                    map.getDot(y).setFromId(x.getId())
                    que.add(map.getDot(y))
                }
            }
        }
        return null
    }

    fun reconstructPath(finish : Int) : ArrayList<Int>? {
        var path = ArrayList<Int>()
        path.add(finish)
        var from = map.getDot(finish).getFromId()
        while(from != -1){
            path.add(from)
            from = map.getDot(from).getFromId()
        }
        path.reverse()
        return path
    }

    fun loadMapFromJson(json : String){
        map.loadFromString(json)
    }

    fun toDotList(list : ArrayList<Int>) : ArrayList<Map.Dot>{
        var kek = ArrayList<Map.Dot>()
        for(i in list) kek.add(map.getDot(i))

        return kek
    }

}