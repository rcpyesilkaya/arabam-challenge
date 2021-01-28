package com.recepyesilkaya.arabam.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.view.adapter.CarListAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private lateinit var carListAdapter: CarListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAdapter()
        initState()

        /* carAdapter = CarAdapter(requireContext())
         rvCar.adapter = carAdapter
         homeViewModel.getCarData(1, 0, 10)
         observeCarLiveData()*/
    }

    private fun initAdapter() {
        carListAdapter = CarListAdapter { homeViewModel.retry() }
        rvCar.adapter = carListAdapter
        homeViewModel.cars.observe(viewLifecycleOwner, Observer {

            it.forEach { car ->
                Log.e("JRDev Tiitle", car.title.toString())

            }

            carListAdapter.submitList(it)
        })
    }

    private fun initState() {
        tvError.setOnClickListener { homeViewModel.retry() }
        homeViewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            progressBar.visibility =
                if (homeViewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            tvError.visibility =
                if (homeViewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!homeViewModel.listIsEmpty()) {
                carListAdapter.setState(state ?: State.SUCCESS)
            }
        })
    }

    /* private fun observeCarLiveData() {
         homeViewModel.loadingValue.observe(viewLifecycleOwner, Observer {
             progressBar.isVisible = it
         })
         homeViewModel.successValue.observe(viewLifecycleOwner, Observer {
             rvCar.isVisible = it
         })
         homeViewModel.errorValue.observe(viewLifecycleOwner, Observer {
             textView.isVisible = it
         })
         homeViewModel.carList.observe(viewLifecycleOwner, Observer {
             carAdapter.updateCarList(it)
         })
         homeViewModel.error.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })
     }*/
}