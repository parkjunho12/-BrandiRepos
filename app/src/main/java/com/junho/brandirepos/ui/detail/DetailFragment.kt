package com.junho.brandirepos.ui.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import coil.Coil
import coil.request.LoadRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.junho.brandirepos.R
import com.junho.brandirepos.ui.main.adapter.data.ImageData
import com.junho.brandirepos.ui.main.view.MainFragment

class DetailFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback
    companion object {
        fun newInstance() = DetailFragment()
    }
    private lateinit var imageData: ImageData

    private lateinit var viewModel: DetailViewModel
    private lateinit var imageView: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.commit {
                    hide(this@DetailFragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        if (arguments != null) {
            imageData = requireArguments().getSerializable("imageData") as ImageData
        }


        setImages(view, imageData)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setImages(view: View , imageData: ImageData) {
        if (imageData.height > imageData.width) {
            view.findViewById<ScrollView>(R.id.scroll_view).visibility = View.VISIBLE
            imageView = view.findViewById(R.id.scroll_img_detail)
        } else {
            view.findViewById<ScrollView>(R.id.scroll_view).visibility = View.GONE
            imageView = view.findViewById(R.id.img_detail)
        }
        val imageLoader = Coil.imageLoader(view.context)
        val request = LoadRequest.Companion.Builder(view.context)
            .data(Uri.parse(imageData.thumbnail_url))
            .target(imageView)
            .size(width = imageData.width, height = imageData.height)
            .build()
        imageLoader.execute(request)
    }

}