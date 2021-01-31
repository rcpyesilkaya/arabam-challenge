package com.recepyesilkaya.arabam.view.detail.advertinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.databinding.FragmentAdvertInfoBinding
import com.recepyesilkaya.arabam.view.adapter.AdvertInfoAdapter


class AdvertInfoFragment : Fragment() {

    private lateinit var binding: FragmentAdvertInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvertInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdvertInfoAdapter(Mock.carAdverts)
        binding.rvAdvertInfo.adapter = adapter
    }
}