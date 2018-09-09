package com.quangtd.qmazes.game.gameview

import android.content.Context
import android.view.SurfaceHolder
import android.graphics.*
import android.graphics.Bitmap
import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.game.enums.IntroState
import com.quangtd.qmazes.game.enums.RenderState
import com.quangtd.qmazes.game.gamemanager.GameManager


/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
class DarknessMazePanel(context: Context, gameManager: GameManager, viewHolder: SurfaceHolder) : MazePanel(context, gameManager, viewHolder) {
    private var introState = IntroState.STATE_1
    private var darknessPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var tempPaint = Paint(Paint.FILTER_BITMAP_FLAG).apply {
        alpha = 0
    }
    private var color: Int = Color.BLACK
    private lateinit var tempCanvas: Canvas
    private lateinit var tempBitmap: Bitmap
    private var xferClearMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private var radiusLightDoorIntro: Float = 0F
    private var radiusLightPlayerIntro: Float = 0F
    private var velocityIntro: Float = 25F
    private var defaultRadiusLightDoor = 0F
    private fun defaultRadiusLightPlayer(): Float {
        return widthCell * map.c / 8
//        return player.radius * 3
    }

    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        super.loadGameUI(null)
        tempBitmap = Bitmap.createBitmap((widthCell * map.c).toInt(), (widthCell * map.r).toInt(), Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap)
        defaultRadiusLightDoor = widthCell * map.c / 8
        radiusLightPlayerIntro = widthCell * map.c
        radiusLightDoorIntro = widthCell * map.c
        loadBufferAsync(loadedGameUICallBack)
    }

    override fun drawIntro() {
        super.drawGame(true)

        if (introState == IntroState.STATE_1) {
            super.drawIntroBackground()
        }
        if (currentIntroBackground <= 0) {
            introState = IntroState.STATE_2
        }
        if (introState == IntroState.STATE_2) {
            drawDarkness(radiusLightPlayerIntro, radiusLightDoorIntro)
            radiusLightDoorIntro -= velocityIntro
            radiusLightPlayerIntro -= velocityIntro

            if (radiusLightDoorIntro <= defaultRadiusLightDoor) {
                radiusLightDoorIntro = defaultRadiusLightDoor
            }
            if (radiusLightPlayerIntro <= defaultRadiusLightPlayer()) {
                radiusLightPlayerIntro = defaultRadiusLightPlayer()
            }
        }
        if (radiusLightPlayerIntro == defaultRadiusLightPlayer() && radiusLightDoorIntro == defaultRadiusLightDoor) {
            introState = IntroState.FINAL
        }
        if (introState == IntroState.FINAL) {
            gameManager.forceChangeGameState(GameState.PLAYING)
        }
        drawBound(canvas)


        renderCallback?.changeRenderState(RenderState.REQUEST_RENDER)
    }

    override fun drawGame(isBuffer: Boolean) {
        super.drawGame(isBuffer)
        drawDarkness()
        drawBound(canvas)
    }

    private fun drawDarkness(radiusLightPlayer: Float = defaultRadiusLightPlayer(), radiusLightDoor: Float = defaultRadiusLightDoor) {
        tempPaint.style = Paint.Style.FILL
        tempPaint.xfermode = xferClearMode
        tempCanvas.drawRect(0F, 0F, tempBitmap.width.toFloat(), tempBitmap.height.toFloat(), tempPaint)
        tempPaint.xfermode = null
        tempPaint.color = color
        tempPaint.alpha = 250
        tempCanvas.drawPaint(tempPaint)
        //Draw transparent shape
        tempPaint.xfermode = xferClearMode
        tempCanvas.drawCircle(player.getPlayerCenterPointF().x, player.getPlayerCenterPointF().y, radiusLightPlayer, tempPaint)
        tempCanvas.drawCircle(door.getCenterPointF().x, door.getCenterPointF().y, radiusLightDoor, tempPaint)
//        tempCanvas.drawRect(door.getCenterPointF().x - radiusLightDoor, door.getCenterPointF().y + radiusLightDoor, door.getCenterPointF().x + radiusLightDoor, door.getCenterPointF().y - radiusLightDoor, tempPaint)
        canvas.drawBitmap(tempBitmap, 0F, 0F, darknessPaint)
        tempPaint.xfermode = null
    }

    override fun resetValue() {
        super.resetValue()
        defaultRadiusLightDoor = widthCell * map.c / 8
        radiusLightPlayerIntro = widthCell * map.c
        radiusLightDoorIntro = widthCell * map.c
        introState = IntroState.STATE_1
    }
}