
package com.recepyesilkaya.arabam.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.recepyesilkaya.arabam.databinding.FragmentHomeBinding
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.view.adapter.CarListAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private lateinit var carListAdapter: CarListAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            this.viewModel = homeViewModel
            this.lifecycleOwner = this@HomeFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initState()
    }

    private fun initAdapter() {
        carListAdapter = CarListAdapter { homeViewModel.retry() }
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