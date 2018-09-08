package com.quangtd.qmazes.game

import java.util.*

/**
 * Created by quang.td95@gmail.com
 * on 9/5/2018.
 */
enum class FlipMapType {
    FLIP_H,
    FLIP_V,
    FLIP_2_W,
    NONE;

    companion object {
        fun getRandomType(): FlipMapType {
            return FLIP_H
//            return values()[Random().nextInt(values().size)]
        }
    }
}