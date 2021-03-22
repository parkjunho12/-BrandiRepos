package com.junho.brandirepos.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junho.brandirepos.R
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.ui.detail.DetailFragment
import com.junho.brandirepos.ui.main.adapter.MainAdapter
import com.junho.brandirepos.ui.main.adapter.data.ImageData
import com.junho.brandirepos.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter
    lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initStartView(root)
        initDataBinding()
        return root
    }

    private fun initStartView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_main)
        gridLayoutManager = GridLayoutManager(requireContext(), 3)
        mRecyclerView.layoutManager = gridLayoutManager

        val searchBar = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_bar)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private var isTexting = false

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("test", newText)
                CoroutineScope(Dispatchers.Main).launch {
                    if (!isTexting) {
                        isTexting = true
                        Handler(Looper.getMainLooper()).postDelayed(
                                Runnable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        mainViewModel.deleteImages()
                                        mainViewModel.getImageData(newText, MainService.Companion.Sort.ACCURACY, 1)

                                    }
                                    isTexting = false
                                },1000
                        )
                    }

                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    mainViewModel.deleteImages()
                    mainViewModel.getImageData(query, MainService.Companion.Sort.ACCURACY, 1)

                }
                return false
            }

        })
        scrollListener = object : RecyclerView.OnScrollListener() {
            private var totalItemCount = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                totalItemCount = recyclerView.layoutManager?.itemCount!!
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        mRecyclerView.addOnScrollListener(scrollListener)
    }

    private fun initDataBinding() {
        mainViewModel.imageDataList.observe(viewLifecycleOwner, {
            mAdapter = MainAdapter(requireContext(), it)
            mAdapter.itemClick = object : MainAdapter.ItemClick {
                override fun onClick(view: View, imgItem: ImageData) {
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                        replace<DetailFragment>(R.id.fragment_container)
                    }
                }

            }
            mRecyclerView.adapter = mAdapter
            mRecyclerView.adapter!!.notifyDataSetChanged()
        })
    }

    private fun afterDataBinding() {

    }
}