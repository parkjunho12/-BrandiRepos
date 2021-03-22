package com.junho.brandirepos.ui.main.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.logging.Handler

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _imageDataList = CustomListLiveData(ArrayList<ImageData>())
    val imageDataList: LiveData<ArrayList<ImageData>>
        get() = _imageDataList

    private val _isToastOn = MutableLiveData<Boolean>()
    val isToastOn: LiveData<Boolean>
        get() = _isToastOn


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

        val meta = rspMainService.meta

        if (meta.is_end) {
            return
        }

        Log.d("response body", rspMainService.toString())
        val documents = rspMainService.documents

        if (documents.isEmpty()) {
            viewModelScope.launch {
                _isToastOn.value = true
                android.os.Handler(Looper.getMainLooper()).postDelayed(
                    Runnable {
                        _isToastOn.value = false
                    },1000
                )
            }
            return
        }

        for (document in documents) {
            val imageData = ImageData(document.collection, document.thumbnail_url, document.image_url, document.width, document.height, document.display_sitename, document.doc_url, document.datetime)
            viewModelScope.launch {
                _imageDataList.addImage(imageData)
            }
        }
    }

}