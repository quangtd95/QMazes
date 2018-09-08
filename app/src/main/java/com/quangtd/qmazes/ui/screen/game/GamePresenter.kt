package com.quangtd.qmazes.ui.screen.game

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangtd.qmazes.data.model.GameDirection
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.*
import com.quangtd.qmazes.mvpbase.DialogUtils
import com.quangtd.qmazes.util.LogUtils
import com.quangtd.qmazes.ui.component.OnSwipeListener
import com.quangtd.qmazes.util.SharedPreferencesUtils
import com.quangtd.qstudio.mvpbase.BasePresenter
import java.lang.reflect.Type

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class GamePresenter : BasePresenter<IGameView>(), GameState.GameStateCallBack, RenderState.RenderCallback {

    private var pref: SharedPreferencesUtils? = null
    private var gameManager: GameManager? = null
    private var gameInterface: GameInterface? = null
    private lateinit var gameThread: GameThread
    private var view: IGameView? = null
    private var setupGameFinish = false
    private lateinit var level: Level
    override fun onInit() {
        view = getIView()
    }

    fun setUpGame(level: Level) {
        this.level = level
        gameManager = MazeClassicManager(level.id, gameKind = level.gameKind)
        gameInterface = when (level.gameKind) {
            GameKind.CLASSIC -> {
                MazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.DARKNESS -> {
                DarknessMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.ICE -> {
                IceFloorMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
        }

        gameManager!!.bindGameStateCallback(this)
        gameManager!!.bindRenderCallback(this)
        gameInterface!!.bindRenderCalBack(this)

        gameManager!!.loadGame(getContext()!!)

        gameThread = GameThread(gameManager!!, gameInterface!!)
        gameThread.start()
        setupGameFinish = true
        pref = SharedPreferencesUtils.getInstance(getContext())
    }

    fun move(direction: OnSwipeListener.Direction) {
        resumeGame()
        when (direction) {
            OnSwipeListener.Direction.up -> gameManager?.action(GameDirection.DOWN)
            OnSwipeListener.Direction.down -> gameManager?.action(GameDirection.UP)
            OnSwipeListener.Direction.left -> gameManager?.action(GameDirection.LEFT)
            OnSwipeListener.Direction.right -> gameManager?.action(GameDirection.RIGHT)
        }
    }

    private fun updateData() {
        val levelJSON = pref?.getString(level.gameKind.nameKind)
        var lstLevel = ArrayList<Level>()
        if (levelJSON == null) {
            DialogUtils.createAlertDialog(getContext(), "ERROR", "cannot write data, you can play game normally but your data will be lost after exit!")
        } else {
            val listType: Type = object : TypeToken<ArrayList<Level>>() {}.type
            lstLevel = Gson().fromJson(levelJSON, listType)
            lstLevel[level.id - 1].apply {
                isComplete = true
            }
            if (level.id < level.gameKind.totalLevel) {
                lstLevel[level.id].apply {
                    isUnLocked = true
                }
            }
        }
        pref?.setString(level.gameKind.nameKind, Gson().toJson(lstLevel))
    }

    fun resumeGame() {
        if (setupGameFinish) {
            gameThread.renderFlg = true
        }
    }

    fun pauseGame() {
        if (setupGameFinish) {
            gameThread.renderFlg = false
        }
    }

    fun stopGame() {
        gameThread.stopFlg = true
    }

    fun reload() {
        gameManager?.reload()
    }

    override fun changeRenderState(renderState: RenderState) {
        when (renderState) {
            RenderState.REQUEST_RENDER -> {
                resumeGame()
            }
            RenderState.STOP_RENDER -> {
                pauseGame()
            }
        }
    }

    override fun onGameStateChangeCallback(gameState: GameState) {
        LogUtils.e(gameState.name)
        gameInterface?.setState(gameState)
        when (gameState) {
            GameState.LOADING -> {
            }
            GameState.LOADED -> {
                gameInterface!!.loadGameUI(object : MazePanel.LoadGameUICallBack {
                    override fun onLoadedUI() {
                        gameManager!!.forceChangeGameState(GameState.INTRO)
                    }
                })
            }
            GameState.INTRO -> {
                gameInterface!!.resetValue()
            }
            GameState.PLAYING -> {
            }
            GameState.PAUSE -> {
            }
            GameState.STOP -> {
            }
            GameState.WIN_GAME -> {
                updateData()
                view?.showWinGameAlert()
                stopGame()
            }
        }
    }
}