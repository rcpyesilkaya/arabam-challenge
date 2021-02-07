package com.recepyesilkaya.arabam.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.databinding.ItemCarSecondStyleBinding
import com.recepyesilkaya.arabam.ui.home.viewstate.HomeItemViewState


class CarViewHolderStyleSecond(val binding: ItemCarSecondStyleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        car: CarResponse?,
        carItemClick: (Long) -> Unit,
    ) {
        car?.let { carItem ->
            Mock.selectedCars?.add(SelectedCarEntity(Mock.selectedCarForBackId))
            binding.car = HomeItemViewState(carItem, Mock.selectedCars)
            binding.root.setOnClickListener {
                carItem.id.let { it1 -> carItemClick.invoke(it1) }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CarViewHolderStyleSecond {
            val binding: ItemCarSecondStyleBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_car_second_style,
                parent,
                false
            )
            return CarViewHolderStyleSecond(binding)
        }
    }
}