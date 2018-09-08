package com.quangtd.qmazes.game

import android.content.Context
import com.quangtd.qmazes.data.model.GameDirection
import com.quangtd.qmazes.data.model.MazeMap

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class MazeClassicManager(level: Int = 1, gameKind: GameKind = GameKind.CLASSIC) :
        AbstractGameManager(gameKind, level), PlayerCallBack {

    override fun changeDirectionCallBack() {
        soundManager.playTouchSound()
    }

    override fun stopCallback() {
    }

    override fun loadGame(context: Context) {
        super.loadGame(context)
        player.playerCallback = this
    }

    override fun update() {
        player.update()
        if (checkWinGame()) {
            gameStateCallback?.onGameStateChangeCallback(GameState.WIN_GAME)
        }
    }

    override fun checkWinGame(): Boolean {
        return player.x == door.x && player.y == door.y
    }

}