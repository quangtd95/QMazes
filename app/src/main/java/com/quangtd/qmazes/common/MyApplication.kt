package com.quangtd.qmazes.common

import android.app.Application
//import com.crashlytics.android.Crashlytics
import com.quangtd.qmazes.game.gamemanager.SoundManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SoundManager.getInstance().setup(this)
    }
}