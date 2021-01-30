package com.recepyesilkaya.arabam.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarAdvertInfo
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.databinding.FragmentDetailBinding
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.view.adapter.CarAdvertInfoViewPager


class DetailFragment : Fragment() {

    private var carId: Long? = null

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

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
        binding.lifecycleOwner = this
        initDetail()
        initImageSlider()
        initObserve()
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
                    detailViewModel.carAdverts.clear()
                    addAdvertInfo(carDetail)
                    Mock.carAdverts = detailViewModel.carAdverts
                    bindViewPager()
                }
            }
        })
    }

    private fun bindViewPager() {

        binding.vpProperty.adapter =
            CarAdvertInfoViewPager(Mock.getListFragment(), childFragmentManager)
        binding.tlProperty.setupWithViewPager(binding.vpProperty)
    }

    private fun initDetail() {
        arguments?.let {
            carId = DetailFragmentArgs.fromBundle(it).id
        }
        carId?.let {
            detailViewModel.getCarDetail(it)
        }
    }

    private fun initImageSlider() {
        val imageSize = String.format(getString(R.string.image_size), 800, 600)
        detailViewModel.imageSize = imageSize
    }

    private fun addAdvertInfo(carDetail: CarDetail) {
        Mock.description = carDetail.text.toString()
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_price_title),
                carDetail.priceFormatted
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_no_title),
                carDetail.id.toString()
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_date_title),
                carDetail.dateFormatted
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_model_title),
                carDetail.modelName
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_year_title),
                carDetail.properties?.find { it.name == getString(R.string.advert_property_year_name) }?.value
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_fuel_title),
                carDetail.properties?.find { it.name == getString(R.string.advert_property_fuel_name) }?.value
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_gear_title),
                carDetail.properties?.find { it.name == getString(R.string.advert_property_gear_name) }?.value
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_km_title),
                carDetail.properties?.find { it.name == getString(R.string.advert_property_km_name) }?.value
            )
        )
        detailViewModel.carAdverts.add(
            CarAdvertInfo(
                getString(R.string.advert_color_title),
                carDetail.properties?.find { it.name == getString(R.string.advert_property_color_name) }?.value
            )
        )
    }
}