package com.quangtd.qmazes.ui.screen.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdSize
//import com.google.android.gms.ads.MobileAds
import com.quangtd.qmazes.R
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.ui.component.OnSwipeListener
import com.quangtd.qmazes.ui.screen.level.LevelActivity
import com.quangtd.qmazes.util.ColorUtils
import com.quangtd.qmazes.util.DialogUtils
import com.quangtd.qmazes.util.LogUtils
import com.quangtd.qmazes.util.ScreenUtils
import kotlinx.android.synthetic.main.activity_game.*


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
        if (level!!.gameKind != GameKind.TIME_TRIAL) {
            tvTitle.text = "${level!!.gameKind.nameKind} + ${level!!.id}"
        } else {
            tvTitle.setTextColor(ColorUtils.colorTimeTrial)
        }
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
            uiChangeListener()
            DialogUtils.showReload(this,
                    {
                        getPresenter(this@GameActivity).resumeGame()
                        getPresenter(this@GameActivity).reload()
                    },
                    { getPresenter(this@GameActivity).resumeGame() }
            )
        }
        menu.setOnClickListener { onBackPressed() }
//        adView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                LogUtils.e("onAdLoaded")
//            }
//
//            override fun onAdFailedToLoad(errorCode: Int) {
//                // Code to be executed when an ad request fails.
//                LogUtils.e("onAdFailedToLoad $errorCode")
//
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//                LogUtils.e("onAdOpened")
//
//            }
//
//            override fun onAdLeftApplication() {
//                // Code to be executed when the user has left the app.\
//                LogUtils.e("onAdLeftApplication")
//
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//                LogUtils.e("onAdClosed")
//            }
//        }
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
            if (getPresenter(this).canNext()) {
                next.visibility = View.VISIBLE
                next.setOnClickListener {
                    moveNextLevel()
                }
            } else {
                next.visibility = View.INVISIBLE
            }
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
            next.visibility = View.VISIBLE
            next.setOnClickListener {
                moveNextLevel()
            }
            DialogUtils.showWin(this, getString(R.string.you_win) + level!!.id) {
                moveNextLevel()
            }
        }
    }

    override fun showLoseGameAlert() {
        runOnUiThread {
            getPresenter(this).pauseGame()
            uiChangeListener()
            DialogUtils.showTimeUp(this) {
                getPresenter(this@GameActivity).resumeGame()
                getPresenter(this@GameActivity).reload()
            }
        }
    }

    private fun moveNextLevel() {
        level!!.apply {
            id += 1
        }
        GameActivity.startNewGame(this@GameActivity, level!!)
        getPresenter(this).stopGame()
        finish()
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
        uiChangeListener()
        getPresenter(this@GameActivity).pauseGame()
        DialogUtils.showExitConfirm(this@GameActivity, {
            LevelActivity.startLevelActivity(this@GameActivity, Category(level!!.gameKind.id, level!!.gameKind))
            finish()
        }, {
            getPresenter(this@GameActivity).resumeGame()
        })

    }

    override fun updateRemainingTime(secondRemaining: Int) {
        runOnUiThread {
            tvTitle.text = secondRemaining.toString()
        }
    }

    override fun onDestroy() {
        getPresenter(this).stopGame()
        super.onDestroy()
    }

    override fun getSurfaceHeight(): Int {
        return ScreenUtils.getHeightScreen(this) - tvTitle.height - ScreenUtils.convertDpToPixel(this, 17)
    }

    override fun showNewCategoryAlert() {
        uiChangeListener()
        runOnUiThread {
            DialogUtils.showUnlockCategory(this)
        }
    }

    override fun setHeightGame(height: Int) {
        val sfHeight = getSurfaceHeight()
        val margin = (sfHeight - height) / 2
        val params = mazeView.layoutParams as MarginLayoutParams
        params.topMargin = margin
        params.bottomMargin = margin
        mazeView.layoutParams = params
        mazeView.invalidate()
//        if (margin > AdSize.BANNER.height) {
//            initAdMod()
//        } else {
//            LogUtils.e("size not compatible : $margin and ${AdSize.BANNER.height}")
//        }
    }

//    private fun initAdMod() {
//        MobileAds.initialize(this, getString(R.string.admod_id))
//        adView.visibility = View.VISIBLE
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)
//        if (adRequest.isTestDevice(this)) {
//            LogUtils.e("test device")
//        } else {
//            LogUtils.e("not test device")
//        }
//    }
}