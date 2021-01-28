package com.recepyesilkaya.arabam.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_car.view.*

class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(car: CarResponse?) {
        if (car != null) {
            itemView.tvCarTittle.text = car.title
            Picasso.get().load(car.photo).into(itemView.ivCar)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CarViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_car, parent, false)
            return CarViewHolder(view)
        }
    }
}