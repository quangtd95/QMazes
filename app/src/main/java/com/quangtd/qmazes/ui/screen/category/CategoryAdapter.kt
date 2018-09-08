package com.quangtd.qmazes.ui.screen.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quangtd.qmazes.R
import com.quangtd.qmazes.data.model.Category
import com.quangtd.qmazes.mvpbase.BaseAdapter
import com.quangtd.qmazes.mvpbase.BaseViewHolder
import kotlinx.android.synthetic.main.item_category.view.*

/**
 * Created by quang.td95@gmail.com
 * on 9/2/2018.
 */
class CategoryAdapter(private var mContext: Context) : BaseAdapter<Category, CategoryAdapter.CategoryViewHolder>() {
    var onCategoryClickListener: OnCategoryClickListener? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_category, p0, false)
        return CategoryViewHolder(view)
    }

    inner class CategoryViewHolder(itemView: View) : BaseViewHolder<Category>(itemView) {

        override fun bindData(t: Category) {
            itemView.rlLevel.background = mContext.resources.getDrawable(R.drawable.round_corner_yellow_bg)
            itemView.tvCategory.text = t.gameKind.nameKind
            itemView.rlLevel.setOnClickListener {
                onCategoryClickListener?.onClickCategory(t)
            }
        }

    }

    interface OnCategoryClickListener {
        fun onClickCategory(category: Category)
    }
}