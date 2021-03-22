package com.junho.brandirepos.ui.main.adapter.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageData(
        val collection : String,
        val thumbnail_url : String,
        val image_url : String,
        val width : Int,
        val height : Int,
        val display_sitename : String,
        val doc_url : String,
        val datetime : String
) : Serializable