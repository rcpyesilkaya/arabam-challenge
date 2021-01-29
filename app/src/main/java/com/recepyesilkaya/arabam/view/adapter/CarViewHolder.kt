package com.recepyesilkaya.arabam.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.databinding.ItemCarBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_car.view.*

class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(car: CarResponse?) {
        if (car != null) {
            binding.car = car
            Picasso.get().load(car.photo).into(itemView.ivCar)
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