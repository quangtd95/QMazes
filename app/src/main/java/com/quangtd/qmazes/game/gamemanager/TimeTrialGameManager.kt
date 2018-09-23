package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import com.quangtd.qmazes.data.model.Player
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.util.LogUtils

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class TimeTrialGameManager(level: Int = 1, gameKind: GameKind = GameKind.CLASSIC) :
        AbstractGameManager(gameKind, level), Player.PlayerCallBack {

    var totalTime = 0L
    var elapsedTime = 0L
    var onCountingTimeCallback: OnCountingTimeCallback? = null

    override fun changeDirectionCallBack() {
        soundManager.playTouchSound()
    }

    override fun onStop() {
    }

    private var isPlayingBackgroundSound = false
    fun playBackgroundSound() {
        if (!isPlayingBackgroundSound) {
            if (soundManager.playSoundClockTick()) {
                isPlayingBackgroundSound = true
            }
        }
    }

    fun stopBackgroundSound() {
        if (isPlayingBackgroundSound) {
            if (soundManager.stopSoundClockTick()) {
                isPlayingBackgroundSound = false
            }
        }
    }

    override fun loadGame(context: Context, randomMap: Boolean) {
        super.loadGame(context, true)
        player.playerCallback = this
        totalTime = (map.r.toFloat() / 9 * 9 * 1000).toLong()
    }

    fun reload(context: Context) {
        loadGame(context, true)
        forceChangeGameState(GameState.INTRO)
        elapsedTime = 0L
    }

    override fun update() {
        player.update()
        if (gameState == GameState.PLAYING) {
            elapsedTime += (System.currentTimeMillis() - startTime)
            LogUtils.e(elapsedTime.toString() + "---elapsedTime")
            onCountingTimeCallback?.onRemainingTime(((totalTime - elapsedTime) / 1000).toInt())
            if (checkLoseGame()) {
                soundManager.playSoundTimeUp()
                stopBackgroundSound()
                forceChangeGameState(GameState.LOSE_GAME)
            }
            if (checkWinGame()) {
                stopBackgroundSound()
                forceChangeGameState(GameState.WIN_GAME)
            }
            startTime = System.currentTimeMillis()
        }
    }

    private fun checkLoseGame(): Boolean {
        return elapsedTime >= totalTime
    }

    override fun checkWinGame(): Boolean {
        return player.x == door.x && player.y == door.y
    }

    interface OnCountingTimeCallback {
        fun onRemainingTime(secondRemaining: Int)
    }

}