package com.quangtd.qmazes.game

import android.content.Context
import com.quangtd.qmazes.data.model.Door
import com.quangtd.qmazes.data.model.GameDirection
import com.quangtd.qmazes.data.model.MazeMap
import com.quangtd.qmazes.data.model.Player

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
interface GameManager {
    fun loadGame(context: Context)
    fun reload()
    fun getMazeMap(): MazeMap
    fun getPlayerObject(): Player
    fun getDoorObject(): Door
    fun update()
    fun checkWinGame(): Boolean
    fun action(direction: GameDirection)
    fun forceChangeGameState(gameState: GameState)
    fun bindRenderCallback(renderCallBack: RenderState.RenderCallback)
    fun bindGameStateCallback(gameStateCallBack: GameState.GameStateCallBack)
}