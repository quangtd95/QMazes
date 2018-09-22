package com.quangtd.qmazes.game.gameview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.quangtd.qmazes.game.enums.IntroState
import com.quangtd.qmazes.game.gamemanager.GameManager
import com.quangtd.qmazes.util.ColorUtils

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class TrapsMazePanel(context: Context, gameManager: GameManager, viewHolder: SurfaceHolder) : ClassicMazePanel(context, gameManager, viewHolder) {
    var introState: IntroState = IntroState.STATE_1
    var colorTrap: Int = ColorUtils.colorTrap

    private var paintTrap = Paint().apply {
        color = colorTrap
    }

    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        super.loadGameUI(null)
        widthWall = widthCell / 7
        colorPlayer = ColorUtils.colorTrap
        loadBufferAsync(loadedGameUICallBack)
    }

    override fun drawGame(isBuffer: Boolean) {
        super.drawGame(isBuffer)
        drawTrap()
        drawBound(canvas)
    }

    private fun drawTrap() {
        map.t.forEach { trap -> trap.draw(canvas, paintTrap) }

    }

    override fun resetValue() {
        super.resetValue()
        introState = IntroState.STATE_1
    }
}