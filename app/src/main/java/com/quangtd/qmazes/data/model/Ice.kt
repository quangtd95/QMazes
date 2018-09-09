package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class Ice(var x: Float = 0F, var y: Float = 0F) : Sprite() {
    var widthCell: Float = 0F
    var radius: Float = 0F
    override

    fun update() {
        // do no-thing
    }


    override fun draw(canvas: Canvas, paint: Paint) {
        radius = widthCell / 2F
        draw(canvas, paint, radius)
    }

    fun draw(canvas: Canvas, paint: Paint, radius: Float) {
        canvas.drawRoundRect(x * widthCell - radius, y * widthCell - radius,
                x * widthCell + radius, y * widthCell + radius, 5F, 5F, paint)
    }
}