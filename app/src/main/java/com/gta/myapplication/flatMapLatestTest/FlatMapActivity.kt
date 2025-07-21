package com.gta.myapplication.flatMapLatestTest

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityFlatMapBinding
import com.gta.myapplication.databinding.LayoutLogBinding
import com.gta.myapplication.databinding.LayoutManualBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FlatMapActivity : AppCompatActivity() {
    private lateinit var binding: LayoutManualBinding
    private val viewModel by viewModels<ManualUserViewModel>()
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_manual)
        setupClickListeners()
        startCollecting()
    }
    private fun setupClickListeners() {
        binding.btnUserA.setOnClickListener { switchUser("用户A") }
        binding.btnUserB.setOnClickListener { switchUser("用户B") }
        binding.btnUserC.setOnClickListener { switchUser("用户C") }
        binding.btnStop.setOnClickListener { stopCurrentUser() }
    }

    private fun switchUser(user: String) {
        lifecycleScope.launch {
            viewModel.switchUser(user)
            binding.logView.append("=== 已切换到 $user ===\n")
        }
    }

    private fun stopCurrentUser() {
        lifecycleScope.launch {
            viewModel.switchUser("") // 发送空用户停止当前流
            binding.logView.append("=== 已停止当前用户 ===\n")
        }
    }

    private fun startCollecting() {
        job = lifecycleScope.launch {
            viewModel.orders.collect { order ->
                binding.logView.append("${getCurrentTime()} $order\n")
                binding.scrollView.post {
                    binding.scrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    private fun getCurrentTime() = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        .format(Date())

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
    /*private fun startFlow() {
        binding.tvLog.text = ""
        job = lifecycleScope.launch {
            viewModel.getOrdersFlatMap()
                .collect { order ->  // 直接在这里处理每个值
                    binding.tvLog.append("${getCurrentTime()} $order\n")
                }
        }
    }

    private fun stopFlow() {
        job?.cancel()
    }

    private fun getCurrentTime() = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        .format(Date())*/
}