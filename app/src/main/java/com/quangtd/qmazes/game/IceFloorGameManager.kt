package com.quangtd.qmazes.game

import android.content.Context
import com.quangtd.qmazes.data.model.GameDirection
import com.quangtd.qmazes.data.model.MazeMap
import com.quangtd.qmazes.data.model.PlayerIceFloor

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class IceFloorGameManager(level: Int) : AbstractGameManager(gameKind = GameKind.ICE, level = level) {

    override fun update() {

    }

    override fun checkWinGame(): Boolean {
        //TODO
        return false
    }

    override fun action(direction: GameDirection) {

    }

    override fun initGame(context: Context) {
        super.initGame(context)
        player = PlayerIceFloor(map)
        map.i.forEach {
            it.widthCell = widthCell
        }
    }
}