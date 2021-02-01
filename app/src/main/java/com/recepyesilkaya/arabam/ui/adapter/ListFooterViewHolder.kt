package com.recepyesilkaya.arabam.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.databinding.ItemListFooterBinding
import com.recepyesilkaya.arabam.util.State

class ListFooterViewHolder(val binding: ItemListFooterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(state: State?) {
        binding.stateProgress = state == State.LOADING
        binding.stateError = state == State.ERROR

        Log.e("JRDevR----binding.stateProgress", binding.stateProgress.toString())
        Log.e("JRDevR----binding.stateError", binding.stateError.toString())
        Log.e("JRDevR----", state?.name.toString())
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val binding: ItemListFooterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_footer,
                parent,
                false
            )
            binding.tvError.setOnClickListener { retry() }
            return ListFooterViewHolder(binding)
        }
    }
}