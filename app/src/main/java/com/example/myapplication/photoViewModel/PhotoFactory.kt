package com.example.myapplication.photoViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoFactory(val application: Application, val byteArray: ByteArray) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoModel(application, byteArray) as T
    }
}