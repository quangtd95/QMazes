package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint
import com.quangtd.qmazes.game.enums.SpriteState
import kotlin.math.sin


/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class PlayerTrap(map: MazeMap) : Player(map) {
    var playerState: SpriteState = SpriteState.NORMAL
    override fun update() {
        if (playerState == SpriteState.NORMAL) {
            super.update()
            checkCollisionBullet()
        }
        if (playerState == SpriteState.DESTROYING) {

        }
        if (playerState == SpriteState.DIED) {
            playerCallback?.onStop()
            reload()
        }
    }

    private var currentDestroying = 0F

    override fun draw(canvas: Canvas, paint: Paint) {
        if (playerState == SpriteState.NORMAL) {
            super.draw(canvas, paint)
        }
        if (playerState == SpriteState.DESTROYING) {
            val temp = currentDestroying * sin(45.0F)
            //top
            canvas.drawCircle(widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2 - currentDestroying, radius / 3, paint)
            //top -left
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 - temp, widthCell * yFloat + widthCell / 2 - temp, radius / 3, paint)
            //top -right
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 + temp, widthCell * yFloat + widthCell / 2 - temp, radius / 3, paint)
            //left
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 - currentDestroying, widthCell * yFloat + widthCell / 2, radius / 3, paint)
            //right
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 + currentDestroying, widthCell * yFloat + widthCell / 2, radius / 3, paint)
            //bottom
            canvas.drawCircle(widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2 + currentDestroying, radius / 3, paint)
            //bottom-left
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 - temp, widthCell * yFloat + widthCell / 2 + temp, radius / 3, paint)
            //bottom- right
            canvas.drawCircle(widthCell * xFloat + widthCell / 2 + temp, widthCell * yFloat + widthCell / 2 + temp, radius / 3, paint)
            currentDestroying += 2
            if (currentDestroying >= radius * 2.5) {
                currentDestroying = 0F
                playerState = SpriteState.DIED
            }
        }
    }

    private fun checkCollisionBullet() {
        map.t.forEach {
            if (it.bullet.bulletState == SpriteState.NORMAL && playerState == SpriteState.NORMAL) {
                if (collides(this, it.bullet)) {
                    it.bullet.bulletState = SpriteState.DESTROYING
                    playerState = SpriteState.DESTROYING
                    playerCallback?.onDied()
                }
            }
        }
    }

    private fun collides(c1: PlayerTrap, c2: Bullet): Boolean {
        val centerPointPlayer = c1.getPlayerCenterPointF()
        val distance = Math.sqrt(Math.pow(((centerPointPlayer.x - c2.xFloat * widthCell).toDouble()), 2.0) + Math.pow((centerPointPlayer.y - c2.yFloat * widthCell).toDouble(), 2.0))
        return (distance < (c1.radius + c2.radius) / 2)
    }

    override fun reload() {
        super.reload()
        playerState = SpriteState.NORMAL
        currentDestroying = 0F
    }


}