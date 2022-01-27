package com.example.myapplication

import android.app.Application
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(app: Application, val text: String) : AndroidViewModel(app) {

    val liveData = MutableLiveData<String>()

    init {
        startTimer()
    }

    fun startTimer(){
        object: CountDownTimer(10000, 1000){
            override fun onFinish() {
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show()
            }

            override fun onTick(millisUntilFinished: Long) {
                liveData.value = (millisUntilFinished/1000).toString()
            }
        }.start()
    }
}