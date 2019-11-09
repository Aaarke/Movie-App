package com.example.mvvmbaseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmbaseproject.ui.listener.Action1

abstract class EndlessAdapter<T, VH : RecyclerView.ViewHolder>(
    context: Context,
    protected var mItems: MutableList<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Action1<List<T>> {

    protected val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var showLoadMore = false

    var isLoadMore: Boolean
        get() = showLoadMore
        set(enabled) {
            if (showLoadMore != enabled) {
                showLoadMore = if (showLoadMore) {
                    notifyItemRemoved(itemCount)
                    false
                } else {
                    notifyItemInserted(itemCount)
                    true
                }
            }
        }

    val items: List<T>
        get() = mItems


    private fun countLoadMore(): Int {
        return if (showLoadMore) 1 else 0
    }

    override fun getItemCount(): Int {
        return mItems.size + countLoadMore()
    }


    override fun call(newItems: List<T>) {
        add(newItems)
    }

    fun set(items: MutableList<T>) {
        mItems = items
        notifyDataSetChanged()
    }

    private fun add(newItems: List<T>) {
        if (newItems.isNotEmpty()) {
            val currentSize = mItems.size
            val amountInserted = newItems.size
            mItems.addAll(newItems)
            notifyItemRangeInserted(currentSize, amountInserted)
        }
    }


    fun clear() {
        if (mItems.isNotEmpty()) {
            mItems.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateItemHolder(parent, viewType)
    }

    protected abstract fun onCreateItemHolder(parent: ViewGroup, viewType: Int): VH


}
