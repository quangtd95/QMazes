package com.quangtd.qmazes.game.enums

import com.google.gson.annotations.SerializedName

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
enum class GameKind(var id: Int = 0, var nameKind: String, var nameAsset: String, var totalLevel: Int) {
    @SerializedName("classic")
    CLASSIC(0, "classic", "classic", 100),
    @SerializedName("darkness")
    DARKNESS(1, "darkness", "darkness", 100),
    @SerializedName("ice")
    ICE(2, "ice", "ice_floor", 45),
    @SerializedName("trap")
    TRAP(3, "trap", "traps", 100),
    @SerializedName("enemies")
    ENEMIES(4, "enemies", "enemies", 100),
    @SerializedName("time_trial")
    TIME_TRIAL(5, "time_trial", "time_trial", 100);

    companion object {
        fun getGameKindById(id: Int): GameKind? {
            for (gameKind: GameKind in values()) {
                if (gameKind.id == id) {
                    return gameKind
                }
            }
            return null
        }
    }
}