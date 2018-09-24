package com.quangtd.qmazes.ui.maze

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by quang.td95@gmail.com
 * on 9/1/2018.
 */
class MazeView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs) {

    init {
        init()
    }

    private fun init() {

    }

    fun drawGame() {
        val canvas = holder.lockCanvas()
        canvas.drawColor(Color.GREEN)
        holder.unlockCanvasAndPost(canvas)
    }

    fun refresh() {
        drawGame()
    }

    fun getSurfaceHolder(): SurfaceHolder {
        return holder
    }
}