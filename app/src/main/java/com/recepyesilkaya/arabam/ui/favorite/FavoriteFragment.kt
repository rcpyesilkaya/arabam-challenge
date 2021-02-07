package com.recepyesilkaya.arabam.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.recepyesilkaya.arabam.databinding.FragmentFavoriteBinding
import com.recepyesilkaya.arabam.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var favoriteAdapter = FavoriteAdapter()
    private lateinit var fragmentFavoriteBinding: FragmentFavoriteBinding

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return fragmentFavoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFavoriteBinding.viewModel = favoriteViewModel
        fragmentFavoriteBinding.lifecycleOwner = viewLifecycleOwner

        initAdapter()
        bindOnClick()
        swipe()
    }

    private fun initAdapter() {
        fragmentFavoriteBinding.rvFavorite.adapter = favoriteAdapter

        favoriteViewModel.favorites.observe(viewLifecycleOwner, Observer {
            favoriteViewModel.swipeState.value = true
            favoriteAdapter.updateFavorites(it)
        })

        favoriteAdapter.onClickItem {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun bindOnClick() {
        favoriteViewModel.onClickBack {
            activity?.onBackPressed()
        }
    }

    private fun swipe() {
        fragmentFavoriteBinding.swipeRefresh.setOnRefreshListener {
            fragmentFavoriteBinding.swipeRefresh.isRefreshing = false
            favoriteViewModel.swipeState.value = false
            favoriteViewModel.getFavorites()
        }
    }
}