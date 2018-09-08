package com.quangtd.qmazes.data.model

/**
 * Created by quang.td95@gmail.com
 * on 9/7/2018.
 */
class PlayerIceFloor(map: MazeMap) : Player(map) {
    private fun checkCollisionIce(): Boolean {
        map.i.forEach { ice ->
            if ((ice.x - 0.5f).toInt() == this@PlayerIceFloor.x
                    && (ice.y - 0.5f).toInt() == this@PlayerIceFloor.y) {
                return true
            }
        }
        return false
    }

    override fun whatNext(map: MazeMap, lastDirection: GameDirection): GameDirection {
        if (!checkCollisionIce()) {
            return super.whatNext(map, lastDirection)
        }
        when (lastDirection) {
            GameDirection.UP -> {
                if (canMoveUp(map)) return GameDirection.UP
            }
            GameDirection.DOWN -> {
                if (canMoveDown(map)) return GameDirection.DOWN
            }
            GameDirection.RIGHT -> {
                if (canMoveRight(map)) return GameDirection.RIGHT
            }
            GameDirection.LEFT -> {
                if (canMoveLeft(map)) return GameDirection.LEFT
            }
            else -> {
                return GameDirection.STOP
            }
        }
        return GameDirection.STOP
    }
}