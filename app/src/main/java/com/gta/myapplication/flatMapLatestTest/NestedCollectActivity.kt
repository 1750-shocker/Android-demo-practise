package com.gta.myapplication.flatMapLatestTest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityNestedCollectBinding
import com.gta.myapplication.databinding.LayoutLogBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NestedCollectActivity : AppCompatActivity() {
    private lateinit var binding: LayoutLogBinding
    private val viewModel by viewModels<OrderViewModel>()
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_log)
        binding.button.setOnClickListener { startFlow() }
        binding.button2.setOnClickListener { stopFlow() }
    }

    private fun startFlow() {
        binding.tvLog.text = ""
        job = lifecycleScope.launch {
            viewModel.getOrdersNested()
                .collect { order ->  // 直接在这里处理每个值
                    binding.tvLog.append("${getCurrentTime()} $order\n")
                }
        }
    }

    private fun stopFlow() {
        job?.cancel()
    }

    private fun getCurrentTime() = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        .format(Date())
}