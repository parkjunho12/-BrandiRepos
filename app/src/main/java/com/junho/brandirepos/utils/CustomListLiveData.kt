package com.junho.brandirepos.utils

import androidx.lifecycle.MutableLiveData
import com.junho.brandirepos.data.repository.main.MainRepository
import com.junho.brandirepos.ui.main.adapter.data.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomListLiveData(arrayList: ArrayList<ImageData>): MutableLiveData<ArrayList<ImageData>>() {
    private var imageArrayList = arrayList

    init {
        value = imageArrayList
    }

    suspend fun deleteAllList() {
        imageArrayList.clear()
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }

    suspend fun addImage(imageData: ImageData) {
        imageArrayList.add(imageData)
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }

    suspend fun deleteImage(repository: MainRepository, imageData: ImageData) {

        imageArrayList.remove(imageData)
        withContext(Dispatchers.Main) {
            value = imageArrayList
        }
    }
}