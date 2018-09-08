package com.quangtd.qmazes.game

/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
enum class RenderState {
    REQUEST_RENDER,
    STOP_RENDER;

    interface RenderCallback {
        fun changeRenderState(renderState: RenderState)
    }
}