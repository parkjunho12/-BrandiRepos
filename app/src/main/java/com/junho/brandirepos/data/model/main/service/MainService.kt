package com.junho.brandirepos.data.model.main.service

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MainService {
    companion object {
        const val SERVER_URL = "https://dapi.kakao.com/"
        const val REST_API_KEY = "0f65095edc343d9c4e5394557fa5d8b8"
        enum class Sort {
            ACCURACY, RECENCY
        }
    }

    @Headers("Authorization: KakaoAK $REST_API_KEY")
    @GET("/v2/search/image")
    fun getImages(
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 30
    ) : Single<ResponseBody>
}