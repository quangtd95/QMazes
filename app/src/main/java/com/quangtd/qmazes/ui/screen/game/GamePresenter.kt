package com.quangtd.qmazes.ui.screen.game

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangtd.qmazes.game.enums.GameDirection
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.game.enums.GameState
import com.quangtd.qmazes.game.enums.RenderState
import com.quangtd.qmazes.game.gamemanager.*
import com.quangtd.qmazes.game.gameview.*
import com.quangtd.qmazes.game.thread.GameThread
import com.quangtd.qmazes.util.DialogUtils
import com.quangtd.qmazes.util.LogUtils
import com.quangtd.qmazes.ui.component.OnSwipeListener
import com.quangtd.qmazes.util.SharedPreferencesUtils
import com.quangtd.qstudio.mvpbase.BasePresenter
import java.lang.reflect.Type
import java.sql.Time

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class GamePresenter : BasePresenter<IGameView>(), GameState.GameStateCallBack, RenderState.RenderCallback, TimeTrialGameManager.OnCountingTimeCallback {

    private var pref: SharedPreferencesUtils? = null
    private var gameManager: GameManager? = null
    private var gamePanel: GamePanel? = null
    private lateinit var gameThread: GameThread
    private var view: IGameView? = null
    private var setupGameFinish = false
    private lateinit var level: Level
    override fun onInit() {
        view = getIView()
    }

    fun setUpGame(level: Level) {
        this.level = level
        when (level.gameKind) {
            GameKind.CLASSIC -> {
                gameManager = MazeClassicManager(level.id, gameKind = GameKind.CLASSIC)
                gamePanel = MazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.DARKNESS -> {
                gameManager = MazeClassicManager(level.id, gameKind = GameKind.DARKNESS)
                gamePanel = DarknessMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.ICE -> {
                gameManager = MazeClassicManager(level.id, gameKind = GameKind.ICE)
                gamePanel = IceFloorMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.TRAP -> {
                gameManager = TrapsGameManager(level.id, gameKind = GameKind.TRAP)
                gamePanel = TrapsMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.ENEMIES -> {
                gameManager = EnemyGameManager(level.id, gameKind = GameKind.ENEMIES)
                gamePanel = EnemyMazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
            }
            GameKind.TIME_TRIAL -> {
                gameManager = TimeTrialGameManager(level.id, gameKind = GameKind.TIME_TRIAL)
                gamePanel = MazePanel(getContext()!!, gameManager!!, view!!.getSurfaceHolder())
                (gameManager as TimeTrialGameManager).onCountingTimeCallback = this
            }
        }

        gameManager!!.bindGameStateCallback(this)
        gameManager!!.bindRenderCallback(this)
        gamePanel!!.bindRenderCalBack(this)

        gameManager!!.loadGame(getContext()!!)

        gameThread = GameThread(gameManager!!, gamePanel!!)
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
            if (level.gameKind == GameKind.TIME_TRIAL) {
                (gameManager!! as TimeTrialGameManager).playBackgroundSound()
            }
            gameManager!!.resetStartGameTime()
            gameThread.renderFlg = true
        }
    }

    fun pauseGame() {
        if (setupGameFinish) {
            if (level.gameKind == GameKind.TIME_TRIAL) {
                (gameManager!! as TimeTrialGameManager).stopBackgroundSound()
            }
            gameThread.renderFlg = false
        }
    }

    fun stopGame() {
        if (level.gameKind == GameKind.TIME_TRIAL) {
            (gameManager!! as TimeTrialGameManager).stopBackgroundSound()
        }
        gameThread.stopFlg = true
    }

    fun reload() {
        gameManager!!.resetStartGameTime()
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
        gamePanel?.setState(gameState)
        when (gameState) {
            GameState.LOADING -> {
            }
            GameState.LOADED -> {
                gamePanel!!.loadGameUI(object : MazePanel.LoadGameUICallBack {
                    override fun onLoadedUI() {
                        gameManager!!.forceChangeGameState(GameState.INTRO)
                    }
                })
            }
            GameState.INTRO -> {
                gamePanel!!.resetValue()
            }
            GameState.PLAYING -> {
                if (level.gameKind == GameKind.TIME_TRIAL) {
                    (gameManager!! as TimeTrialGameManager).playBackgroundSound()
                }
                gameManager!!.resetStartGameTime()
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
            GameState.LOSE_GAME -> {
                view?.showLoseGameAlert()
            }
        }
    }

    override fun onRemainingTime(secondRemaining: Int) {
        view!!.updateRemainingTime(secondRemaining)
    }

    fun canNext(): Boolean {
        val levelJSON = pref?.getString(level.gameKind.nameKind)
        val lstLevel: ArrayList<Level>
        if (levelJSON == null) {
            return false
        } else {
            val listType: Type = object : TypeToken<ArrayList<Level>>() {}.type
            lstLevel = Gson().fromJson(levelJSON, listType)
            lstLevel.forEach {
                if (it.id == (level.id + 1)) {
                    return it.isUnLocked
                }
            }
        }
        return false
    }
}