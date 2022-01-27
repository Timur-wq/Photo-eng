package com.example.myapplication.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoFactory(val application: Application, val byteArray: ByteArray) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoModel(application, byteArray) as T
    }
}