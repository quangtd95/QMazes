package com.quangtd.qmazes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
data class Point(
        @SerializedName("x")
        var x: Int = 0,
        @SerializedName("y")
        var y: Int = 0)