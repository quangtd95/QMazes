package com.quangtd.qmazes.game

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
abstract class Sprite {
    abstract fun update()
    abstract fun draw(canvas: Canvas, paint: Paint)

    fun calcDistance(p1: PointF, p2: PointF): Float {
        return Math.sqrt(Math.pow(((p2.x - p1.x).toDouble()), 2.0) + Math.pow(((p2.y - p1.y).toDouble()), 2.0)).toFloat()
    }
}