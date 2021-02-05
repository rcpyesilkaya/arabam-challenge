package com.recepyesilkaya.arabam.ui.detail.advertdescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.databinding.FragmentDescriptionBinding


class DescriptionFragment : Fragment() {
    private lateinit var binding: FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.description = Mock.viewPagerAdvertDescription
    }
}