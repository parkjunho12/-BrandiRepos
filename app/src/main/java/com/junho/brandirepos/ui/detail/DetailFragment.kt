package com.junho.brandirepos.ui.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.commit
import coil.Coil
import coil.request.LoadRequest
import com.junho.brandirepos.R
import com.junho.brandirepos.ui.main.adapter.data.ImageData

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
                    setCustomAnimations(0, R.anim.anim_slide_out_bottom)
                    hide(this@DetailFragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        if (arguments != null) {
            imageData = requireArguments().getSerializable("imageData") as ImageData
        }

        view.findViewById<ImageButton>(R.id.back_button_detail).setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(0, R.anim.anim_slide_out_bottom)
                hide(this@DetailFragment)
            }
        }
        setImages(view, imageData)
        setTextViews(view, imageData)

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

    private fun setTextViews(view: View, imageData: ImageData) {
        val tvSiteName = view.findViewById<TextView>(R.id.detail_sitename)
        val tvDateTime = view.findViewById<TextView>(R.id.detail_datetime)
        if (imageData.display_sitename != "") {
            tvSiteName.visibility = View.VISIBLE
            tvSiteName.text = imageData.display_sitename
        } else {
            tvSiteName.visibility = View.GONE
        }

        if (imageData.datetime != "") {
            tvDateTime.visibility = View.VISIBLE
            tvDateTime.text = imageData.datetime
        } else {
            tvDateTime.visibility = View.GONE
        }

    }

    private fun setImages(view: View , imageData: ImageData) {
        if (imageData.height > imageData.width) {
            view.findViewById<ScrollView>(R.id.scroll_view).visibility = View.VISIBLE
            imageView = view.findViewById(R.id.scroll_img_detail)
            view.findViewById<ImageView>(R.id.img_detail).visibility = View.GONE
        } else {
            view.findViewById<ScrollView>(R.id.scroll_view).visibility = View.GONE
            imageView = view.findViewById(R.id.img_detail)
            imageView.visibility = View.VISIBLE
        }
        val imageLoader = Coil.imageLoader(view.context)
        val request = LoadRequest.Companion.Builder(view.context)
            .data(Uri.parse(imageData.image_url))
            .target(imageView)
            .size(width = imageData.width, height = imageData.height)
            .build()
        imageLoader.execute(request)
    }

}