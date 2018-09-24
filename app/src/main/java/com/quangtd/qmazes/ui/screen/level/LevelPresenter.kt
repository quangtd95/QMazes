package com.quangtd.qmazes.ui.screen.level

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.data.model.Level
import com.quangtd.qmazes.game.enums.GameKind
import com.quangtd.qmazes.util.SharedPreferencesUtils
import com.quangtd.qmazes.mvpbase.BasePresenter
import com.quangtd.qstudio.mvpbase.IAdapterData
import java.lang.reflect.Type


/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class LevelPresenter : BasePresenter<ILevelView>() {
    private var pref: SharedPreferencesUtils? = null
    var adapter: IAdapterData<Level>? = null
    override fun onInit() {
        pref = SharedPreferencesUtils.getInstance(getContext())
    }

    fun loadData(gameKind: GameKind) {
        val levelJSON = pref?.getString(gameKind.nameKind)
        var numberComplete: Int = 0
        var lstLevel = ArrayList<Level>()
        if (levelJSON == null) {
            for (i in 1..gameKind.totalLevel) {
                lstLevel.add(Level(i, gameKind = gameKind))
            }
            lstLevel[0].isUnLocked = true
            val levelString = Gson().toJson(lstLevel)
            pref?.setString(gameKind.nameKind, levelString)
        } else {
            val listType: Type = object : TypeToken<ArrayList<Level>>() {}.type
            lstLevel = Gson().fromJson(levelJSON, listType)
            lstLevel.forEach {
                if (it.isComplete) numberComplete++
            }
        }
        pref?.setInt(String.format(CommonConstants.COMPLETE_PRX, gameKind.nameKind), numberComplete)
        adapter?.setItems(lstLevel)
    }
}