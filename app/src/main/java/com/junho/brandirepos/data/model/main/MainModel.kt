package com.junho.brandirepos.data.model.main

import com.junho.brandirepos.data.format.request.ReqHeader
import com.junho.brandirepos.data.model.main.service.MainService
import io.reactivex.Single
import okhttp3.ResponseBody

interface MainModel {
    fun getImageData(query: String, sort: MainService.Companion.Sort, page: Int): Single<ResponseBody>
}