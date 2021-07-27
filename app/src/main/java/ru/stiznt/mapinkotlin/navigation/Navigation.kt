package ru.stiznt.mapinkotlin.navigation

import java.util.*
import kotlin.collections.ArrayList

class Navigation {

    private var map : Map
    private var Path : pathCash?
    init{
        map = Map()
        Path = null
    }

    fun path(start : Int, finish :Int) : FloatArray? {
        if(Path?.from == start && Path?.to == finish) return Path?.path
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

    fun reconstructPath(finish : Int) : FloatArray? {
        var path = ArrayList<Int>()

        path.add(finish)
        var from = map.getDot(finish).getFromId()
        while(from != -1){
            path.add(from)
            from = map.getDot(from).getFromId()
        }
        path.reverse()
        var init = true
        var size = path.size * 4 - 4
        var i = 0
        var dot = 0

        var lines = FloatArray(size)

        while(i < size){
            if(init){
                lines[i] = map.getDot(path[dot]).getX()
                lines[i+1] = map.getDot(path[dot]).getY()
                init = false
                i += 2
                dot++
            }else{
                lines[i] = map.getDot(path[dot]).getX()
                lines[i+1] = map.getDot(path[dot]).getY()
                if(i + 2 >= size) break
                lines[i + 2] = lines[i]
                lines[i + 3] = lines[i + 1]
                i += 4
                dot++
            }
        }
        Path = object: pathCash(){
            override var path = lines
            override var from = path[0]
            override var to = path[path.size-1]
        }
        return lines
    }

    fun loadMapFromJson(json : String){
        map.loadFromString(json)
    }

    fun toDotList(list : ArrayList<Int>) : ArrayList<Map.Dot>{
        var kek = ArrayList<Map.Dot>()
        for(i in list) kek.add(map.getDot(i))

        return kek
    }

    abstract class pathCash{
        abstract var path :FloatArray
        abstract var from :Int
        abstract var to : Int
    }
}