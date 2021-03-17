package com.junho.brandirepos.data.repository.main

import com.junho.brandirepos.data.model.main.service.MainService
import io.reactivex.Single
import okhttp3.ResponseBody

interface MainRepository {
    fun getImageData(query: String, sort: MainService.Companion.Sort, page: Int): Single<ResponseBody>
}