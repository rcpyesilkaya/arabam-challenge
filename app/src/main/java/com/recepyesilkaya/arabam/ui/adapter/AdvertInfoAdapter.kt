package com.recepyesilkaya.arabam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarAdvertInfo
import com.recepyesilkaya.arabam.databinding.ItemAdvertInfoBinding
import com.recepyesilkaya.arabam.ui.detail.advertinfo.AdvertInfoViewState

class AdvertInfoAdapter(private var carAdvertInfo: ArrayList<CarAdvertInfo>) :
    RecyclerView.Adapter<AdvertInfoAdapter.AdvertInfoViewHolder>() {

    private var isColor = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertInfoViewHolder {
        val binding: ItemAdvertInfoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_advert_info,
            parent,
            false
        )
        return AdvertInfoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AdvertInfoViewHolder, position: Int) {
        holder.bind(carAdvertInfo[position], isColor)
        isColor = !isColor
    }

    override fun getItemCount(): Int = carAdvertInfo.size

    class AdvertInfoViewHolder(val binding: ItemAdvertInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(carAdvertInfo: CarAdvertInfo, isColor: Boolean) {
            binding.viewItemState = AdvertInfoViewState(carAdvertInfo)
            binding.carAdvertInfo = carAdvertInfo
            binding.isColor = isColor
        }
    }
}