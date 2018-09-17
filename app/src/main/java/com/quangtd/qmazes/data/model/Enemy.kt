package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Paint
import com.quangtd.qmazes.game.enums.GameDirection
import com.quangtd.qmazes.game.enums.SpriteState
import java.util.*
import kotlin.collections.ArrayList

class Enemy(x: Int = 0, y: Int = 0, map: MazeMap) : Player(x, y, map) {
    private var random = Random()
    var currentRotation: Float = 0F
    var velocityRotate: Float = 5F
    var enemyState: SpriteState = SpriteState.NORMAL
    var xDefault = 0
    var yDefault = 0

    init {
        xDefault = x
        yDefault = y
        defaultValue()
    }

    private var listDirectionCanUsing = ArrayList<GameDirection>()
    override fun whatNext(map: MazeMap, lastDirection: GameDirection): GameDirection {
        listDirectionCanUsing.clear()
        var nextDirection = GameDirection.STOP
        val canMoveDown: Boolean = canMoveDown(map)
        if (canMoveDown) {
            if (lastDirection != GameDirection.UP) {
                nextDirection = GameDirection.DOWN
                listDirectionCanUsing.add(GameDirection.DOWN)
            }
        }
        val canMoveUp: Boolean = canMoveUp(map)
        if (canMoveUp) {
            if (lastDirection != GameDirection.DOWN) {
                nextDirection = GameDirection.UP
                listDirectionCanUsing.add(GameDirection.UP)
            }
        }
        val canMoveLeft: Boolean = canMoveLeft(map)
        if (canMoveLeft) {
            if (lastDirection != GameDirection.RIGHT) {
                nextDirection = GameDirection.LEFT
                listDirectionCanUsing.add(GameDirection.LEFT)
            }
        }
        val canMoveRight: Boolean = canMoveRight(map)
        if (canMoveRight) {
            if (lastDirection != GameDirection.LEFT) {
                nextDirection = GameDirection.RIGHT
                listDirectionCanUsing.add(GameDirection.RIGHT)
            }
        }
        if (listDirectionCanUsing.size >= 2) {
            return listDirectionCanUsing[random.nextInt(listDirectionCanUsing.size)]
        }
        if (listDirectionCanUsing.size == 1) {
            return listDirectionCanUsing[0]
        }
        return nextDirection
    }

    override fun update() {
        currentRotation += velocityRotate
        if (currentRotation > 360) {
            currentRotation = 0F
        }
        lastDirection = direction
        if (direction != GameDirection.STOP) {
            when (direction) {
                GameDirection.UP -> {
                    xFloat = x.toFloat()
                    yFloat += velocity
                    nextDestination.x = x
                    nextDestination.y = y + 1
                    isRollback = footPrints.size >= 2 && nextDestination == footPrints.elementAt(footPrints.size - 2)
                    if (yFloat > nextDestination.y) {
                        moveUp()
                    }
                }
                GameDirection.DOWN -> {
                    xFloat = x.toFloat()
                    yFloat -= velocity
                    nextDestination.x = x
                    nextDestination.y = y - 1
                    isRollback = footPrints.size >= 2 && nextDestination == footPrints.elementAt(footPrints.size - 2)
                    if (yFloat < nextDestination.y) {
                        moveDown()
                    }
                }
                GameDirection.LEFT -> {
                    yFloat = y.toFloat()
                    xFloat -= velocity
                    nextDestination.x = x - 1
                    nextDestination.y = y
                    isRollback = footPrints.size >= 2 && nextDestination == footPrints.elementAt(footPrints.size - 2)
                    if (xFloat < nextDestination.x) {
                        moveLeft()
                    }
                }
                GameDirection.RIGHT -> {
                    yFloat = y.toFloat()
                    xFloat += velocity
                    nextDestination.x = x + 1
                    nextDestination.y = y
                    isRollback = footPrints.size >= 2 && nextDestination == footPrints.elementAt(footPrints.size - 2)
                    if (xFloat > nextDestination.x) {
                        moveRight()
                    }
                }
                GameDirection.STOP -> {
                    //do nothing
                }
            }
        }
        if (direction == GameDirection.STOP) {
            val nextDirection = whatNext(map, lastDirection)
            direction = nextDirection
            if (lastDirection != nextDirection) {
                playerCallback?.changeDirectionCallBack()
            }
        }
        if (direction == GameDirection.STOP) {
            playerCallback?.onStop()
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        radius = widthCell / 5
        if (!isMoving()) {
            canvas.drawCircle(widthCell * x + widthCell / 2, widthCell * y + widthCell / 2, radius, paint)
        } else {
            canvas.drawCircle(widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2, radius, paint)
        }
    }

    override fun reload() {
        super.reload()
        defaultValue()

    }

    private fun defaultValue() {
        lastDirection = GameDirection.values()[random.nextInt(GameDirection.values().size)]
        velocity = 0.04F
        velocityRotate = 0.5F
        x = xDefault
        y = yDefault
        enemyState = SpriteState.NORMAL
    }

}