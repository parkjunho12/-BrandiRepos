package com.junho.brandirepos.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.junho.brandirepos.R
import com.junho.brandirepos.ui.main.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        return root
    }
}