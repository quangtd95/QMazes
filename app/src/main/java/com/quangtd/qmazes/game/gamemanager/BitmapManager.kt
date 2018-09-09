package com.quangtd.qmazes.game.gamemanager

import android.content.Context
import android.graphics.Bitmap
import com.quangtd.qmazes.R
import com.quangtd.qmazes.util.LoadImageUtils

/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
class BitmapManager private constructor() {
    companion object {
        private var ins: BitmapManager? = null

        fun initResource(context: Context): BitmapManager {
            if (ins == null) {
                ins = BitmapManager()
                ins!!.loadResource(context)
            }
            return ins!!
        }

        fun getInstance(): BitmapManager {
            return ins!!
        }
    }

    var door: Array<Bitmap>? = null
    var shuriken: Array<Bitmap>? = null

    fun loadResource(context: Context) {
        if (door == null) {
            door = LoadImageUtils.loadSubImage(context, R.drawable.door, 4, 1)
        }
        if (shuriken == null) {
            shuriken = LoadImageUtils.loadSubImage(context, R.drawable.shuriken, 2, 2)
        }
    }

}