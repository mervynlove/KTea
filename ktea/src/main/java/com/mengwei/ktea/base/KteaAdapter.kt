package com.mengwei.ktea.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Create by MengWei at 2018/8/28
 */
abstract class KteaAdapter<T>(@LayoutRes private val layoutRes: Int) : RecyclerView.Adapter<KteaAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    val datas by lazy { mutableListOf<T>() }

    fun clear() {
        datas.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, datas.size)
    }

    fun update(list: List<T>) {
        if (datas.isNotEmpty()) datas.clear()
        datas.addAll(list)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        datas.add(item)
        notifyItemChanged(datas.size - 1)
    }

    fun addAll(list: List<T>) {
        datas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getItemCount() = datas.size


}