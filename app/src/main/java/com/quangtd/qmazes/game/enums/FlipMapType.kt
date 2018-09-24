package com.quangtd.qmazes.game.enums

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by quang.td95@gmail.com
 * on 9/5/2018.
 */
enum class FlipMapType {
    @SerializedName("flip_h")
    FLIP_H,
    @SerializedName("flip_v")
    FLIP_V,
    @SerializedName("flip_2_w")
    FLIP_2_W,
    @SerializedName("none")
    NONE;

    companion object {
        fun getRandomType(): FlipMapType {
//            return FLIP_H
            return values()[Random().nextInt(values().size)]
        }
    }
}