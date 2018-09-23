package com.quangtd.qmazes.ui.screen.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.quangtd.qmazes.R
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.ui.screen.level.LevelActivity
import com.quangtd.qmazes.util.RecyclerViewUtils
import com.quangtd.qstudio.mvpbase.IAdapterView
import kotlinx.android.synthetic.main.activity_level.*

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class CategoryActivity : BaseActivity<ICategoryView, CategoryPresenter>(), CategoryAdapter.OnCategoryClickListener, ICategoryView {

    private lateinit var mCategoryAdapter: IAdapterView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        initViews()
        initActions()
        loadValues()
    }

    private fun loadValues() {
        getPresenter(this).loadData()
        mCategoryAdapter.refresh()
    }

    private fun initActions() {
        menu.setOnClickListener { finish() }
    }

    private fun initViews() {
        val categoryAdapter = CategoryAdapter(this)
        categoryAdapter.onCategoryClickListener = this
        RecyclerViewUtils.getInstance().setUpGridVertical(this, rvLevel, 1)
        mCategoryAdapter = categoryAdapter
        getPresenter(this).adapter = categoryAdapter
        rvLevel.adapter = categoryAdapter
        tvTitle.text = "Category"
    }

    override fun onClickCategory(category: Category) {
        LevelActivity.startLevelActivity(this, category)
        finish()
    }

    companion object {
        fun startCategoryActivity(context: Context) {
            val intent = Intent(context, CategoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}