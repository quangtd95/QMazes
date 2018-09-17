package com.quangtd.qmazes.ui.screen.game

import android.view.SurfaceHolder
import com.quangtd.qstudio.mvpbase.IBaseView

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
interface IGameView : IBaseView {
    fun getSurfaceHolder(): SurfaceHolder
    fun showWinGameAlert()
    fun showLoseGameAlert()
    fun updateRemainingTime(secondRemaining: Int)
}