package com.recepyesilkaya.arabam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.Sort
import com.recepyesilkaya.arabam.databinding.ItemAdvertSortBinding
import com.recepyesilkaya.arabam.ui.adapter.viewholder.AdvertSortViewHolder

class AdvertSortAdapter(private val sorts: List<Sort>, private val sortClick: (Sort) -> Unit) :
    RecyclerView.Adapter<AdvertSortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertSortViewHolder {
        val binding: ItemAdvertSortBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_advert_sort,
            parent,
            false
        )
        return AdvertSortViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertSortViewHolder, position: Int) =
        holder.bind(sorts[position], sortClick)

    override fun getItemCount(): Int = sorts.size
}