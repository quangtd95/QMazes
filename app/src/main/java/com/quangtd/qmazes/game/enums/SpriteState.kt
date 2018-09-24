package com.quangtd.qmazes.game.enums

import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/9/2018.
 */
enum class SpriteState {
    @SerializedName("normal")
    NORMAL,
    @SerializedName("destroying")
    DESTROYING,
    @SerializedName("died")
    DIED
}