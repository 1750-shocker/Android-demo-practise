package com.gta.myapplication.observeForeverTest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import kotlin.concurrent.timerTask

class ObserveViewModel: ViewModel() {
    val timerData = MutableLiveData<Int>()
    private var count = 0
    private val timer = Timer()
    init{
        //每秒更新livedata
        timer.scheduleAtFixedRate(timerTask{
            count++
            timerData.postValue(count)
        }, 0, 1000)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.d(TAG, "onCleared: ViewModel cleared and timer cancelled.")
    }
    companion object{
        private const val TAG = "ObserveDemo"
    }
}