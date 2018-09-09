package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class Wall(var o: Point = Point(0, 0), var d: Point = Point(0, 0)) : Sprite() {
    var widthCell: Float = 0F
    var widthWall: Float = 0F

    override fun update() {
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.strokeWidth = widthWall
        canvas.drawLine(widthCell * o.x, widthCell * o.y, widthCell * d.x, widthCell * d.y, paint)
    }

    private var rect = RectF()
    fun getRect(): RectF {
        rect.set(o.x * widthCell - widthWall / 2, o.y * widthCell - widthWall / 2, d.x * widthCell + widthWall / 2, d.y * widthCell + widthWall / 2)
        return rect
    }

    override fun equals(other: Any?): Boolean {

        return when (other) {
            is Wall -> {
                val check =
                        (
                                other.o.x >= o.x && other.o.x <= d.x
                                        && other.d.x >= o.x && other.d.x <= d.x
                                        && other.o.y >= o.y && other.o.y <= d.y
                                        && other.d.y >= o.y && other.d.y <= d.y
                                ) || (
                                other.o.x >= d.x && other.o.x <= o.x
                                        && other.d.x >= d.x && other.d.x <= o.x
                                        && other.o.y >= d.y && other.o.y <= o.y
                                        && other.d.y >= d.y && other.d.y <= o.y
                                )
                if (check) {
                    println(check)
                }
                return check
//                (o == other.o && d == other.d) || (o == other.d && d == other.o)
            }
            null -> false
            else -> false
        }
    }
}


