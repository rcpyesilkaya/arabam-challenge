package com.recepyesilkaya.arabam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.databinding.ItemCarBinding
import com.recepyesilkaya.arabam.ui.home.HomeViewState

class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(car: CarResponse?, carItemClick: (Long) -> Unit) {
        car?.let { carItem ->
            binding.car = HomeViewState(carItem)
            binding.root.setOnClickListener {
                carItem.id.let { it1 -> carItemClick.invoke(it1) }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CarViewHolder {
            val binding: ItemCarBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_car,
                parent,
                false
            )
            return CarViewHolder(binding)
        }
    }
}