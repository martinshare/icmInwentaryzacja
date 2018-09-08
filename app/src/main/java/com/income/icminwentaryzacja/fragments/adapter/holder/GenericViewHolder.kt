package com.income.icminwentaryzacja.fragments.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.income.icminwentaryzacja.fragments.abstraction.FragmentBase

abstract class GenericViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T, fragmentBase: FragmentBase)
}