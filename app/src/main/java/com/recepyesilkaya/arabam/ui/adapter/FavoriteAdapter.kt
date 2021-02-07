package com.recepyesilkaya.arabam.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.data.local.entity.FavoriteEntity
import com.recepyesilkaya.arabam.ui.adapter.viewholder.CarViewHolder
import com.recepyesilkaya.arabam.util.toCarResponse

class FavoriteAdapter : RecyclerView.Adapter<CarViewHolder>() {

    private var favorites: List<FavoriteEntity>? = null
    private lateinit var carItemClick: (Long) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        return CarViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val carResponse = favorites?.get(position)?.toCarResponse()
        holder.bind(carResponse, carItemClick)
    }

    override fun getItemCount(): Int {
        favorites?.let {
            return it.size
        }
        return 0
    }

    fun updateFavorites(favorites: List<FavoriteEntity>?) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun onClickItem(carItemClick: (Long) -> Unit) {
        this.carItemClick = carItemClick
    }
}