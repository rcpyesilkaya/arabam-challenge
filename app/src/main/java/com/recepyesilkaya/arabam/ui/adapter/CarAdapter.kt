package com.recepyesilkaya.arabam.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.ui.adapter.viewholder.CarViewHolder
import com.recepyesilkaya.arabam.ui.adapter.viewholder.CarViewHolderStyleSecond
import com.recepyesilkaya.arabam.ui.adapter.viewholder.ListFooterViewHolder
import com.recepyesilkaya.arabam.util.State

private const val DATA_VIEW_TYPE = 1
private const val FOOTER_VIEW_TYPE = 2
private const val DATA_VIEW_TYPE_CHANGES = 3

class CarListAdapter(
    private val isStyleChange: Boolean,
    private val retry: () -> Unit
) :
    PagedListAdapter<CarResponse, RecyclerView.ViewHolder>(CarDiffCallback) {
    private var state = State.LOADING
    private lateinit var carItemClick: (Long) -> Unit

    fun onClickItem(
        carItemClick: (Long) -> Unit
    ) {
        this.carItemClick = carItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DATA_VIEW_TYPE -> CarViewHolder.create(parent)
            DATA_VIEW_TYPE_CHANGES -> CarViewHolderStyleSecond.create(parent)
            else -> ListFooterViewHolder.create(retry, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == DATA_VIEW_TYPE -> {
                (holder as CarViewHolder).bind(getItem(position), carItemClick)
            }
            getItemViewType(position) == DATA_VIEW_TYPE_CHANGES -> {
                (holder as CarViewHolderStyleSecond).bind(getItem(position), carItemClick)
            }
            else -> (holder as ListFooterViewHolder).bind(state)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) {
            if (isStyleChange) DATA_VIEW_TYPE_CHANGES
            else DATA_VIEW_TYPE
        } else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val CarDiffCallback = object : DiffUtil.ItemCallback<CarResponse>() {
            override fun areItemsTheSame(oldItem: CarResponse, newItem: CarResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CarResponse, newItem: CarResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
