package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class Ice(
        @SerializedName("x")
        var x: Float = 0F,
        @SerializedName("y")
        var y: Float = 0F) : Sprite() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(x * widthCell - radius, y * widthCell - radius,
                    x * widthCell + radius, y * widthCell + radius, 5F, 5F, paint)
        } else {
            canvas.drawRect(x * widthCell - radius, y * widthCell - radius,
                    x * widthCell + radius, y * widthCell + radius, paint)
        }
    }
}