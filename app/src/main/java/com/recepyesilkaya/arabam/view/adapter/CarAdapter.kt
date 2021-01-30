package com.recepyesilkaya.arabam.view.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.util.State

class CarListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<CarResponse, RecyclerView.ViewHolder>(CarDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    private lateinit var carItemClick: (Long) -> Unit

    fun onClickItem(carItemClick: (Long) -> Unit) {
        this.carItemClick = carItemClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) CarViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as CarViewHolder).bind(getItem(position), carItemClick)
        } else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val CarDiffCallback = object : DiffUtil.ItemCallback<CarResponse>() {
            override fun areItemsTheSame(oldItem: CarResponse, newItem: CarResponse): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: CarResponse, newItem: CarResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}
