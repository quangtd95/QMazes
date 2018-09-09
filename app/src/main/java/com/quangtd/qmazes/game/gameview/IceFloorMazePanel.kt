package com.quangtd.qmazes.game.gameview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.game.enums.IntroState
import com.quangtd.qmazes.game.gamemanager.GameManager

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class IceFloorMazePanel(context: Context, gameManager: GameManager, viewHolder: SurfaceHolder) : MazePanel(context, gameManager, viewHolder) {
    var introState: IntroState = IntroState.STATE_1
    var colorIce: Int = Color.rgb(140, 204, 255)

    private var paintIce = Paint().apply {
        color = colorIce
    }
    private var currentIntroIce = 0F
    private var velocityDrawIceBlock = 2F

    override fun drawIntro() {
        super.drawGame(false)
        super.drawIntroBackground()
        if (introState == IntroState.STATE_1) {
            if (currentIntroBackground <= 0) {
                introState = IntroState.STATE_2
            }
        }
        if (introState == IntroState.STATE_2) {
            map.i.forEach { it -> it.draw(canvas, paintIce, currentIntroIce) }
            currentIntroIce += velocityDrawIceBlock
            if (currentIntroIce >= map.i[0].radius) {
                currentIntroIce = map.i[0].radius
                introState = IntroState.FINAL
            }
            /**
             * vẽ lại vì khi draw intro, ice block đang đè lên door + wall
             */
            drawWall(canvas)
            drawBound(canvas)
            drawDoor()
        }
        if (introState == IntroState.FINAL) {
            gameManager.forceChangeGameState(GameState.PLAYING)
        }
    }


    override fun drawIce(tempCanvasBackground: Canvas) {
        if (gameState == GameState.INTRO) return
        map.i.forEach { ice ->
            if (!((ice.x - 0.5F).toInt() == map.f.x && (ice.y - 0.5F).toInt() == map.f.y)) {
                ice.draw(tempCanvasBackground, paintIce)
            }

        }
    }

    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        super.loadGameUI(null)
        widthWall = widthCell / 7
        super.loadBufferAsync(loadedGameUICallBack)
    }

    override fun resetValue() {
        super.resetValue()
        widthWall = widthCell / 7
        velocityDrawIceBlock = 2F
        currentIntroIce = 0F
        introState = IntroState.STATE_1
    }
}