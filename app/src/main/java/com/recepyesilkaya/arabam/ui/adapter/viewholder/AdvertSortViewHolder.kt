package com.recepyesilkaya.arabam.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.data.model.Sort
import com.recepyesilkaya.arabam.databinding.ItemAdvertSortBinding

class AdvertSortViewHolder(private val binding: ItemAdvertSortBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(sort: Sort, sortClick: (Sort) -> Unit) {
        binding.sort = sort
        binding.root.setOnClickListener {
            sortClick.invoke(sort)
        }
    }
}