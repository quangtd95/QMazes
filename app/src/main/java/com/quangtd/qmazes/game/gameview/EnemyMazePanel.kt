package com.quangtd.qmazes.game.gameview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.quangtd.qmazes.game.enums.IntroState
import com.quangtd.qmazes.game.gamemanager.GameManager

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class EnemyMazePanel(context: Context, gameManager: GameManager, viewHolder: SurfaceHolder) : MazePanel(context, gameManager, viewHolder) {
    var introState: IntroState = IntroState.STATE_1
    var colorEnemy: Int = Color.RED

    private var paintEnemy = Paint().apply {
        color = colorEnemy
    }

    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        super.loadGameUI(null)
        widthWall = widthCell / 7
        loadBufferAsync(loadedGameUICallBack)
    }

    override fun drawGame(isBuffer: Boolean) {
        super.drawGame(isBuffer)
        drawEnemies()
        drawBound(canvas)
    }

    private fun drawEnemies() {
        map.lstEnemy.forEach { enemy -> enemy.draw(canvas, paintEnemy) }

    }

    override fun resetValue() {
        super.resetValue()
        introState = IntroState.STATE_1
    }
}