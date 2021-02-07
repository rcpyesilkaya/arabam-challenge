package com.recepyesilkaya.arabam.ui.home

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.model.Filter
import com.recepyesilkaya.arabam.databinding.FragmentHomeBinding
import com.recepyesilkaya.arabam.databinding.LayoutAdvertFilterBinding
import com.recepyesilkaya.arabam.databinding.LayoutSortBinding
import com.recepyesilkaya.arabam.ui.adapter.CarListAdapter
import com.recepyesilkaya.arabam.ui.home.viewstate.FilterViewState
import com.recepyesilkaya.arabam.ui.home.viewstate.SortViewState
import com.recepyesilkaya.arabam.util.REQ_CODE_SPEECH_INPUT
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_sort.*
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var carListAdapter: CarListAdapter
    private lateinit var mBuilder: AlertDialog
    private lateinit var filterViewState: FilterViewState
    private lateinit var intent: Intent
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var layoutSortBinding: LayoutSortBinding
    private lateinit var layoutAdvertFilterBinding: LayoutAdvertFilterBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val interpolator = OvershootInterpolator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding.viewModel = homeViewModel
        fragmentHomeBinding.lifecycleOwner = viewLifecycleOwner

        arguments?.let {
            homeViewModel.filterSuccessValue.value =
                HomeFragmentArgs.fromBundle(it).filterSuccessValue
        }
        fragmentHomeBinding.fabSort.alpha = 0f
        fragmentHomeBinding.fabFilter.alpha = 0f
        fragmentHomeBinding.fabFavorite.alpha = 0f

        initAdapter()
        initState()
        bindOnClick()
        bindObserve()
        bindAlertDialog()
    }

    private fun initAdapter() {
        carListAdapter =
            CarListAdapter(homeViewModel.isStyleChange.value!!) { homeViewModel.retry() }
        rvCar.adapter = carListAdapter
        homeViewModel.cars.observe(viewLifecycleOwner, Observer {
            filterControl(it)
            carListAdapter.submitList(it)
        })
    }

    private fun filterControl(carResponse: PagedList<CarResponse>) {
        val filterControl = carResponse.firstOrNull()
        if (filterControl == null && !homeViewModel.errorValue.value!!) {
            homeViewModel.filterErrorValue.value = true
            homeViewModel.filterSuccessValue.value = true
            fragmentHomeBinding.tvFilterError.text =
                getString(R.string.fragment_home_filter_result_error_info)
        } else homeViewModel.filterErrorValue.value = false
    }

    private fun initState() {
        tvError.setOnClickListener { homeViewModel.retry() }
        homeViewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            homeViewModel.checkVisibility(state)
            if (!homeViewModel.listIsEmpty()) {
                carListAdapter.setState(state ?: State.SUCCESS)
            }
        })
    }

    private fun bindOnClick() {
        carListAdapter.onClickItem {
            Mock.selectedCarForBackId = it.toInt()
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }

        fragmentHomeBinding.ivListStyle.setOnClickListener {
            homeViewModel.isStyleChange.value = !homeViewModel.isStyleChange.value!!
            initAdapter()
            bindOnClick()
        }

        fragmentHomeBinding.ivFilterClean.setOnClickListener {
            Mock.advertFilter = Filter(null, null, null, null, null)
            Mock.advertSort.sortType = null
            Mock.advertSort.sortDirections = null
            homeViewModel.filterSuccessValue.value = false
            val action =
                HomeFragmentDirections.actionHomeFragmentSelf(homeViewModel.filterSuccessValue.value!!)
            findNavController().navigate(action)
        }
    }

    private fun bindObserve() {
        homeViewModel.selectedCars.observe(viewLifecycleOwner, Observer {

            it.forEach {
                Mock.selectedCars?.add(it)
            }
            if (homeViewModel.firstTime) {
                initAdapter()
                bindOnClick()
                homeViewModel.firstTime = false
            }
        })

        homeViewModel.sort.observe(viewLifecycleOwner, Observer {
            it?.let {
                val action = HomeFragmentDirections.actionHomeFragmentSelf(true)
                findNavController().navigate(action)
                mBuilder.dismiss()
                return@Observer
            }
            Toast.makeText(
                context,
                getString(R.string.fragment_home_sort_result_error_info),
                Toast.LENGTH_LONG
            )
                .show()
            mBuilder.dismiss()
        })

        homeViewModel.isMenuOpen.observe(viewLifecycleOwner, Observer {
            if (it) {
                fragmentHomeBinding.fabMain.animate().setInterpolator(interpolator).rotationBy(45f)
                    .setDuration(300).start()
                fragmentHomeBinding.fabFilter.animate().translationY(0f).alpha(1f)
                    .setInterpolator(interpolator).setDuration(300).start()
                fragmentHomeBinding.fabSort.animate().translationY(0f).alpha(1f)
                    .setInterpolator(interpolator).setDuration(300).start()
                fragmentHomeBinding.fabFavorite.animate().translationY(0f).alpha(1f)
                    .setInterpolator(interpolator).setDuration(300).start()
            } else {
                fragmentHomeBinding.fabMain.animate().setInterpolator(interpolator).rotationBy(0f)
                    .setDuration(300).start()
                fragmentHomeBinding.fabFilter.animate().translationY(100f).alpha(0f)
                    .setInterpolator(interpolator).setDuration(300).start()
                fragmentHomeBinding.fabSort.animate().translationY(100f).alpha(0f)
                    .setInterpolator(interpolator).setDuration(300).start()
                fragmentHomeBinding.fabFavorite.animate().translationY(100f).alpha(0f)
                    .setInterpolator(interpolator).setDuration(300).start()
            }
        })
    }

    private fun bindAlertDialog() {
        fragmentHomeBinding.fabSort.setOnClickListener {
            bindAlertSort()
        }
        fragmentHomeBinding.fabFilter.setOnClickListener {
            bindAlertFilter()
        }
        fragmentHomeBinding.fabFavorite.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
            findNavController().navigate(action)
        }
    }

    private fun bindAlertSort() {
        layoutSortBinding = LayoutSortBinding.inflate(
            LayoutInflater.from(context),
            fragmentHomeBinding.root as ViewGroup,
            false
        )
        mBuilder = AlertDialog.Builder(context).setView(layoutSortBinding.root).show()
        mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        layoutSortBinding.viewState = context?.let { SortViewState(it, mBuilder, homeViewModel) }

        mBuilder.ivMicrophoneDialog.setOnClickListener {
            promptSpeechInput()
        }
    }

    private fun promptSpeechInput() {
        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speach_to_text_title)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                view?.context,
                getString(R.string.speach_to_text_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode === RESULT_OK && null != data) {
                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    context?.let { homeViewModel.sortResult(null, it, result!![0]) }
                }
            }
        }
    }

    private fun bindAlertFilter() {
        layoutAdvertFilterBinding = LayoutAdvertFilterBinding.inflate(
            LayoutInflater.from(context),
            fragmentHomeBinding.root as ViewGroup,
            false
        )
        mBuilder = AlertDialog.Builder(context).setView(layoutAdvertFilterBinding.root).show()
        mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        filterViewState = FilterViewState(mBuilder, homeViewModel, context) {
            homeViewModel.filterSuccessValue.value = true
            val action =
                HomeFragmentDirections.actionHomeFragmentSelf(homeViewModel.filterSuccessValue.value!!)
            findNavController().navigate(action)
        }
        layoutAdvertFilterBinding.viewState = filterViewState
    }

}