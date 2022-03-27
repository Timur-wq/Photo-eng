package com.example.myapplication.photoViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoModel(application: Application, byteArray: ByteArray): ViewModel() {
    val liveData = MutableLiveData<ByteArray>()

    init{
        liveData.value = byteArray
    }
}