package com.quangtd.qmazes.game.gameview

import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.game.enums.RenderState

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
interface GamePanel {
    fun draw()
    fun setState(gameState: GameState)
    fun loadGameUI(loadedGameUICallBack: ClassicMazePanel.LoadGameUICallBack?)
    fun loadBufferAsync(loadedGameUICallBack: ClassicMazePanel.LoadGameUICallBack?)
    fun bindRenderCalBack(renderCallBack: RenderState.RenderCallback)
    fun resetValue()
}