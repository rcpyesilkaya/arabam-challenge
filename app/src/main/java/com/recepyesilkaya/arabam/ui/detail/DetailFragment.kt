package com.recepyesilkaya.arabam.ui.detail

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.databinding.FragmentDetailBinding
import com.recepyesilkaya.arabam.databinding.LayoutFullScreenImageBinding
import com.recepyesilkaya.arabam.databinding.LayoutUserBinding
import com.recepyesilkaya.arabam.ui.adapter.CarAdvertDetailViewPagerAdapter
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_full_screen_image.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.OnItemClickListener


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    lateinit var layoutUserBinding: LayoutUserBinding
    lateinit var layoutFullScreenImageBinding: LayoutFullScreenImageBinding
    lateinit var mBuilder: AlertDialog

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
        bindObserve()
        bindOnClick()
        bindAlertDialog()
    }

    private fun initDetail() {
        arguments?.let {
            detailViewModel.selectionCarId = DetailFragmentArgs.fromBundle(it).id
        }
        detailViewModel.selectionCarId?.let {
            detailViewModel.getCarDetail(it)
            detailViewModel.addSelectedCar(it.toInt())
        }
    }

    private fun bindImageSize() {
        val imageSize = String.format(getString(R.string.image_size), 800, 600)
        detailViewModel.imageSize = imageSize
    }

    private fun bindObserve() {
        detailViewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let { carousel ->
                binding.carousel.addData(carousel)
            }
        })

        detailViewModel.carDetailResource.observe(viewLifecycleOwner, Observer { resource ->
            if (resource.state == State.SUCCESS) {
                resource.data?.let { carDetail ->
                    addViewPagerAdvertInfo(carDetail)
                    bindViewPager()
                    detailViewModel.bindImages(carDetail)
                    detailViewModel.userInfo.value = carDetail.userInfo
                    detailViewModel.carShareInfo.value = String.format(
                        getString(R.string.whatsapp_message),
                        carDetail.title,
                        carDetail.modelName,
                        carDetail.priceFormatted
                    )
                }
            }
        })
    }

    private fun bindOnClick() {
        detailViewModel.onClickBack {
            activity?.onBackPressed()
        }
        detailViewModel.bindHigherOrderShareCar {
            context?.let { context ->
                ContextCompat.startActivity(
                    context,
                    it,
                    it.getBundleExtra(Intent.EXTRA_TEXT)
                )
            }
        }
    }

    private fun bindAlertDialog() {
        binding.btnCall.setOnClickListener {
            layoutUserBinding = LayoutUserBinding.inflate(
                LayoutInflater.from(context),
                binding.root as ViewGroup,
                false
            )
            layoutUserBinding.viewModel = detailViewModel
            mBuilder = AlertDialog.Builder(context).setView(layoutUserBinding.root).show()
            mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder.show()

            detailViewModel.bindHigherOrderPhone {
                startActivity(it)
            }
        }

        binding.carousel.onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                layoutFullScreenImageBinding = LayoutFullScreenImageBinding.inflate(
                    LayoutInflater.from(context),
                    binding.root as ViewGroup,
                    false
                )
                mBuilder =
                    AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                        .setView(layoutFullScreenImageBinding.root).show()
                detailViewModel.images.value?.let { mBuilder.ivFullScreen.addData(it) }

                mBuilder.btnCallFullScreen.setOnClickListener {
                    binding.btnCall.performClick()
                }
            }

            override fun onLongClick(position: Int, dataObject: CarouselItem) {}
        }
    }

    private fun bindViewPager() {
        binding.vpProperty.adapter =
            CarAdvertDetailViewPagerAdapter(Mock.getListFragment(), childFragmentManager)
        binding.tlProperty.setupWithViewPager(binding.vpProperty)
    }

    private fun addViewPagerAdvertInfo(carDetail: CarDetail) {
        Mock.viewPagerAdvertDescription = carDetail.text.toString()
        Mock.carAdvertInfo(requireContext(), carDetail)
    }
}