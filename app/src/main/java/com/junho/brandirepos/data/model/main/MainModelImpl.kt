package com.junho.brandirepos.data.model.main

import com.junho.brandirepos.data.format.request.ReqHeader
import com.junho.brandirepos.data.model.main.service.MainService
import io.reactivex.Single
import okhttp3.ResponseBody

class MainModelImpl(private val mainService: MainService): MainModel {
    override fun getImageData(query: String, sort: MainService.Companion.Sort, page: Int): Single<ResponseBody> {
        val sortString =
            if (sort == MainService.Companion.Sort.ACCURACY) {
                "accuracy"
            } else {
                "recency"
            }
        return mainService.getImages(query, sortString, page)
    }
}