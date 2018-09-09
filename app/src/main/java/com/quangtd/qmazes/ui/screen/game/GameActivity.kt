package com.quangtd.qmazes.ui.screen.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.quangtd.qmazes.R
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.util.DialogUtils
import com.quangtd.qmazes.ui.screen.level.LevelActivity
import com.quangtd.qmazes.util.LogUtils
import com.quangtd.qmazes.ui.component.OnSwipeListener
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*


/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class GameActivity : BaseActivity<IGameView, GamePresenter>(), SurfaceHolder.Callback, IGameView {

    private lateinit var mDetector: GestureDetectorCompat
    private var level: Level? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initValues()
        initViews()
        initActions()
    }

    private fun initValues() {
        level = intent.getSerializableExtra(CommonConstants.INTENT_LEVEL) as Level?
        if (level == null) {
            level = Level(1)
        }
    }

    private fun initViews() {
        tvTitle.text = "${level!!.gameKind.nameKind} + ${level!!.id}"
    }


    private fun initActions() {
        mazeView.getSurfaceHolder().addCallback(this)
        mDetector = GestureDetectorCompat(this, object : OnSwipeListener() {
            override fun onSwipe(direction: Direction): Boolean {
                getPresenter(this@GameActivity).move(direction)
                return true
            }
        })
        reload.setOnClickListener {
            getPresenter(this@GameActivity).pauseGame()
            DialogUtils.createConfirmDialog(this, "Reload current game?", "",
                    object : DialogUtils.DialogConfirmCallback {
                        override fun onClickPositiveButton() {
                            getPresenter(this@GameActivity).resumeGame()
                            getPresenter(this@GameActivity).reload()
                        }

                        override fun onClickNegativeButton() {
                            getPresenter(this@GameActivity).resumeGame()
                        }
                    })
        }
        menu.setOnClickListener { onBackPressed() }
    }

    override fun getSurfaceHolder(): SurfaceHolder {
        return mazeView.getSurfaceHolder()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        LogUtils.e("surface Changed")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        LogUtils.e("surfaceDestroyed")
        getPresenter(this).pauseGame()
        isPause = true
    }

    private var isPause = false
    override fun surfaceCreated(p0: SurfaceHolder?) {
        LogUtils.e("surfaceCreated")
        if (!isPause) {
            getPresenter(this@GameActivity).setUpGame(level!!)
        } else {
            getPresenter(this).resumeGame()
        }
    }

    override fun onStop() {
        super.onStop()
        getPresenter(this).pauseGame()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (this.mDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun showWinGameAlert() {
        runOnUiThread {
            uiChangeListener()
            DialogUtils.createAlertDialog(this, "YOU WIN! level ${level!!.id}", CommonConstants.CONGRATE_MESSAGE[Random().nextInt(CommonConstants.CONGRATE_MESSAGE.size)]) {
                level!!.apply {
                    id += 1
                }
                startNewGame(this@GameActivity, level!!)
                getPresenter(this).stopGame()
                finish()
            }
        }
    }

    companion object {
        fun startNewGame(context: Context, level: Level) {
            val intent = Intent(context, GameActivity::class.java)
            if (level.id <= level.gameKind.totalLevel) {
                intent.putExtra(CommonConstants.INTENT_LEVEL, level)
            } else {
                intent.putExtra(CommonConstants.INTENT_LEVEL, Level(1, level.gameKind))
            }
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        LevelActivity.startLevelActivity(this, Category(level!!.gameKind.id, level!!.gameKind))
        finish()
    }


}