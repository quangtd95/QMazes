package com.quangtd.qmazes.game.enums

import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
enum class GameKind(var id: Int = 0, var nameKind: String, var nameAsset: String, var totalLevel: Int) {
    @SerializedName("classic")
    CLASSIC(0, "CLASSIC", "classic", 100),
    @SerializedName("darkness")
    DARKNESS(1, "DARKNESS", "darkness", 100),
    @SerializedName("ice")
    ICE(2, "ICE", "ice_floor", 45),
    @SerializedName("trap")
    TRAP(3, "TRAP", "traps", 100),
    @SerializedName("enemies")
    ENEMIES(4, "ENEMIES", "enemies", 100),
    @SerializedName("time_trial")
    TIME_TRIAL(5, "TIME_TRIAL", "time_trial", 100)
}