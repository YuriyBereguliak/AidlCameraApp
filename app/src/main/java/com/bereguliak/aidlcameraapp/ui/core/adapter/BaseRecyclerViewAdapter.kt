package com.bereguliak.aidlcameraapp.ui.core.adapter

import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<T>();

    //region RecyclerView.Adapter
    override fun getItemCount(): Int = items.size
    //endregion

    //region BaseRecyclerViewAdapter
    @UiThread
    fun addAll(list: List<T>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    @UiThread
    fun getItemByIndex(position: Int): T = items[position]

    @UiThread
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
    //endregion
}