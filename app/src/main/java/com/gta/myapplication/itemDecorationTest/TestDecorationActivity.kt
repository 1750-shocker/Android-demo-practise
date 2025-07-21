package com.gta.myapplication.itemDecorationTest

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityTestDecorationBinding


class TestDecorationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestDecorationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_decoration)
        setContentView(binding.root)

        // 准备数据
        val data = List(20) { index -> "Item ${index + 1}" }

        // 设置RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TestDecorationActivity)
            adapter = MyAdapter(data)

            // 添加自定义的ItemDecoration
            addItemDecoration(
                DividerItemDecoration(
                    dividerHeight = 40,
                    mcolor = Color.parseColor("#E0E0E0"),
                    paddingStart = 16,
                    paddingEnd = 16
                )
                /*RoundCornerDecoration(
                    cornerRadius = 16f,
                    margin = 16f,
                    mcolor = Color.BLUE
                )*/
            )

            // 添加顶部和底部间距
            setPadding(0, 16, 0, 16)
            clipToPadding = true
        }
    }
}