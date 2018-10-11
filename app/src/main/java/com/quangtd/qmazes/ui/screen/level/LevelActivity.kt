package com.quangtd.qmazes.ui.screen.level

//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.reward.RewardItem
//import com.google.android.gms.ads.reward.RewardedVideoAd
//import com.google.android.gms.ads.reward.RewardedVideoAdListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.quangtd.qmazes.R
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.ui.screen.category.CategoryActivity
import com.quangtd.qmazes.ui.screen.game.GameActivity
import com.quangtd.qmazes.util.ColorUtils
import com.quangtd.qmazes.util.DialogUtils
import com.quangtd.qmazes.util.RecyclerViewUtils
import com.quangtd.qstudio.mvpbase.IAdapterView
import kotlinx.android.synthetic.main.activity_level.*

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class LevelActivity : BaseActivity<ILevelView, LevelPresenter>(), LevelAdapter.OnLevelClickListener, ILevelView/*, RewardedVideoAdListener*/ {

    private lateinit var mLevelAdapter: IAdapterView
    private var gameKind: GameKind? = null
//    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private var mSelectedLevel: Level? = null
    private var mIsCanUnlock: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        gameKind = intent.getSerializableExtra("gameKind") as GameKind?
        if (gameKind == null) {
            gameKind = GameKind.CLASSIC
        }
        initViews()
        initActions()
        loadValues(gameKind!!)
        initAds()
    }

    private fun initAds() {
        // Use an activity context to get the rewarded video instance.
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
//        mRewardedVideoAd.rewardedVideoAdListener = this
    }

    private fun loadValues(gameKind: GameKind) {
        getPresenter(this).loadData(gameKind)
        mLevelAdapter.refresh()
    }

    private fun initActions() {
        menu.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        CategoryActivity.startCategoryActivity(this@LevelActivity)
        finish()
        super.onBackPressed()
    }

    private fun initViews() {
        val levelAdapter = LevelAdapter(this)
        levelAdapter.onLevelClickListener = this
        RecyclerViewUtils.getInstance().setUpGridVertical(this, rvLevel, 5)
        mLevelAdapter = levelAdapter
        getPresenter(this).adapter = levelAdapter
        rvLevel.adapter = levelAdapter
        tvTitle.text = gameKind!!.nameKind
        when (gameKind!!) {
            GameKind.ICE -> tvTitle.setTextColor(ColorUtils.colorIce)
            GameKind.DARKNESS -> tvTitle.setTextColor(ColorUtils.colorDark)
            GameKind.CLASSIC -> tvTitle.setTextColor(ColorUtils.colorGreen)
            GameKind.TRAP -> tvTitle.setTextColor(ColorUtils.colorTrap)
            GameKind.TIME_TRIAL -> tvTitle.setTextColor(ColorUtils.colorTimeTrial)
            else -> tvTitle.setTextColor(ColorUtils.colorOrange)
        }

    }

    override fun onClickLevel(level: Level) {
        mSelectedLevel = level
        if (mSelectedLevel!!.isUnLocked) {
            GameActivity.startNewGame(this, mSelectedLevel!!)
            finish()
        } else {
            uiChangeListener()
//            if (getPresenter(this).canRequestUnlock(level)) {
            DialogUtils.showUnlock(this)
//            } else {
//                DialogUtils.showNotify(this, "Please unlock or complete the previous level first!") {}
//            }
        }
    }

    private fun loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd(getString(R.string.admod_unit_id)/*"ca-app-pub-3940256099942544/5224354917"*/,
//                AdRequest.Builder().build())
    }

//    override fun onRewarded(reward: RewardItem) {
//        Toast.makeText(this, "onRewarded! currency: ${reward.type} amount: ${reward.amount}",
//                Toast.LENGTH_SHORT).show()
//        // Reward the user.
//        mIsCanUnlock = true
//    }

//    override fun onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show()
//    }

//    override fun onRewardedVideoAdClosed() {
//        if (mIsCanUnlock) {
//            mIsCanUnlock = false
//            getPresenter(this).unlockLevel(mSelectedLevel)
//            mLevelAdapter.refresh()
//            uiChangeListener()
//            DialogUtils.showUnlockComplete(this) {
//                GameActivity.startNewGame(this, mSelectedLevel!!)
//                finish()
//            }
//        }
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
//    }

//    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
//        uiChangeListener()
//        DialogUtils.hideLoadingDialog()
//        DialogUtils.showNotify(this, "Sorry, try again later") {}
//        LogUtils.e("onRewardedVideoAdFailedToLoad")
//    }

//    override fun onRewardedVideoAdLoaded() {
//        if (mRewardedVideoAd.isLoaded) {
//            mRewardedVideoAd.show()
//        }
//        uiChangeListener()
//        DialogUtils.hideLoadingDialog()
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show()
//    }

//    override fun onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRewardedVideoCompleted() {
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show()
//    }

    override fun onPause() {
        super.onPause()
//        mRewardedVideoAd.pause(this)
    }

    override fun onResume() {
        super.onResume()
//        mRewardedVideoAd.resume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
//        mRewardedVideoAd.destroy(this)
    }

    companion object {
        fun startLevelActivity(context: Context, category: Category) {
            val intent = Intent(context, LevelActivity::class.java)
            intent.putExtra("gameKind", category.gameKind)
            context.startActivity(intent)
        }
    }
}