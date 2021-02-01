package com.recepyesilkaya.arabam.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.recepyesilkaya.arabam.R


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initSplash()
    }

    private fun initSplash() {
        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(1500)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.homeFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

}