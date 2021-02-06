package com.recepyesilkaya.arabam.util

import android.app.DatePickerDialog
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.ui.adapter.AdvertSortAdapter
import com.recepyesilkaya.arabam.ui.home.HomeViewModel

@BindingAdapter("loadImage", "imageSize")
fun ImageView.setImage(url: String?, imageSize: String) {
    if (!url.isNullOrEmpty()) {
        val urlChange = url.replace("{0}", imageSize)
        this.downloadImageFromUrl(urlChange, getPlaceHolder(this.context))
    }
}

@BindingAdapter("toHtml")
fun TextView?.toHtml(description: String) {
    if (description.isNotEmpty()) {
        this?.text = description.toHtml()
    }
}

@BindingAdapter("onItemClickListener")
fun Spinner.onItemClickListener(onItemClickListener: AdapterView.OnItemSelectedListener) {
    this.onItemSelectedListener = onItemClickListener
}

@BindingAdapter("spinnerAdapterMinYear")
fun Spinner.spinnerAdapterMinYear(yearAdapter: ArrayAdapter<String>) {
    this.adapter = yearAdapter
    Mock.advertFilter.minYear?.let {
        val index = Mock.filterYears?.indexOf(it.toString())
        this.setSelection(index!!)
    }
}

@BindingAdapter("spinnerAdapterMaxYear")
fun Spinner.spinnerAdapterMaxYear(yearAdapter: ArrayAdapter<String>) {
    this.adapter = yearAdapter
    Mock.advertFilter.maxYear?.let {
        val index = Mock.filterYears?.indexOf(it.toString())
        this.setSelection(index!!)
    }
}

@BindingAdapter("spinnerAdapterCategory")
fun Spinner.spinnerAdapterCategory(categoryAdapter: ArrayAdapter<String>) {
    this.adapter = categoryAdapter
    Mock.advertFilter.category?.let {
        val index = Mock.filterCategoryId?.indexOf(it)
        this.setSelection(index!!)
    }
}

@BindingAdapter("datePickerDialogMinDate")
fun Button.onClickBtnMinDate(homeViewModel: HomeViewModel) {
    this.setOnClickListener {
        val dpd = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    Mock.advertFilter.minDate =
                        context.getString(R.string.date_format, year, dayOfMonth, monthOfYear)
                    this.text =
                        context.getString(R.string.date_format, year, dayOfMonth, monthOfYear)
                },
                homeViewModel.year,
                homeViewModel.month,
                homeViewModel.day
            )
        }
        dpd?.show()
    }
    Mock.advertFilter.minDate?.let {
        this.text = it
    }
}

@BindingAdapter("datePickerDialogMaxDate")
fun Button.onClickBtnMaxDate(homeViewModel: HomeViewModel) {
    this.setOnClickListener {
        val dpd = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    Mock.advertFilter.maxDate =
                        context.getString(R.string.date_format, year, dayOfMonth, monthOfYear)
                    this.text =
                        context.getString(R.string.date_format, year, dayOfMonth, monthOfYear)
                },
                homeViewModel.year,
                homeViewModel.month,
                homeViewModel.day
            )
        }
        dpd?.show()
    }
    Mock.advertFilter.maxDate?.let {
        this.text = it
    }
}

@BindingAdapter("bindAdapter")
fun RecyclerView.bindAdapter(advertSortAdapter: AdvertSortAdapter) {
    this.adapter = advertSortAdapter
}