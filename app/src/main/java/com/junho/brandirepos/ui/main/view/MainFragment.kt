package com.junho.brandirepos.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.junho.brandirepos.R
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.ui.detail.DetailFragment
import com.junho.brandirepos.ui.main.adapter.MainAdapter
import com.junho.brandirepos.ui.main.adapter.data.ImageData
import com.junho.brandirepos.ui.main.viewmodel.MainViewModel
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.koin.android.ext.android.inject
import java.util.*

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter
    private lateinit var tvNoResult: TextView
    private lateinit var floatingActionButton: FloatingActionButton
    lateinit var scrollListener: RecyclerView.OnScrollListener
    lateinit var circlerView: CircularProgressBar
    private var pageCount = 1
    private var queryText = ""
    private var previousTotal = 0
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initStartView(root)
        initDataBinding()
        afterDataBinding()
        return root
    }

    private fun initStartView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_main)
        gridLayoutManager = GridLayoutManager(requireContext(), 3)
        mRecyclerView.layoutManager = gridLayoutManager
        circlerView = view.findViewById(R.id.circularProgressBar)
        tvNoResult = view.findViewById<TextView>(R.id.tv_no_result)
        floatingActionButton = view.findViewById(R.id.fab_button)
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
                        setCustomAnimations(R.anim.anim_slide_in_bottom, 0)
                        val objBundle = Bundle()
                        objBundle.putSerializable("imageData", imgItem)
                        add<DetailFragment>(R.id.fragment_container, "imageData", objBundle)
                    }
                }

            }
            circlerView.visibility = View.GONE
            mRecyclerView.adapter!!.notifyDataSetChanged()
        })
        mainViewModel.isToastOn.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it) {
                    Toast.makeText(requireContext(), getString(R.string.no_search_result), Toast.LENGTH_LONG).show()
                    tvNoResult.visibility = View.VISIBLE
                } else {
                    tvNoResult.visibility = View.GONE
                }
            }
        })

        mainViewModel.isEnd.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it) {
                    mRecyclerView.removeOnScrollListener(scrollListener)
                    Toast.makeText(requireContext(), "모든 페이지를 검색 하였습니다.", Toast.LENGTH_LONG).show()
                } else {
                    mRecyclerView.addOnScrollListener(scrollListener)
                }
            }
        })
    }

    private fun afterDataBinding() {
        floatingActionButton.setOnClickListener {
            view -> mRecyclerView.smoothScrollToPosition(0)
        }
    }

    private fun setTextListener(searchView: androidx.appcompat.widget.SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private val textingQueue: Queue<Boolean> = LinkedList()

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("test", newText)
                textingQueue.add(true)
                Handler(Looper.getMainLooper()).postDelayed(
                    Runnable {
                        if (textingQueue.isNotEmpty()) {
                            textingQueue.poll()
                        }
                        if (textingQueue.isEmpty()){
                            mainViewModel.deleteImages()
                            mRecyclerView.addOnScrollListener(scrollListener)
                            pageCount = 1
                            queryText = newText
                            previousTotal = 0
                            mainViewModel.getImageData(
                                queryText,
                                MainService.Companion.Sort.ACCURACY,
                                pageCount++
                            )
                        }
                    },1000
                )
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {

//                    mainViewModel.deleteImages()
//                    mRecyclerView.addOnScrollListener(scrollListener)
//                    pageCount = 1
//                    queryText = query
//                    previousTotal = 0
//                    mainViewModel.getImageData(queryText, MainService.Companion.Sort.ACCURACY, pageCount++)

                return false
            }

        })
    }

    private fun setScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            private var firstVisibleItem = 0
            private var visibleItemCount = 0
            private var totalItemCount = 0
            private var loading = true
            private var visibleThreshold = 2
            private val scrollQueue: Queue<Boolean> = LinkedList()

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
                            recyclerView.removeOnScrollListener(scrollListener)
                            Toast.makeText(requireContext(), "모든 페이지를 검색 하였습니다.", Toast.LENGTH_LONG).show()
                            return
                        }
                        // 끝에 도달 했을 때
                        scrollQueue.add(true)
                        Handler(Looper.getMainLooper()).postDelayed(
                            Runnable {
                                if (scrollQueue.isNotEmpty()) {
                                    scrollQueue.poll()
                                }
                                if (scrollQueue.isEmpty()){
                                    circlerView.visibility = View.VISIBLE
                                    recyclerView.removeOnScrollListener(scrollListener)
                                    mainViewModel.getImageData(queryText, MainService.Companion.Sort.ACCURACY, pageCount++)
                                }
                            },200
                        )
                        loading = true
                    }
                }
            }
        }
    }
}