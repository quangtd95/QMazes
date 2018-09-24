package com.quangtd.qmazes.data.model

import com.google.gson.annotations.SerializedName
import com.quangtd.qmazes.mvpbase.BaseModel

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
data class MazeMap(
        // number of r
        @SerializedName("r")
        var r: Int,
        // number of c
        @SerializedName("c")
        var c: Int,
        //start
        @SerializedName("s")
        var s: Point,
        //finish
        @SerializedName("f")
        var f: Point,
        //hint
        @SerializedName("h")
        var h: List<Point>,
        //wall
        @SerializedName("w")
        var w: List<Wall>,
        @SerializedName("i")
        var i: List<Ice>,
        @SerializedName("e")
        var e: List<Point>,
        @SerializedName("t")
        var t: List<Trap>,
        @Transient var lstEnemy: ArrayList<Enemy>
) : BaseModel