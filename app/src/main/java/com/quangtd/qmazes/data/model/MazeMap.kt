package com.quangtd.qmazes.data.model

import com.google.gson.annotations.SerializedName
import com.quangtd.qmazes.mvpbase.BaseModel

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
data class MazeMap(
        // number of r
        var r: Int,
        // number of c
        var c: Int,
        //start
        var s: Point,
        //finish
        var f: Point,
        //hint
        var h: List<Point>,
        //wall
        var w: List<Wall>,
        var i: List<Ice>,
        var e: List<Point>,
        var t: List<Point>
) : BaseModel