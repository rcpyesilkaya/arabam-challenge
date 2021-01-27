package com.recepyesilkaya.arabam.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.databinding.ItemCarBinding

class CarAdapter(private val context: Context) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private var carList = ArrayList<CarResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding: ItemCarBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_car,
            parent,
            false
        )
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    override fun getItemCount(): Int = carList.size

    fun updateCarList(carList: List<CarResponse>) {
        this.carList.clear()
        this.carList.addAll(carList)
        notifyDataSetChanged()
    }

    class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: CarResponse) {
            binding.car = car
        }
    }
}