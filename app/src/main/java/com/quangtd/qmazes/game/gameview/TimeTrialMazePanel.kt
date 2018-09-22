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
class TimeTrialMazePanel(context: Context, gameManager: GameManager, viewHolder: SurfaceHolder) : ClassicMazePanel(context, gameManager, viewHolder) {


    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        super.loadGameUI(null)
        widthWall = widthCell / 7
        colorPlayer = ColorUtils.colorTimeTrial
        loadBufferAsync(loadedGameUICallBack)
    }

    fun reloadUI(context: Context, loadedGameUICallBack: LoadGameUICallBack?) {
        tempBackgroundBitmap?.recycle()
        tempBackgroundBitmap = null
        loadGameUI(loadedGameUICallBack)
    }

}