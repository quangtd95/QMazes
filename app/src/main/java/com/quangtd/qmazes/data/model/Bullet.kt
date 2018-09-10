package com.quangtd.qmazes.data.model

import android.graphics.*
import com.quangtd.qmazes.game.enums.GameDirection
import com.quangtd.qmazes.game.enums.SpriteState
import com.quangtd.qmazes.game.gamemanager.BitmapManager
import java.util.*

/**
 * Created by quang.td95@gmail.com
 * on 9/9/2018.
 */
class Bullet : Sprite() {
    lateinit var map: MazeMap
    var xFloat: Float = 0F
    var yFloat: Float = 0F
    var shurikens: Array<Bitmap> = BitmapManager.getInstance().shuriken!!
    var idShuriken = 0
    var widthCell: Float = 0F
    var matrix = Matrix()
    var direction: GameDirection = GameDirection.STOP
    var velocity: Float = 0.1F
    var radius: Float = 0F
    var currentRotation: Float = 0F
    var velocityRotate: Float = 5F
    lateinit var trap: Trap
    var bulletState: SpriteState = SpriteState.DIED
    var bulletCallBack: Trap.BulletCallBack? = null


    override fun update() {
        if (bulletState == SpriteState.NORMAL) {
            when (direction) {
                GameDirection.UP -> {
                    yFloat -= velocity
                }
                GameDirection.DOWN -> {
                    yFloat += velocity
                }
                GameDirection.LEFT -> {
                    xFloat -= velocity
                }
                GameDirection.RIGHT -> {
                    xFloat += velocity
                }
                GameDirection.STOP -> {

                }
            }
        }
        currentRotation += velocityRotate
        if (currentRotation > 360) {
            currentRotation = 0F
        }
        if (bulletState == SpriteState.NORMAL) {
            if (direction == GameDirection.RIGHT) {
                if (xFloat * widthCell + radius >= map.c * widthCell) {
                    xFloat = map.c.toFloat() - radius / widthCell
                    destroy()
                    return
                }
            } else if (direction == GameDirection.LEFT) {
                if (xFloat * widthCell - radius >= map.c * widthCell) {
                    xFloat = map.c.toFloat() + radius / widthCell
                    destroy()
                    return
                }
            }
            if (direction == GameDirection.UP) {
                if (yFloat * widthCell - radius >= map.r * widthCell) {
                    yFloat = map.r.toFloat() + radius / widthCell
                    destroy()
                    return
                }
            } else if (direction == GameDirection.DOWN) {
                if (yFloat * widthCell + radius >= map.r * widthCell) {
                    yFloat = map.r.toFloat() - radius / widthCell
                    destroy()
                    return
                }
            }
            if (xFloat <= 0) {
                xFloat = 0F
                destroy()
                return
            }
            if (yFloat <= 0) {
                yFloat = 0F
                destroy()
                return
            }
            checkCollisionWall()
        }
    }

    private fun destroy() {
        bulletState = SpriteState.DESTROYING
        bulletCallBack?.onBulletDestroy()
    }

    private fun checkCollisionWall() {
        map.w.forEach {
            if ((this.trap.v1 == it.o && this.trap.v2 == it.d)
                    || (this.trap.v1 == it.d && this.trap.v2 == it.o)) {

            } else {
                if (this.getRect().intersect(it.getRect())) {
                    destroy()
                    return
                }
            }
        }
    }

    private var rect = RectF()
    private fun getRect(): RectF {
        rect.set(xFloat * widthCell - radius / 2, yFloat * widthCell - radius / 2, xFloat * widthCell + radius / 2, yFloat * widthCell + radius / 2)
        return rect
    }

    private var currentDestroy = radius

    override fun draw(canvas: Canvas, paint: Paint) {
        if (bulletState == SpriteState.DIED) {
            return
        }
        if (bulletState == SpriteState.NORMAL) {
            matrix.reset()
            matrix.setScale(radius / shurikens[0].width, radius / shurikens[0].height)
            matrix.postTranslate(xFloat * widthCell - radius / 2, yFloat * widthCell - radius / 2)
            matrix.postRotate(currentRotation, xFloat * widthCell, yFloat * widthCell)
            canvas.drawBitmap(shurikens[idShuriken], matrix, paint)
        }
        if (bulletState == SpriteState.DESTROYING) {
            matrix.reset()
            matrix.setScale(currentDestroy / shurikens[0].width, currentDestroy / shurikens[0].height)
            matrix.postTranslate(xFloat * widthCell - currentDestroy, yFloat * widthCell - currentDestroy)
            matrix.postRotate(currentRotation, xFloat * widthCell, yFloat * widthCell)
            canvas.drawBitmap(shurikens[idShuriken], matrix, paint)
            currentDestroy -= 3
            if (currentDestroy <= 0) {
                bulletState = SpriteState.DIED
            }
        }
    }

    fun resetValue(trap: Trap, widthCell: Float) {
        this.trap = trap
        this.widthCell = widthCell
        velocity = trap.s / 100
        val middle = PointF((trap.v1.x + trap.v2.x).toFloat() / 2, (trap.v1.y + trap.v2.y).toFloat() / 2)
        xFloat = middle.x
        yFloat = middle.y
        radius = widthCell / 1.5f
        direction = trap.direction
        currentRotation = 0F
        velocityRotate = 5F
        bulletState = SpriteState.NORMAL
        currentDestroy = radius
        idShuriken = Random().nextInt(shurikens.size)
    }
}