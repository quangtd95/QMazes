package com.quangtd.qmazes.game.enums

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
enum class GameKind(var id: Int = 0, var nameKind: String, var nameAsset: String, var totalLevel: Int) {
    CLASSIC(0, "CLASSIC", "classic", 100),
    DARKNESS(1, "DARKNESS", "darkness", 100),
    ICE(2, "ICE", "ice_floor", 45),
    TRAP(3, "TRAP", "traps", 100)
}