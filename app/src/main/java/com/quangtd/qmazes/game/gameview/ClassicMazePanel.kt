package com.quangtd.qmazes.game.gameview

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import com.quangtd.qmazes.data.model.Door
import com.quangtd.qmazes.data.model.MazeMap
import com.quangtd.qmazes.data.model.Player
import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.game.enums.RenderState
import com.quangtd.qmazes.game.gamemanager.GameManager
import com.quangtd.qmazes.util.ScreenUtils

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
open class ClassicMazePanel(var context: Context, var gameManager: GameManager,
                            var viewHolder: SurfaceHolder) : GamePanel {

    protected var renderCallback: RenderState.RenderCallback? = null
    protected var widthScreen: Int = 0
    protected var widthCell: Float = 0F
    protected var widthWall: Float = 0F
    protected var gameState = GameState.LOADING
    protected var tempBackgroundBitmap: Bitmap? = null
    lateinit var map: MazeMap
    lateinit var player: Player
    lateinit var door: Door

    override fun loadGameUI(loadedGameUICallBack: LoadGameUICallBack?) {
        widthScreen = ScreenUtils.getWidthScreen(context)
        widthCell = (ScreenUtils.getWidthScreen(context).toFloat() / gameManager.getMazeMap().c)
        map = gameManager.getMazeMap()
        player = gameManager.getPlayerObject()
        door = gameManager.getDoorObject()
        resetValue()
        loadedGameUICallBack?.onLoadedUI()
    }

    override fun resetValue() {
        widthWall = widthCell / 7
        currentIntroBackground = widthCell * map.r
        velocityIntro = widthCell * map.r / 20
    }


    protected lateinit var canvas: Canvas
    protected var colorBound: Int = Color.WHITE
        set(value) {
            paintBound.color = value
            field = value
        }
    protected var colorDoor: Int = Color.rgb(75, 174, 74)
        set(value) {
            paintDoor.color = value
            field = value
        }
    protected var colorWall: Int = Color.WHITE
        set(value) {
            paintWall.color = value
            field = value
        }
    protected var colorPlayer: Int = Color.rgb(74, 174, 74)
        set(value) {
            paintPlayer.color = value
            field = value
        }
    protected var colorBackground: Int = Color.rgb(43, 34, 34)

    private var paintWall = Paint().apply {
        color = colorWall
        flags = Paint.ANTI_ALIAS_FLAG
        strokeCap = Paint.Cap.ROUND
    }
    private var paintPlayer = Paint().apply {
        color = colorPlayer
        flags = Paint.ANTI_ALIAS_FLAG
        strokeCap = Paint.Cap.ROUND

    }
    private var paintDoor = Paint().apply {
        color = colorDoor
        style = Paint.Style.STROKE
        strokeWidth = widthWall
        flags = Paint.ANTI_ALIAS_FLAG
        strokeCap = Paint.Cap.ROUND
    }
    protected var paintBound = Paint().apply {
        color = colorBound
        flags = Paint.ANTI_ALIAS_FLAG
        strokeWidth = widthWall
    }

    override fun setState(gameState: GameState) {
        this.gameState = gameState
    }

    override fun bindRenderCalBack(renderCallBack: RenderState.RenderCallback) {
        this.renderCallback = renderCallBack
    }

    override fun draw() {
        try {
            canvas = viewHolder.lockCanvas()
            canvas.let {
                drawEvery()
                viewHolder.unlockCanvasAndPost(canvas)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun drawEvery() {
        when (gameState) {
            GameState.LOADED -> {
            }
            GameState.INTRO -> {
                drawIntro()
            }
            GameState.LOADING, GameState.PLAYING, GameState.PAUSE, GameState.STOP, GameState.WIN_GAME -> {
                drawGame()
            }
            GameState.LOSE_GAME -> {
            }
        }
    }

    private var velocityIntro = 0F
    protected var currentIntroBackground = 0F
    private var tempPaintIntro = Paint().apply {
        color = Color.BLACK
    }
    private var tempRectIntro = Rect()

    open fun drawIntro() {
        drawGame()
        drawIntroBackground()
        checkFinishIntro()
    }

    protected fun drawIntroBackground() {
        tempRectIntro.set(0, 0, widthScreen, currentIntroBackground.toInt())
        canvas.drawRect(tempRectIntro, tempPaintIntro)
        currentIntroBackground -= velocityIntro
        if (currentIntroBackground <= 0) currentIntroBackground = 0F
    }

    open fun checkFinishIntro() {
        if (currentIntroBackground <= 0) {
            gameManager.forceChangeGameState(GameState.PLAYING)
        }
    }

    open fun drawGame(isBuffer: Boolean = true) {
        drawBackground(canvas)
        drawMaze(isBuffer)
        drawPlayer()
        drawDoor()
    }

    private fun drawMaze(isBuffer: Boolean) {
        if (isBuffer) {
            if (tempBackgroundBitmap == null) {
                tempBackgroundBitmap = Bitmap.createBitmap((widthCell * map.c).toInt(), (widthCell * map.r).toInt(), Bitmap.Config.ARGB_8888)
                val tempCanvasBackground = Canvas(tempBackgroundBitmap)
                drawBackground(tempCanvasBackground)
                drawBound(tempCanvasBackground)
                drawIce(tempCanvasBackground)
                drawWall(tempCanvasBackground)
            } else {
                canvas.drawBitmap(tempBackgroundBitmap, 0F, 0F, paintWall)
            }
        } else {
            drawBackground(canvas)
            drawBound(canvas)
            drawIce(canvas)
            drawWall(canvas)
        }
    }

    override fun loadBufferAsync(loadedGameUICallBack: LoadGameUICallBack?) {
        Thread {
            if (tempBackgroundBitmap == null) {
                tempBackgroundBitmap = Bitmap.createBitmap((widthCell * map.c).toInt(), (widthCell * map.r).toInt(), Bitmap.Config.ARGB_8888)
            }
            synchronized(tempBackgroundBitmap!!) {
                val tempCanvasBackground = Canvas(tempBackgroundBitmap)
                drawBackground(tempCanvasBackground)
                drawBound(tempCanvasBackground)
                drawIce(tempCanvasBackground)
                drawWall(tempCanvasBackground)
            }
            loadedGameUICallBack?.onLoadedUI()
        }.start()
    }


    open fun drawIce(tempCanvasBackground: Canvas) {}

    open fun drawBackground(canvas: Canvas) {
        canvas.drawColor(colorBackground)
    }

    open fun drawBound(canvas: Canvas) {
        canvas.drawRect(0F, 0F, widthCell * map.c, widthWall, paintBound)
        canvas.drawRect(0F, widthCell * map.r, widthCell * map.c, widthCell * map.r + widthWall, paintBound)
        canvas.drawRect(0F, 0F, widthWall, widthCell * map.r + widthWall, paintBound)
        canvas.drawRect(widthCell * map.c - widthWall, 0F, widthCell * map.c, widthCell * map.r + widthWall, paintBound)
    }

    open fun drawWall(canvas: Canvas) {
        map.w.forEach { it.draw(canvas, paintWall) }
    }

    open fun drawPlayer() {
        player.draw(canvas, paintPlayer)
    }

    open fun drawDoor() {
        paintDoor.strokeWidth = widthWall
        door.draw(canvas, paintDoor)
    }

    interface LoadGameUICallBack {
        fun onLoadedUI()
    }

    override fun getHeight(): Int {
        return (map.r * widthCell).toInt()
    }

}