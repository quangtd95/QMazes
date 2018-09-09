package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import com.quangtd.qmazes.data.model.Player
import com.quangtd.qmazes.data.model.Trap
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.game.enums.GameState

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class TrapsGameManager(level: Int = 1, gameKind: GameKind = GameKind.TRAP) :
        AbstractGameManager(gameKind, level), Player.PlayerCallBack, Trap.BulletCallBack {

    override fun onBulletFired() {
        soundManager.playBulletShoot()
    }

    override fun onBulletDestroy() {
        soundManager.playBulletDestroy()
    }

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
        map.t.forEach { trap ->
            trap.bulletCallBack = this
        }
    }

    override fun update() {
        player.update()
        map.t.forEach { trap -> trap.update() }
        if (checkWinGame()) {
            gameStateCallback?.onGameStateChangeCallback(GameState.WIN_GAME)
        }
    }

    override fun checkWinGame(): Boolean {
        return player.x == door.x && player.y == door.y
    }

    override fun reload() {
        map.t.forEach { t -> t.reLoad() }
        super.reload()
    }

}