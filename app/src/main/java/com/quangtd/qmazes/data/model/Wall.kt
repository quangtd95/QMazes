package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint
import com.quangtd.qmazes.game.Sprite

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class Wall(var o: Point = Point(0, 0), var d: Point = Point(0, 0)) : Sprite() {
    var widthCell: Float = 0F

    override fun update() {
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawLine(widthCell * o.x, widthCell * o.y, widthCell * d.x, widthCell * d.y, paint)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Wall -> (o == other.o && d == other.d) || (o == other.d && d == other.o)
            null -> false
            else -> false
        }
    }
}


