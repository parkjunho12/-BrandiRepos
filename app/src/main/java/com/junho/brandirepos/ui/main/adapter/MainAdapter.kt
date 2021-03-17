package com.junho.brandirepos.ui.main.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.LoadRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.junho.brandirepos.R
import com.junho.brandirepos.ui.main.adapter.data.ImageData

class MainAdapter(private val context: Context, private val itemList: ArrayList<ImageData>):
        RecyclerView.Adapter<MainAdapter.Holder>(){

    var itemClick: ItemClick? = null
    interface ItemClick
    {
        fun onClick(
            view: View,
            position: Int
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item_image, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], position)
        if (itemClick != null){
            holder.itemView.setOnClickListener { v -> itemClick?.onClick(v, position)!! }
        }
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val img = itemView?.findViewById<ImageView>(R.id.img)
        fun bind (image: ImageData, position: Int) {

            val imageLoader = Coil.imageLoader(itemView.context)
            val request = LoadRequest.Companion.Builder(itemView.context)
                .data(Uri.parse(image.imageString))
                .target(img!!)
                .scale(Scale.FIT)
                .transformations(RoundedCornersTransformation(25f))
                .build()
            imageLoader.execute(request)
        }

    }

}



