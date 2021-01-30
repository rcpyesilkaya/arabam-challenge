package com.recepyesilkaya.arabam.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.recepyesilkaya.arabam.data.model.FragmentModel

class CarAdvertInfoViewPager(
    private val fragmentList: List<FragmentModel>,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getCount(): Int =
        fragmentList.size

    override fun getItem(position: Int): Fragment =
        fragmentList[position].fragment

    override fun getPageTitle(position: Int): CharSequence? =
        fragmentList[position].title
}