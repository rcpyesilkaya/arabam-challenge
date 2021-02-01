package com.recepyesilkaya.arabam.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.databinding.FragmentDetailBinding
import com.recepyesilkaya.arabam.ui.adapter.CarAdvertDetailViewPagerAdapter
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var carId: Long? = null
    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initDetail()
        bindImageSize()
        initObserve()
        bindOnClickBack()
    }

    private fun initDetail() {
        arguments?.let {
            carId = DetailFragmentArgs.fromBundle(it).id
        }
        carId?.let {
            detailViewModel.getCarDetail(it)
        }
    }

    private fun bindImageSize() {
        val imageSize = String.format(getString(R.string.image_size), 800, 600)
        detailViewModel.imageSize = imageSize
    }

    private fun initObserve() {
        detailViewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let { slideModel ->
                binding.imageSlider.setImageList(slideModel)
            }
        })

        detailViewModel.carDetailResource.observe(viewLifecycleOwner, Observer { resource ->
            if (resource.state == State.SUCCESS) {
                resource.data?.let { carDetail ->
                    addAdvertInfo(carDetail)
                    bindViewPager()
                    detailViewModel.bindImages(carDetail)
                }
            }
        })
    }

    private fun bindOnClickBack() {
        detailViewModel.onClickBack {
            activity?.onBackPressed()
        }
    }

    private fun bindViewPager() {
        binding.vpProperty.adapter =
            CarAdvertDetailViewPagerAdapter(Mock.getListFragment(), childFragmentManager)
        binding.tlProperty.setupWithViewPager(binding.vpProperty)
    }

    private fun addAdvertInfo(carDetail: CarDetail) {
        Mock.description = carDetail.text.toString()
        Mock.carAdvertInfo(requireContext(), carDetail)
    }
}