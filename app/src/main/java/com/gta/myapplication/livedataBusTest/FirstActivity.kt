package com.gta.myapplication.livedataBusTest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gta.myapplication.R

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.button3).setOnClickListener{
            LiveDataBus.post(EventKeys.NORMAL_EVENT, "普通事件: ${System.currentTimeMillis()}")
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            LiveDataBus.postSticky(EventKeys.STICKY_EVENT, "粘性事件: ${System.currentTimeMillis()}")
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}