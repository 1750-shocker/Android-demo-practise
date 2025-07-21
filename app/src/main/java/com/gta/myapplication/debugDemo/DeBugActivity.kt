package com.gta.myapplication.debugDemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gta.myapplication.R

class DeBugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        // 加载 fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MyFragment())
            .commit()
    }
}