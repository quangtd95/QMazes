package com.quangtd.qmazes.game

import android.content.Context
import android.graphics.Bitmap
import com.quangtd.qmazes.R
import com.quangtd.qmazes.util.LoadImageUtils

/**
 * Created by quang.td95@gmail.com
 * on 9/3/2018.
 */
class ResourceManager private constructor() {
    companion object {
        private var ins: ResourceManager? = null

        fun initResource(context: Context): ResourceManager {
            if (ins == null) {
                ins = ResourceManager()
                ins!!.loadResource(context)
            }
            return ins!!
        }

        fun getInstance(): ResourceManager {
            return ins!!
        }
    }

    var door: Array<Bitmap>? = null

    fun loadResource(context: Context) {
        if (door == null) {
            door = LoadImageUtils.loadSubImage(context, R.drawable.door, 4, 1)
        }
    }

}