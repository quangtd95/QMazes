package com.quangtd.qmazes.data.model

import com.quangtd.qmazes.game.GameKind
import com.quangtd.qmazes.mvpbase.BaseModel
import java.io.Serializable

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
data class Level(var id: Int, var gameKind: GameKind = GameKind.CLASSIC, var isComplete: Boolean = false, var isUnLocked: Boolean = false) : BaseModel, Serializable