package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import com.google.gson.Gson
import com.quangtd.qmazes.data.model.*
import com.quangtd.qmazes.game.enums.*
import com.quangtd.qmazes.util.ScreenUtils
import java.io.File

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
abstract class AbstractGameManager(var gameKind: GameKind = GameKind.CLASSIC, var level: Int = 1) : GameManager {
    lateinit var gameState: GameState
    lateinit var soundManager: SoundManager
    lateinit var map: MazeMap
    lateinit var player: Player
    lateinit var door: Door
    var widthCell: Float = 0F
    var gameStateCallback: GameState.GameStateCallBack? = null
    var renderCallback: RenderState.RenderCallback? = null

    override fun loadGame(context: Context) {
        forceChangeGameState(GameState.LOADING)
        //load image
        BitmapManager.initResource(context)
        //load sound
        soundManager = SoundManager.getInstance(context)
        //load map
        map = loadMap(context, gameKind, level)
        initGame(context)
        forceChangeGameState(GameState.LOADED)
    }

    open fun initGame(context: Context) {
        widthCell = (ScreenUtils.getWidthScreen(context).toFloat() / map.c)
        map.w.forEach { wall ->
            wall.widthCell = widthCell
            wall.widthWall = widthCell / 7
        }
        map.i.forEach { ice -> ice.widthCell = widthCell }
        map.t.forEach { trap ->
            when {
                trap.d == "d" -> trap.direction = GameDirection.UP
                trap.d == "u" -> trap.direction = GameDirection.DOWN
                trap.d == "r" -> trap.direction = GameDirection.RIGHT
                trap.d == "l" -> trap.direction = GameDirection.LEFT
            }
            trap.map = map
            trap.widthCell = widthCell
        }
        player = when (gameKind) {
            GameKind.ICE -> PlayerIceFloor(map)
            GameKind.TRAP -> PlayerTrap(map)
            else -> Player(map)
        }
        player.widthCell = widthCell
        door = Door(map, player)
        door.widthCell = widthCell
    }

    private fun loadMap(context: Context, gameKind: GameKind, level: Int, randomMap: Boolean = false): MazeMap {
        val inputStream = context.assets.open(gameKind.nameAsset + File.separator + level + ".json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val map = Gson().fromJson(String(buffer), MazeMap::class.java)
        if (randomMap) {
            when (FlipMapType.getRandomType()) {
                FlipMapType.FLIP_H -> flipHMap(map)
                FlipMapType.FLIP_V -> flipVMap(map)
                FlipMapType.FLIP_2_W -> flip2WMap(map)
                FlipMapType.NONE -> {
                    //do nothing
                }
            }
        }
        return map
    }

    private fun flipHMap(map: MazeMap) {
        map.s.x = map.c - 1 - map.s.x
        map.f.x = map.c - 1 - map.f.x
        map.w.forEach {
            it.o.x = map.c - 1 - it.o.x
            it.d.x = map.c - 1 - it.d.x
        }
        map.i.forEach {
            it.x = map.c - (it.x - 0.5F) + 0.5F
        }
    }

    private fun flipVMap(map: MazeMap) {
        map.s.y = map.r - 1 - map.s.y
        map.f.y = map.r - 1 - map.f.y
        map.w.forEach {
            it.o.y = map.r - 1 - it.o.y
            it.d.y = map.r - 1 - it.d.y
        }
        map.i.forEach {
            it.y = map.r - (it.y - 0.5F) + 0.5F
        }
    }

    private fun flip2WMap(map: MazeMap) {
        map.s.x = map.c - 1 - map.s.x
        map.f.x = map.c - 1 - map.f.x
        map.s.y = map.r - 1 - map.s.y
        map.f.y = map.r - 1 - map.f.y
        map.w.forEach {
            it.o.x = map.c - 1 - it.o.x
            it.d.x = map.c - 1 - it.d.x
            it.o.y = map.r - 1 - it.o.y
            it.d.y = map.r - 1 - it.d.y
        }
        map.i.forEach {
            it.x = map.c - (it.x - 0.5F) + 0.5F
            it.y = map.r - (it.y - 0.5F) + 0.5F
        }
    }

    override fun reload() {
        player.reload()
        renderCallback?.changeRenderState(RenderState.REQUEST_RENDER)
        forceChangeGameState(GameState.INTRO)
    }

    override fun getPlayerObject(): Player = player

    override fun getDoorObject(): Door = door

    override fun bindRenderCallback(renderCallBack: RenderState.RenderCallback) {
        this.renderCallback = renderCallBack
    }

    override fun bindGameStateCallback(gameStateCallBack: GameState.GameStateCallBack) {
        this@AbstractGameManager.gameStateCallback = gameStateCallBack
    }

    override fun forceChangeGameState(gameState: GameState) {
        this.gameState = gameState
        gameStateCallback?.onGameStateChangeCallback(gameState)
    }

    override fun getMazeMap(): MazeMap = map

    override fun action(direction: GameDirection) {
        player.changeDirection(direction)
    }
}