package com.junho.brandirepos.koinexam.usecase

import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.data.repository.main.MainRepository
import io.reactivex.Single
import okhttp3.ResponseBody

class MainUseCase(private val mainRepository: MainRepository) {

    fun getImageDatas(query: String, sort: MainService.Companion.Sort, page: Int): Single<ResponseBody> {
        mainRepository.getImageData(query, sort, page)
        return mainRepository.getImageData(query, sort, page)
    }

}