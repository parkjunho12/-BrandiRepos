package com.junho.brandirepos.data.repository.main

import android.content.ContentValues
import android.util.Log
import com.junho.brandirepos.data.model.main.MainModel
import com.junho.brandirepos.data.model.main.service.MainService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class MainRepositoryImpl(private val mainModel: MainModel) : MainRepository {

    override fun getImageData(query: String, sort: MainService.Companion.Sort, page: Int): Single<ResponseBody> {
       return mainModel.getImageData(query, sort, page)
    }
}