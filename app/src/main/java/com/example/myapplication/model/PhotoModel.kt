package com.example.myapplication.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoModel(application: Application, byteArray: ByteArray): ViewModel() {
    val liveData = MutableLiveData<ByteArray>()

    init{
        liveData.value = byteArray
    }
}