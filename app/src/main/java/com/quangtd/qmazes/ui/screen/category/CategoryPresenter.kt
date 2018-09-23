package com.quangtd.qmazes.ui.screen.category

import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.util.SharedPreferencesUtils
import com.quangtd.qstudio.mvpbase.BasePresenter
import com.quangtd.qstudio.mvpbase.IAdapterData


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

        val lstCategory = GameKind.values().map {
            val numberOfComplete = pref?.getInt(String.format(CommonConstants.COMPLETE_PRX, it.nameKind))!!
            Category(it.id, it, if (numberOfComplete == -1) 0 else numberOfComplete)
        }
        adapter?.setItems(lstCategory)
    }
}