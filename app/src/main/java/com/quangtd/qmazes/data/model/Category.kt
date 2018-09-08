package com.quangtd.qmazes.data.model

import com.quangtd.qmazes.game.GameKind
import com.quangtd.qmazes.mvpbase.BaseModel
import java.io.Serializable

/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
data class Category(var id: Int, var gameKind: GameKind) : BaseModel, Serializable