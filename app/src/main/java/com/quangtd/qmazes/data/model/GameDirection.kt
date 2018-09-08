package com.quangtd.qmazes.data.model

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
enum class GameDirection(var id: Int, var nameDirection: String) {
    UP(0, "up"),
    RIGHT(1, "right"),
    DOWN(2, "down"),
    LEFT(3, "left"),
    STOP(5, "stop");
}