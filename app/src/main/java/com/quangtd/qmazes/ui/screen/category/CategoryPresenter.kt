package com.quangtd.qmazes.ui.screen.category

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.GameKind
import com.quangtd.qmazes.util.SharedPreferencesUtils
import com.quangtd.qstudio.mvpbase.BasePresenter
import com.quangtd.qstudio.mvpbase.IAdapterData
import java.lang.reflect.Type


/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class CategoryPresenter : BasePresenter<ICategoryView>() {
    private var pref: SharedPreferencesUtils? = null
    var adapter: IAdapterData<Category>? = null

    override fun onInit() {
        pref = SharedPreferencesUtils.getInstance(getContext())
    }

    fun loadData() {
        val lstCategory = GameKind.values().map { Category(it.id, it) }
        adapter?.setItems(lstCategory)
    }
}