package com.recepyesilkaya.arabam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.recepyesilkaya.arabam.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}