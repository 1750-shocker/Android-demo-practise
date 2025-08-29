package com.gta.myapplication.livedataBusTest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.gta.myapplication.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tv_normal_event = findViewById<TextView>(R.id.textView2)
        val tv_sticky_event = findViewById<TextView>(R.id.textView3)
        val btn_toggle_lifecycle = findViewById<Button>(R.id.button6)
        // 观察普通事件（不会收到注册前发送的事件）
        LiveDataBus.observeEvent<String>(this, EventKeys.NORMAL_EVENT) { msg ->
            tv_normal_event.text = "收到普通事件: $msg"
            Log.d("LiveDataBus", "普通事件: $msg")
        }

        // 观察粘性事件（会立即收到最后一次事件）
        LiveDataBus.observeStickyEvent<String>(this, EventKeys.STICKY_EVENT) { msg ->
            tv_sticky_event.text = "收到粘性事件: $msg"
            Log.d("LiveDataBus", "粘性事件: $msg")
        }

        // 模拟生命周期变化（测试自动注销）
        btn_toggle_lifecycle.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                moveTaskToBack(true) // 进入后台
            } else {
                moveTaskToBack(false) // 回到前台
            }
        }
    }
}