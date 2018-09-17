package com.quangtd.qmazes.game.enums

/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
enum class GameState {
    LOADING,
    LOADED,
    INTRO,
    PLAYING,
    PAUSE,
    STOP,
    WIN_GAME,
    LOSE_GAME;

    interface GameStateCallBack {
        fun onGameStateChangeCallback(gameState: GameState)
    }
}