package com.quangtd.qmazes.ui.screen.level

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.quangtd.qmazes.R
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.ui.screen.game.GameActivity
import com.quangtd.qmazes.util.RecyclerViewUtils
import com.quangtd.qstudio.mvpbase.IAdapterView
import kotlinx.android.synthetic.main.activity_level.*

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class LevelActivity : BaseActivity<ILevelView, LevelPresenter>(), LevelAdapter.OnLevelClickListener, ILevelView {

    private lateinit var mLevelAdapter: IAdapterView
    private var gameKind: GameKind? = null
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
    }

    private fun loadValues(gameKind: GameKind) {
        getPresenter(this).loadData(gameKind)
        mLevelAdapter.refresh()
    }

    private fun initActions() {
        menu.setOnClickListener { finish() }
    }

    private fun initViews() {
        val levelAdapter = LevelAdapter(this)
        levelAdapter.onLevelClickListener = this
        RecyclerViewUtils.getInstance().setUpGridVertical(this, rvLevel, 5)
        mLevelAdapter = levelAdapter
        getPresenter(this).adapter = levelAdapter
        rvLevel.adapter = levelAdapter
        tvTitle.text = gameKind!!.nameKind
    }

    override fun onClickLevel(level: Level) {
        if (level.isUnLocked) {
            GameActivity.startNewGame(this, level)
            finish()
        }
    }

    companion object {
        fun startLevelActivity(context: Context, category: Category) {
            val intent = Intent(context, LevelActivity::class.java)
            intent.putExtra("gameKind", category.gameKind)
            context.startActivity(intent)
        }
    }
}