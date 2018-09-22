package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import com.quangtd.qmazes.data.model.Player
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.game.enums.GameState

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class MazeClassicManager(level: Int = 1, gameKind: GameKind = GameKind.CLASSIC) :
        AbstractGameManager(gameKind, level), Player.PlayerCallBack {

    override fun changeDirectionCallBack() {
        soundManager.playTouchSound()
    }

    override fun onStop() {
    }

    override fun loadGame(context: Context,randomMap : Boolean) {
        super.loadGame(context,randomMap)
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