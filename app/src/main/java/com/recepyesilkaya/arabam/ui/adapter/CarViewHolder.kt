package com.recepyesilkaya.arabam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.databinding.ItemCarBinding
import com.recepyesilkaya.arabam.ui.home.HomeItemViewState

class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        car: CarResponse?,
        carItemClick: (Long) -> Unit,
    ) {
        car?.let { carItem ->
            Mock.selectedCars?.add(SelectedCarEntity(Mock.idBackForSelectedCar))
            binding.car = HomeItemViewState(carItem, Mock.selectedCars)
            binding.root.setOnClickListener {
                binding.car = HomeItemViewState(carItem, Mock.selectedCars)
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