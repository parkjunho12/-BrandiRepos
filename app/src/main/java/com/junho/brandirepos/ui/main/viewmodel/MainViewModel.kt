package com.junho.brandirepos.ui.main.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.data.repository.main.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    @SuppressLint("CheckResult")
    fun getImageData(query: String, sort:MainService.Companion.Sort, page: Int) {
        viewModelScope.launch {

        }
        mainRepository.getImageData(query, sort, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run {
                    Log.d("response body", this.string())
                }
            }, {
                Log.d(ContentValues.TAG, "response error, message : ${it.message}")
            })
    }
}