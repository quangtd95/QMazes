package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import com.quangtd.qmazes.data.model.Enemy
import com.quangtd.qmazes.data.model.Player
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.game.enums.GameState

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class EnemyGameManager(level: Int = 1, gameKind: GameKind = GameKind.ENEMIES) :
        AbstractGameManager(gameKind, level), Player.PlayerCallBack {

    override fun changeDirectionCallBack() {
        soundManager.playTouchSound()
    }

    override fun onStop() {
    }

    override fun onDied() {
        soundManager.playPlayerDied()
    }

    override fun loadGame(context: Context) {
        super.loadGame(context)
        player.playerCallback = this
    }

    override fun update() {
        player.update()
        map.lstEnemy.forEach { it -> it.update() }
        if (checkWinGame()) {
            gameStateCallback?.onGameStateChangeCallback(GameState.WIN_GAME)
        }
    }

    override fun checkWinGame(): Boolean {
        return player.x == door.x && player.y == door.y
    }

    override fun reload() {
        map.lstEnemy.clear()
        map.lstEnemy = ArrayList(map.e.map { Enemy(it.x, it.y, map) })
        map.lstEnemy.forEach {
            it.widthCell = this@EnemyGameManager.widthCell
        }
        super.reload()
    }

}