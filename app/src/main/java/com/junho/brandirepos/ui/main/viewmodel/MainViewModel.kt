package com.junho.brandirepos.ui.main.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.junho.brandirepos.data.format.response.RspMainService
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.data.repository.main.MainRepository
import com.junho.brandirepos.ui.main.adapter.data.ImageData
import com.junho.brandirepos.utils.CustomListLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _imageDataList = CustomListLiveData(ArrayList<ImageData>())
    val imageDataList: LiveData<ArrayList<ImageData>>
        get() = _imageDataList

    suspend fun deleteImages() {
        _imageDataList.deleteAllList()
    }


    @SuppressLint("CheckResult")
    fun getImageData(query: String, sort:MainService.Companion.Sort, page: Int) {
        mainRepository.getImageData(query, sort, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run {
                    distributeRsp(this)
                }
            }, {
                Log.d(ContentValues.TAG, "response error, message : ${it.message}")
            })
    }

    private fun distributeRsp(response: ResponseBody) {
        var jsonString = response.string()
        jsonString = jsonString.replace("\\", "").replace("\"{", "{")
        val jsonObj: JSONObject? = try {
            JSONObject(jsonString)
        } catch (ex: java.lang.Exception) {
            null
        }

        val rspMainService = Gson().fromJson(jsonObj.toString(), RspMainService::class.java)

        Log.d("response body", rspMainService.toString())
        val documents = rspMainService.documents

        for (document in documents) {
            val imageData = ImageData(document.collection, document.thumbnail_url, document.image_url, document.width, document.height, document.display_sitename, document.doc_url, document.datetime)
            viewModelScope.launch {
                _imageDataList.addImage(imageData)
            }
        }
    }

}