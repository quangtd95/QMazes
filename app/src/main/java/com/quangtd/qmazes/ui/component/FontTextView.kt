package com.quangtd.qmazes.ui.component

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.quangtd.qmazes.R
import com.quangtd.qmazes.common.CommonConstants
import com.quangtd.qmazes.util.FontUtils

/**
 * Created by QuangTD
 * on 1/14/2018.
 */
class FontTextView(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var mFontName: String?

    init {
        val a: TypedArray? = context?.obtainStyledAttributes(attrs, R.styleable.FontTextView)
        mFontName = a?.getString(R.styleable.FontTextView_font_name)
        if (mFontName == null) {
            mFontName = CommonConstants.DEFAULT_FONT
        }
        val typeface = FontUtils.getTypeface(mFontName!!, context)
        setTypeface(typeface)
        a?.recycle()
    }

    fun setFont(fontName: String) {
        mFontName = fontName
        val typeface = FontUtils.getTypeface(mFontName!!, context)
        setTypeface(typeface)
    }
}