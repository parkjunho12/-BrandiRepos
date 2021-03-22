package com.junho.brandirepos.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar
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
    lateinit var circlerView: CircularProgressBar
    private var pageCount = 1
    private var queryText = ""
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
        circlerView = view.findViewById(R.id.circularProgressBar)

        val searchBar = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_bar)
        setTextListener(searchBar)
        setScrollListener()
        mAdapter = MainAdapter(requireContext(), mainViewModel.imageDataList.value!!)
        mRecyclerView.adapter = mAdapter
    }

    private fun initDataBinding() {
        mainViewModel.imageDataList.observe(viewLifecycleOwner, {
            mAdapter.itemClick = object : MainAdapter.ItemClick {
                override fun onClick(view: View, imgItem: ImageData) {
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                        replace<DetailFragment>(R.id.fragment_container)
                    }
                }

            }
            circlerView.visibility = View.GONE
            mRecyclerView.adapter!!.notifyDataSetChanged()
            mRecyclerView.addOnScrollListener(scrollListener)
        })
    }

    private fun setTextListener(searchView: androidx.appcompat.widget.SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
                                    mRecyclerView.addOnScrollListener(scrollListener)
                                    pageCount = 1
                                    queryText = newText
                                    mainViewModel.getImageData(queryText, MainService.Companion.Sort.ACCURACY, pageCount++)

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
                    mRecyclerView.addOnScrollListener(scrollListener)
                    pageCount = 1
                    queryText = query
                    mainViewModel.getImageData(queryText, MainService.Companion.Sort.ACCURACY, pageCount++)

                }
                return false
            }

        })
    }

    private fun setScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            private var firstVisibleItem = 0
            private var visibleItemCount = 0
            private var totalItemCount = 0
            private var previousTotal = 0
            private var loading = true
            private var visibleThreshold = 2
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false
                            previousTotal = totalItemCount
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                        if (pageCount == 51) {
                            Toast.makeText(requireContext(), "모든 페이지를 검색 하였습니다.", Toast.LENGTH_LONG).show()
                            return
                        }
                        // 끝에 도달 했을 때
                        CoroutineScope(Dispatchers.Main).launch {
                            circlerView.visibility = View.VISIBLE
                            recyclerView.removeOnScrollListener(scrollListener)
                            mainViewModel.getImageData(queryText, MainService.Companion.Sort.ACCURACY, pageCount++)
                        }
                        loading = true
                    }
                }
            }
        }
    }
}