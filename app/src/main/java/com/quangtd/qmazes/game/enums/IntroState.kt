package com.quangtd.qmazes.game.enums

import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/8/2018.
 */
enum class IntroState {
    @SerializedName("state_1")
    STATE_1,
    @SerializedName("state_2")
    STATE_2,
    @SerializedName("state_3")
    STATE_3,
    @SerializedName("state_4")
    STATE_4,
    @SerializedName("state_5")
    STATE_5,
    @SerializedName("final")
    FINAL;
}