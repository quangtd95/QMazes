package com.quangtd.qmazes.data.model

import android.graphics.*
import android.support.v4.math.MathUtils
import com.quangtd.qmazes.game.ResourceManager
import com.quangtd.qmazes.game.Sprite

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
data class Door(var x: Int = 0, var y: Int = 0, var player: Player) : Sprite() {
    var widthCell: Float = 0F
    var matrix = Matrix()
    private var centerPt = PointF()
    fun getCenterPointF(): PointF {
        centerPt.x = x * widthCell + widthCell / 2
        centerPt.y = y * widthCell + widthCell / 2
        return centerPt
    }

    private var doorImages: Array<Bitmap> = ResourceManager.getInstance().door!!

    override fun update() {
    }

    override fun draw(canvas: Canvas, paint: Paint) {
//        canvas.drawCircle(x * widthCell + widthCell / 2, y * widthCell + widthCell / 2, widthCell / 3, paint)
        matrix.reset()
        matrix.setScale(widthCell / doorImages[0].width, widthCell / doorImages[0].width)
        matrix.postTranslate(widthCell * x, widthCell * y)

        val distance = calcDistance(player.getPlayerCenterPointF(), this@Door.getCenterPointF())
        if (distance <= widthCell * 5) {
            val rate = distance / widthCell * 2.5 / doorImages.size
            val index: Int = MathUtils.clamp((rate).toInt(), 0, doorImages.size - 1)
            canvas.drawBitmap(doorImages[3 - index], matrix, null)
        } else {
            canvas.drawBitmap(doorImages[0], matrix, null)
        }
    }

    constructor(mazeMap: MazeMap, player: Player) : this(mazeMap.f.x, mazeMap.f.y, player)
}