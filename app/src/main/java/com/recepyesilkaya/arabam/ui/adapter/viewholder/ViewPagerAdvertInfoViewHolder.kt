package com.recepyesilkaya.arabam.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.data.model.ViewPagerAdvertInfo
import com.recepyesilkaya.arabam.databinding.ItemAdvertInfoBinding
import com.recepyesilkaya.arabam.ui.detail.advertinfo.AdvertInfoViewState

class ViewPagerAdvertInfoViewHolder(val binding: ItemAdvertInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewPagerAdvertInfo: ViewPagerAdvertInfo, isColor: Boolean) {
        binding.viewItemState = AdvertInfoViewState(viewPagerAdvertInfo)
        binding.carAdvertInfo = viewPagerAdvertInfo
        binding.isColor = isColor
    }
}