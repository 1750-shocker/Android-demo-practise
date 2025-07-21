package com.gta.myapplication.observeForeverTest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityObserveBinding

class ObserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObserveBinding
    private val viewModel: ObserveViewModel by viewModels()
    private val foreverObserver = Observer<Int> { value ->
        Log.d(TAG, "observerForever: value = $value")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_observe)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(TAG, "--- MainActivity Created ---")
        viewModel.timerData.observe(this, Observer { value ->
            Log.d(TAG, "onCreate: observer:value is ${value}. Lifecycle: ${lifecycle.currentState}")
            binding.textView.text = "observe value:$value"
        })
        viewModel.timerData.observeForever(foreverObserver)
        binding.closeButton.setOnClickListener{
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        //手动移除observeForever
        viewModel.timerData.removeObserver(foreverObserver)
        Log.d(TAG, "onDestroy: 手动移除了observeForever")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }
    companion object {
        private const val TAG = "ObserveDemo"
    }
}