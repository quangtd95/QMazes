package com.quangtd.qmazes.ui.screen.home

import android.os.Bundle
import android.os.PersistableBundle
import com.quangtd.qmazes.R
import com.quangtd.qmazes.mvpbase.BaseActivity
import com.quangtd.qmazes.ui.screen.category.CategoryActivity
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.game.gamemanager.SoundManager
import com.quangtd.qmazes.util.SharedPreferencesUtils


/**
 * Created by quang.td95@gmail.com
 * on 9/23/2018.
 */
class HomeActivity : BaseActivity<HomeView, HomePresenter>(), HomeView {
    private var pref: SharedPreferencesUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initValues()
        initViews()
        initActions()
    }

    private fun initValues() {
        pref = SharedPreferencesUtils.getInstance(this)
    }

    private fun initViews() {
        if (pref?.getBool(CommonConstants.MUTE)!!) {
            ibMute.setImageDrawable(resources.getDrawable(R.drawable.ic_unmute))
        } else {
            ibMute.setImageDrawable(resources.getDrawable(R.drawable.ic_mute))
        }
    }

    private fun initActions() {
        ibPlay.setOnClickListener {
            CategoryActivity.startCategoryActivity(this)
        }
        ibLike.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            } else {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
            }
        }
        ibAbout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/quang.td95")))
        }
        ibMute.setOnClickListener {
            val mute = SoundManager.getInstance().toggleMute()
            if (mute) {
                ibMute.setImageDrawable(resources.getDrawable(R.drawable.ic_mute))
                val toast = Toast.makeText(this, "Sound Off", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.BOTTOM, 0, (ibMute.height * 1.5).toInt())
                toast.show()
                pref?.setBool(CommonConstants.MUTE, true)
            } else {
                ibMute.setImageDrawable(resources.getDrawable(R.drawable.ic_unmute))
                SoundManager.getInstance().playTouchSound()
                val toast = Toast.makeText(this, "Sound On", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.BOTTOM, 0, (ibMute.height * 1.5).toInt())
                toast.show()
                pref?.setBool(CommonConstants.MUTE, false)
            }
        }
    }
}