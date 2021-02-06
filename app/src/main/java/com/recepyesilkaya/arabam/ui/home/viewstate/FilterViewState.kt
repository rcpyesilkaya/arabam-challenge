package com.recepyesilkaya.arabam.ui.home.viewstate

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.ui.home.HomeViewModel

data class FilterViewState(
    private val alert: AlertDialog,
    val homeViewModel: HomeViewModel,
    private val context: Context?,
    private val startHomePage: () -> Unit
) {

    fun onClickBtnOk(view: View) {
        if (Mock.advertFilter.minDate != null ||
            Mock.advertFilter.maxDate != null ||
            Mock.advertFilter.category != null ||
            Mock.advertFilter.minYear != null ||
            Mock.advertFilter.maxYear != null
        ) {
            startHomePage.invoke()
        }
        alert.dismiss()
    }

    fun alertClose(view: View) {
        alert.dismiss()
    }

    fun yearAdapter(): ArrayAdapter<String> {
        val adapter = context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_spinner_item,
                Mock.getYearList()
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter!!
    }

    fun categoryAdapter(): ArrayAdapter<String> {
        context?.let { Mock.getCategoryList(it) }
        val adapterCategory = context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_spinner_item,
                Mock.filterCategoryName!!
            )
        }
        adapterCategory?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapterCategory!!
    }

    val onItemClickListenerMinYear =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)
                        .toString() != context?.getString(R.string.spinner_select)
                ) {
                    Mock.advertFilter.minYear = p0?.getItemAtPosition(p2).toString().toInt()
                } else Mock.advertFilter.minYear = null
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    val onItemClickListenerMaxYear =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)
                        .toString() != context?.getString(R.string.spinner_select)
                ) {
                    Mock.advertFilter.maxYear = p0?.getItemAtPosition(p2).toString().toInt()
                } else Mock.advertFilter.maxYear = null
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    val onItemClickListenerCategory =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)
                        .toString() != context?.getString(R.string.spinner_select)
                ) {
                    Mock.advertFilter.category = Mock.filterCategoryId?.get(p2)
                } else Mock.advertFilter.category = null
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
}