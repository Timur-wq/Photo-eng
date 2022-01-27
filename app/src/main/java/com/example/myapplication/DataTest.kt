package com.example.myapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class DataTest(val context: Context): LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getData(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun sendData(){

    }


}