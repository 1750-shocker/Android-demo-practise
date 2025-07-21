package com.gta.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.gta.myapplication.contentResolverTest.ContentResolverActivity
import com.gta.myapplication.databinding.ActivityMainBinding
import com.gta.myapplication.debugDemo.DeBugActivity
import com.gta.myapplication.flatMapLatestTest.FlatMapActivity
import com.gta.myapplication.flatMapLatestTest.NestedCollectActivity
import com.gta.myapplication.itemDecorationTest.TestDecorationActivity
import com.gta.myapplication.observeForeverTest.ObserveActivity
import com.gta.myapplication.recyclerViewTest.ListTestActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.button.setOnClickListener {
            startActivity(Intent(this, NestedCollectActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, ListTestActivity::class.java))
        }
    }
}