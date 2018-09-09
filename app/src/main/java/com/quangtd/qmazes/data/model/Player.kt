package com.quangtd.qmazes.data.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.quangtd.qmazes.game.enums.GameDirection
import java.util.*
import kotlin.math.sqrt

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
open class Player(var x: Int = 0, var y: Int = 0, var map: MazeMap) : Sprite() {
    constructor(mazeMap: MazeMap) : this(mazeMap.s.x, mazeMap.s.y, mazeMap)

    var isWaitingActionLeft: Boolean = false
    var isWaitingActionRight: Boolean = false
    var isWaitingActionUp: Boolean = false
    var isWaitingActionDown: Boolean = false

    var widthCell: Float = 0F
    var widthWaitingAction: Float = 0F
    var xFloat: Float = x.toFloat()
    var yFloat: Float = y.toFloat()
    var radius = 0F
    var nextDestination = Point()
    private var centerPoint = android.graphics.PointF()
    private var centerPointF = android.graphics.PointF()
    var playerCallback: PlayerCallBack? = null

    private fun getPlayerCenterPoint(): android.graphics.PointF {
        centerPoint.x = (x * widthCell + widthCell / 2)
        centerPoint.y = (y * widthCell + widthCell / 2)
        return centerPoint
    }

    fun getPlayerCenterPointF(): android.graphics.PointF {
        centerPointF.x = (xFloat * widthCell + widthCell / 2)
        centerPointF.y = (yFloat * widthCell + widthCell / 2)
        return centerPointF
    }

    var isRollback: Boolean = false
    var footPrints = Stack<Point>().apply {
        add(Point(x, y))
    }
    private var direction: GameDirection = GameDirection.STOP
    var lastDirection: GameDirection = GameDirection.STOP
    var velocity: Float = 0.2F

    fun changeDirection(newDirection: GameDirection) {
        if (!isMoving())
            direction = when (newDirection) {
                GameDirection.UP -> if (canMoveUp(map)) newDirection else GameDirection.STOP
                GameDirection.DOWN -> if (canMoveDown(map)) newDirection else GameDirection.STOP
                GameDirection.LEFT -> if (canMoveLeft(map)) newDirection else GameDirection.STOP
                GameDirection.RIGHT -> if (canMoveRight(map)) newDirection else GameDirection.STOP
                GameDirection.STOP -> GameDirection.STOP
            }
    }

    protected fun canMoveUp(map: MazeMap): Boolean {
        var canMoveUp = true
        val fence = Wall()
        fence.o = Point(x, y + 1)
        fence.d = Point(x + 1, y + 1)
        map.w.forEach { wall ->
            if (wall == fence) {
                canMoveUp = false
                return@forEach
            }
        }
        if (y + 1 >= map.r) {
            canMoveUp = false
        }
        return canMoveUp
    }

    protected fun canMoveDown(map: MazeMap): Boolean {
        var canMoveDown = true
        val fence = Wall()
        fence.o = Point(x, y)
        fence.d = Point(x + 1, y)
        map.w.forEach { wall ->
            if (wall == fence) {
                canMoveDown = false
                return@forEach
            }
        }
        if (y - 1 < 0) {
            canMoveDown = false
        }
        return canMoveDown
    }

    protected fun canMoveLeft(map: MazeMap): Boolean {
        var canMoveLeft = true
        val fence = Wall()
        fence.d = Point(x, y)
        fence.o = Point(x, y + 1)
        map.w.forEach { wall ->
            if (wall == fence) {
                canMoveLeft = false
                return@forEach
            }
        }
        if (map.w.contains(fence)) {
            canMoveLeft = false
        }
        if (x - 1 < 0) {
            canMoveLeft = false
        }
        return canMoveLeft
    }

    protected fun canMoveRight(map: MazeMap): Boolean {
        var canMoveRight = true
        val fence = Wall()
        fence.d = Point(x + 1, y)
        fence.o = Point(x + 1, y + 1)
        map.w.forEach { wall ->
            if (wall == fence) {
                canMoveRight = false
                return@forEach
            }
        }
        if (x + 1 >= map.c) {
            canMoveRight = false
        }
        return canMoveRight
    }

    open fun whatNext(map: MazeMap, lastDirection: GameDirection): GameDirection {
        var count = 0
        var nextDirection = GameDirection.STOP
        val canMoveDown: Boolean = canMoveDown(map)
        if (canMoveDown) {
            count++
            if (lastDirection != GameDirection.UP) {
                nextDirection = GameDirection.DOWN
            }
            isWaitingActionDown = true
        }
        val canMoveUp: Boolean = canMoveUp(map)
        if (canMoveUp) {
            count++
            if (lastDirection != GameDirection.DOWN) {
                nextDirection = GameDirection.UP
            }
            isWaitingActionUp = true
        }
        val canMoveLeft: Boolean = canMoveLeft(map)
        if (canMoveLeft) {
            count++
            if (lastDirection != GameDirection.RIGHT) {
                nextDirection = GameDirection.LEFT
            }
            isWaitingActionLeft = true
        }
        val canMoveRight: Boolean = canMoveRight(map)
        if (canMoveRight) {
            count++
            if (lastDirection != GameDirection.LEFT) {
                nextDirection = GameDirection.RIGHT
            }
            isWaitingActionRight = true
        }
        if (count != 2) {
            return GameDirection.STOP
        }
        clearWaitingFlg()
        return nextDirection
    }

    private fun clearWaitingFlg() {
        isWaitingActionDown = false
        isWaitingActionLeft = false
        isWaitingActionRight = false
        isWaitingActionUp = false
    }

    private fun updateFootPrints() {
        if (footPrints.size >= 2) {
            val currentPointInStack = footPrints.pop()
            val lastPointInStack = footPrints.peek()
            if (lastPointInStack.x != x || lastPointInStack.y != y) {
                footPrints.push(currentPointInStack)
                footPrints.push(Point(x, y))
            } else {
            }
        } else {
            footPrints.push(Point(x, y))
        }

    }

    private fun moveUp() {
        y += 1
        direction = GameDirection.STOP
        xFloat = x.toFloat()
        yFloat = y.toFloat()
        updateFootPrints()
    }

    private fun moveDown() {
        y -= 1
        direction = GameDirection.STOP
        xFloat = x.toFloat()
        yFloat = y.toFloat()
        updateFootPrints()
    }

    private fun moveLeft() {
        x -= 1
        direction = GameDirection.STOP
        xFloat = x.toFloat()
        yFloat = y.toFloat()
        updateFootPrints()
    }

    private fun moveRight() {
        x += 1
        direction = GameDirection.STOP
        xFloat = x.toFloat()
        yFloat = y.toFloat()
        updateFootPrints()
    }

    override fun update() {
        lastDirection = direction
        if (direction != GameDirection.STOP) {
            clearWaitingFlg()
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
        if (lastDirection != GameDirection.STOP && direction == GameDirection.STOP) {
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
        drawPlayer(canvas, paint)
        drawFootPrints(canvas, paint)
        drawWaitingAction(canvas, paint)
    }

    private var padding = 5
    private var n = 1
    private var colorBk = 0
    private fun drawWaitingAction(canvas: Canvas, paint: Paint) {
        if (map.f.x == x && map.f.y == y) {
            return
        }
        widthWaitingAction += n
        if (widthWaitingAction > radius / 2) {
            widthWaitingAction = radius / 2
            n = -1
        }
        if (widthWaitingAction < radius / 3) {
            widthWaitingAction = radius / 3
            n = 1
        }
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        colorBk = paint.color
        paint.color = Color.YELLOW
        if (isWaitingActionDown) {
            canvas.drawLine(getPlayerCenterPoint().x - widthWaitingAction, getPlayerCenterPoint().y - radius - padding, getPlayerCenterPoint().x + widthWaitingAction, getPlayerCenterPoint().y - radius - padding, paint)
            canvas.drawLine(getPlayerCenterPoint().x - widthWaitingAction, getPlayerCenterPoint().y - radius - padding, getPlayerCenterPoint().x, (getPlayerCenterPoint().y - radius - padding - widthWaitingAction * sqrt(3F) / 2), paint)
            canvas.drawLine(getPlayerCenterPoint().x + widthWaitingAction, getPlayerCenterPoint().y - radius - padding, getPlayerCenterPoint().x, getPlayerCenterPoint().y - radius - padding - widthWaitingAction * sqrt(3F) / 2, paint)
        }
        if (isWaitingActionUp) {
            canvas.drawLine(getPlayerCenterPoint().x - widthWaitingAction, getPlayerCenterPoint().y + radius + padding, getPlayerCenterPoint().x + widthWaitingAction, getPlayerCenterPoint().y + radius + padding, paint)
            canvas.drawLine(getPlayerCenterPoint().x - widthWaitingAction, getPlayerCenterPoint().y + radius + padding, getPlayerCenterPoint().x, (getPlayerCenterPoint().y + radius + padding + widthWaitingAction * sqrt(3F) / 2), paint)
            canvas.drawLine(getPlayerCenterPoint().x + widthWaitingAction, getPlayerCenterPoint().y + radius + padding, getPlayerCenterPoint().x, getPlayerCenterPoint().y + radius + padding + widthWaitingAction * sqrt(3F) / 2, paint)
        }
        if (isWaitingActionLeft) {
            canvas.drawLine(getPlayerCenterPoint().x - radius - padding, getPlayerCenterPoint().y - widthWaitingAction, getPlayerCenterPoint().x - radius - padding, getPlayerCenterPoint().y + widthWaitingAction, paint)
            canvas.drawLine(getPlayerCenterPoint().x - radius - padding, getPlayerCenterPoint().y + widthWaitingAction, getPlayerCenterPoint().x - radius - padding - widthWaitingAction * sqrt(3F) / 2, getPlayerCenterPoint().y, paint)
            canvas.drawLine(getPlayerCenterPoint().x - radius - padding, getPlayerCenterPoint().y - widthWaitingAction, getPlayerCenterPoint().x - radius - padding - widthWaitingAction * sqrt(3F) / 2, getPlayerCenterPoint().y, paint)
        }
        if (isWaitingActionRight) {
            canvas.drawLine(getPlayerCenterPoint().x + radius + padding, getPlayerCenterPoint().y - widthWaitingAction, getPlayerCenterPoint().x + radius + padding, getPlayerCenterPoint().y + widthWaitingAction, paint)
            canvas.drawLine(getPlayerCenterPoint().x + radius + padding, getPlayerCenterPoint().y - widthWaitingAction, getPlayerCenterPoint().x + radius + padding + widthWaitingAction * sqrt(3F) / 2, getPlayerCenterPoint().y, paint)
            canvas.drawLine(getPlayerCenterPoint().x + radius + padding, getPlayerCenterPoint().y + widthWaitingAction, getPlayerCenterPoint().x + radius + padding + widthWaitingAction * sqrt(3F) / 2, getPlayerCenterPoint().y, paint)
        }
        paint.style = Paint.Style.FILL
        paint.color = colorBk
    }


    private fun drawFootPrints(canvas: Canvas, paint: Paint) {
        paint.strokeWidth = this.radius / 2
        footPrints.forEachIndexed { index, point ->
            if (!isRollback) {
                if (index < footPrints.size - 1) {
                    val d = footPrints[index + 1]
                    canvas.drawLine(widthCell * point.x + widthCell / 2, widthCell * point.y + widthCell / 2, widthCell * d.x + widthCell / 2, widthCell * d.y + widthCell / 2, paint)
                } else if (index == footPrints.size - 1) {
                    canvas.drawLine(widthCell * point.x + widthCell / 2, widthCell * point.y + widthCell / 2, widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2, paint)
                }
            } else {
                if (index < footPrints.size - 2) {
                    val d = footPrints[index + 1]
                    canvas.drawLine(widthCell * point.x + widthCell / 2, widthCell * point.y + widthCell / 2, widthCell * d.x + widthCell / 2, widthCell * d.y + widthCell / 2, paint)
                } else if (index == footPrints.size - 2) {
                    canvas.drawLine(widthCell * point.x + widthCell / 2, widthCell * point.y + widthCell / 2, widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2, paint)
                }
            }
        }
    }

    private fun drawPlayer(canvas: Canvas, paint: Paint) {
        radius = widthCell / 3
        if (!isMoving()) {
            canvas.drawCircle(widthCell * x + widthCell / 2, widthCell * y + widthCell / 2, radius, paint)
        } else {
            canvas.drawCircle(widthCell * xFloat + widthCell / 2, widthCell * yFloat + widthCell / 2, radius, paint)
        }
    }

    fun isMoving(): Boolean {
        return direction != GameDirection.STOP
    }

    open fun reload() {
        direction = GameDirection.STOP
        x = map.s.x
        y = map.s.y
        xFloat = x.toFloat()
        yFloat = y.toFloat()
        footPrints.clear()
        footPrints.add(Point(x, y))
        clearWaitingFlg()
        isRollback = false
        lastDirection = GameDirection.STOP
        whatNext(map, lastDirection)
    }

    interface PlayerCallBack {
        fun changeDirectionCallBack()
        fun onStop()
        fun onDied() {}
    }
}

