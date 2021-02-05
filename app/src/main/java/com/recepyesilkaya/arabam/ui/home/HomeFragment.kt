package com.recepyesilkaya.arabam.ui.home

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.Filter
import com.recepyesilkaya.arabam.databinding.FragmentHomeBinding
import com.recepyesilkaya.arabam.ui.adapter.AdvertSortAdapter
import com.recepyesilkaya.arabam.ui.adapter.CarListAdapter
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.util.toArray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_advert_filter.*
import kotlinx.android.synthetic.main.layout_sort.*
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var carListAdapter: CarListAdapter
    private lateinit var advertSortAdapter: AdvertSortAdapter
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var mBuilder: AlertDialog
    private lateinit var mDialogView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.getSelectedCars()
        initAdapter()
        bindOnClick()
        initState()
        bindObserve()
        bindAlertDialog()

    }

    private fun initAdapter() {
        carListAdapter =
            CarListAdapter(homeViewModel.isStyleChange.value!!) { homeViewModel.retry() }
        rvCar.adapter = carListAdapter
        homeViewModel.cars.observe(viewLifecycleOwner, Observer {
            carListAdapter.submitList(it)
        })
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

        binding.ivListStyle.setOnClickListener {
            homeViewModel.isStyleChange.value = !homeViewModel.isStyleChange.value!!
            initAdapter()
            bindOnClick()
        }

        binding.ivFilterClean.setOnClickListener {
            Mock.advertFilter = Filter(null, null, null, null, null)
            Mock.advertSort.sortType = null
            Mock.advertSort.sortDirections = null
            homeViewModel.isFilter.value = false
            val action = HomeFragmentDirections.actionHomeFragmentSelf()
            findNavController().navigate(action)
        }

    }

    private fun bindObserve() {
        homeViewModel.selectedCars.observe(viewLifecycleOwner, Observer {
            Mock.selectedCars = it.toArray()
            if (homeViewModel.firstTime) {
                initAdapter()
                bindOnClick()
                homeViewModel.firstTime = false
            }
        })

        homeViewModel.sort.observe(viewLifecycleOwner, Observer {
            it?.let {
                val action = HomeFragmentDirections.actionHomeFragmentSelf()
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
    }

    private fun bindAlertDialog() {
        binding.ivMic.setOnClickListener {
            mDialogView =
                LayoutInflater.from(context).inflate(R.layout.layout_sort, null)
            mBuilder = AlertDialog.Builder(context).setView(mDialogView).show()
            mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            mBuilder.ivMicrophoneDialog.setOnClickListener {
                promptSpeechInput()
            }

            mBuilder.ivSortClose.setOnClickListener {
                mBuilder.dismiss()
            }

            context?.let { context ->
                advertSortAdapter = AdvertSortAdapter(Mock.getSortList(context)) {
                    homeViewModel.sortResult(it, context, null)
                }
            }
            mBuilder.rvSort.adapter = advertSortAdapter
        }
        binding.ivFilter.setOnClickListener {
            bindAlertFilter()
        }
    }

    private fun bindAlertFilter() {
        mDialogView =
            LayoutInflater.from(context).inflate(R.layout.layout_advert_filter, null)
        mBuilder = AlertDialog.Builder(context).setView(mDialogView).show()
        mBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mBuilder.ivFilterClose.setOnClickListener {
            mBuilder.dismiss()
        }
        mBuilder.btnOk.setOnClickListener {
            if (homeViewModel.isFilter.value!!) {
                val action = HomeFragmentDirections.actionHomeFragmentSelf()
                findNavController().navigate(action)
            }

            mBuilder.dismiss()
        }

        val adapter = context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_spinner_item,
                Mock.getYearList()
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mBuilder.spinnerMinYear.adapter = adapter
        mBuilder.spinnerMaxYear.adapter = adapter
        mBuilder.spinnerMinYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0?.getItemAtPosition(p2)
                            .toString() != getString(R.string.spinner_select)
                    ) {
                        Mock.advertFilter.minYear = p0?.getItemAtPosition(p2).toString().toInt()
                        homeViewModel.isFilter.value = true
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        mBuilder.spinnerMaxYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0?.getItemAtPosition(p2)
                            .toString() != getString(R.string.spinner_select)
                    ) {
                        Mock.advertFilter.maxYear = p0?.getItemAtPosition(p2).toString().toInt()
                        homeViewModel.isFilter.value = true
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        context?.let { Mock.getCategoryList(it) }

        val adapterCategory = context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_spinner_item,
                Mock.filterCategoryName!!
            )
        }
        adapterCategory?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBuilder.spinnerCategory.adapter = adapterCategory
        mBuilder.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0?.getItemAtPosition(p2)
                            .toString() != getString(R.string.spinner_select)
                    ) {
                        Mock.advertFilter.category = Mock.filterCategoryId?.get(p2)
                        homeViewModel.isFilter.value = true
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        mBuilder.btnMinDate.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        Mock.advertFilter.minDate = "$year-$dayOfMonth-$monthOfYear"
                        mBuilder.btnMinDate.text = "$year-$dayOfMonth-$monthOfYear"
                        homeViewModel.isFilter.value = true
                    },
                    homeViewModel.year,
                    homeViewModel.month,
                    homeViewModel.day
                )
            }
            dpd?.show()
        }

        mBuilder.btnMaxDate.setOnClickListener {
            val dpd2 = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        Mock.advertFilter.maxDate = "$year-$dayOfMonth-$monthOfYear"
                        mBuilder.btnMaxDate.text = "$year-$dayOfMonth-$monthOfYear"
                        homeViewModel.isFilter.value = true
                    },
                    homeViewModel.year,
                    homeViewModel.month,
                    homeViewModel.day
                )
            }
            dpd2?.show()
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
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
            startActivityForResult(intent, Companion.REQ_CODE_SPEECH_INPUT)
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
            Companion.REQ_CODE_SPEECH_INPUT -> {
                if (resultCode === RESULT_OK && null != data) {
                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    context?.let { homeViewModel.sortResult(null, it, result!![0]) }
                }
            }
        }
    }

    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 1000
    }

}