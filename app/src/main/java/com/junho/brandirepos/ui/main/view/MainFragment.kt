package com.junho.brandirepos.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junho.brandirepos.R
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.ui.main.adapter.MainAdapter
import com.junho.brandirepos.ui.main.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initDataBinding()
        return root
    }

    private fun initDataBinding() {
        mainViewModel.text.observe(viewLifecycleOwner,
            {
                Log.d("model", it!!)
            })
        mainViewModel.getImageData("설현", MainService.Companion.Sort.ACCURACY, 1)
    }

    private fun afterDataBinding() {

    }
}