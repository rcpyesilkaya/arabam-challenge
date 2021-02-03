package com.recepyesilkaya.arabam.ui.detail

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.recepyesilkaya.arabam.data.model.User
import com.recepyesilkaya.arabam.databinding.FragmentDetailBinding
import com.recepyesilkaya.arabam.ui.adapter.CarAdvertDetailViewPagerAdapter
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_user.*


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var carId: Long? = null
    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mock.childFragmentManager = childFragmentManager
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
        bindAlertDialog()
    }

    private fun initDetail() {
        arguments?.let {
            carId = DetailFragmentArgs.fromBundle(it).id
        }
        carId?.let {
            detailViewModel.getCarDetail(it)
            detailViewModel.addSelectedCar(it.toInt())
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
                    this.user = carDetail.userInfo
                }
            }
        })

        detailViewModel.message.observe(viewLifecycleOwner, Observer {
            detailViewModel.carDetail
            val message = String.format(
                getString(R.string.whatsapp_message),
                detailViewModel.carDetail?.title,
                detailViewModel.carDetail?.modelName,
                detailViewModel.carDetail?.priceFormatted
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.setPackage("com.whatsapp")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            context?.let {
                ContextCompat.startActivity(
                    it,
                    intent,
                    intent.getBundleExtra(Intent.EXTRA_TEXT)
                )
            }
        })
    }

    private fun bindOnClickBack() {
        detailViewModel.onClickBack {
            activity?.onBackPressed()
        }

    }

    private fun bindAlertDialog() {
        binding.btnCall.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.layout_user, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView).show()
            mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder.show()
            mBuilder.tvUserNameSurname.text = detailViewModel.carDetail?.userInfo?.nameSurname
            mBuilder.tvPhone.text = detailViewModel.carDetail?.userInfo?.phoneFormatted

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${detailViewModel.carDetail?.userInfo?.phone}")
            mBuilder.tvPhone.setOnClickListener {
                startActivity(intent)
            }
        }
    }

    private fun bindViewPager() {
        binding.vpProperty.adapter =
            Mock.childFragmentManager?.let {
                CarAdvertDetailViewPagerAdapter(
                    Mock.getListFragment(),
                    it
                )
            }
        binding.tlProperty.setupWithViewPager(binding.vpProperty)
    }

    private fun addAdvertInfo(carDetail: CarDetail) {
        Mock.description = carDetail.text.toString()
        Mock.carAdvertInfo(requireContext(), carDetail)
    }

}