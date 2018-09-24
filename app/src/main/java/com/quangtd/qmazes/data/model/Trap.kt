package com.quangtd.qmazes.data.model

import android.graphics.*
import com.google.gson.annotations.SerializedName
import com.quangtd.qmazes.game.enums.GameDirection
import com.quangtd.qmazes.game.enums.SpriteState

/**
 * Created by quang.td95@gmail.com
 * on 9/9/2018.
 */
class Trap : Sprite() {

    /**
     * d : down
     * u : up
     * r : right
     * l : left
     */
    @SerializedName("d")
    var d: String = "d"

    var direction: GameDirection = GameDirection.STOP

    @SerializedName("f")
    var f: Float = 0F

    var s: Float = 0F

    @SerializedName("ds")
    var duration: Float = 0F

    @SerializedName("v1")
    var v1: Point = Point(0, 0)

    @SerializedName("v2")
    var v2: Point = Point(0, 0)

    private var path: Path = Path()
    private var radius: Float = 10F
    var bulletCallBack: BulletCallBack? = null
        set(value) {
            field = value
            bullet.bulletCallBack = value
        }

    var widthCell: Float = 0F
        set(value) {
            field = value
            radius = value / 2
            path = Path()
            val middle = PointF((v1.x + v2.x).toFloat() / 2 * widthCell, (v1.y + v2.y).toFloat() / 2 * widthCell)
            when (direction) {
                GameDirection.DOWN -> {
                    path.moveTo(middle.x - radius / 2, middle.y)
                    path.lineTo(middle.x + radius / 2, middle.y)
                    path.lineTo(middle.x, (middle.y + radius * Math.sqrt(3.0) / 2).toFloat())
                    path.moveTo(middle.x - radius / 2, middle.y)

                }
                GameDirection.UP -> {
                    path.moveTo(middle.x - radius / 2, middle.y)
                    path.lineTo(middle.x + radius / 2, middle.y)
                    path.lineTo(middle.x, (middle.y - radius * Math.sqrt(3.0) / 2).toFloat())
                    path.moveTo(middle.x - radius / 2, middle.y)

                }
                GameDirection.RIGHT -> {
                    path.moveTo(middle.x, middle.y - radius / 2)
                    path.lineTo(middle.x, middle.y + radius / 2)
                    path.lineTo((middle.x + radius * Math.sqrt(3.0) / 2).toFloat(), middle.y)
                    path.moveTo(middle.x, middle.y - radius / 2)

                }
                GameDirection.LEFT -> {
                    path.moveTo(middle.x, middle.y - radius / 2)
                    path.lineTo(middle.x, middle.y + radius / 2)
                    path.lineTo((middle.x - radius * Math.sqrt(3.0) / 2).toFloat(), middle.y)
                    path.moveTo(middle.x, middle.y - radius / 2)
                }
                GameDirection.STOP -> {
                }
            }
            path.close()
        }


    lateinit var map: MazeMap
    var bullet: Bullet = Bullet()
    var lastTime: Long = 0L

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        canvas.drawPath(path, paint)
        bullet.draw(canvas, paint)
    }

    override fun update() {
        if (bullet.bulletState == SpriteState.DIED) {
            if (System.currentTimeMillis() - lastTime > this.duration * 1000) {
                fireBullet()
            }
        } else {
            bullet.update()
            lastTime = System.currentTimeMillis()
        }

    }

    private fun fireBullet() {
        bullet.map = map
        bullet.resetValue(this, widthCell)
        bulletCallBack?.onBulletFired()
    }

    fun reLoad() {
        bullet.bulletState = SpriteState.DIED

    }

    interface BulletCallBack {
        fun onBulletFired()
        fun onBulletDestroy()
    }

}