package com.recepyesilkaya.arabam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.recepyesilkaya.arabam.databinding.FragmentHomeBinding
import com.recepyesilkaya.arabam.ui.adapter.CarListAdapter
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var carListAdapter: CarListAdapter
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

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

        initAdapter()
        initState()
        initOnClickAdapterItem()
        initObserve()
    }

    private fun initAdapter() {
        carListAdapter = CarListAdapter { homeViewModel.retry() }
        rvCar.adapter = carListAdapter
        homeViewModel.cars.observe(viewLifecycleOwner, Observer {
            carListAdapter.submitList(it)
            homeViewModel.carsAddDatabase(it)
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

    private fun initOnClickAdapterItem() {
        carListAdapter.onClickItem {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }
    }


    private fun initObserve() {
        /* homeViewModel.localCars.observe(viewLifecycleOwner, Observer { cars ->
             cars.forEach {
                 Log.e("JRDev",it.title.toString())
             }
         })*/
    }

}