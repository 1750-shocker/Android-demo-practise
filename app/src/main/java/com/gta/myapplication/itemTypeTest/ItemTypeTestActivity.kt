package com.gta.myapplication.itemTypeTest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gta.myapplication.R

class ItemTypeTestActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MultiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_type_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)
        setupRecyclerView()
        loadSampleData()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)

        // 设置跨列规则
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    MultiTypeAdapter.VIEW_TYPE_EXPANDABLE -> 3 // 占据全部3列
                    MultiTypeAdapter.VIEW_TYPE_GRID -> 1      // 占据1列
                    else -> 1
                }
            }
        }

        adapter = MultiTypeAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun loadSampleData() {
        val sampleData = mutableListOf<ListItem>()

        // 添加可展开的文本项
        sampleData.add(ExpandableItem(
            1,
            "展开文本项目 1",
            "这是一个可以展开和收起的文本内容。当收起时只显示两行文字，点击向下箭头可以展开显示全部内容。这里有更多的文字内容来演示展开收起的效果。当文本内容很长时，用户可以选择查看完整内容或者保持简洁的两行显示。这个功能对于显示长文本内容非常有用，可以节省屏幕空间并提供更好的用户体验。"
        ))

        // 添加网格项
        for (i in 2..7) {
            sampleData.add(GridItem(i.toLong(), "网格项目 $i", "描述 $i"))
        }

        // 再添加一个可展开项
        sampleData.add(ExpandableItem(
            8,
            "展开文本项目 2",
            "另一个可展开的文本内容示例。这个内容也比较长，用来演示在RecyclerView中如何实现文本的展开和收起功能。用户可以通过点击箭头按钮来控制文本的显示状态。这种设计模式在很多应用中都有使用，特别是在需要显示大量文本信息但又要保持界面整洁的场景下。"
        ))

        // 添加更多网格项
        for (i in 9..15) {
            sampleData.add(GridItem(i.toLong(), "网格项目 $i", "描述 $i"))
        }

        adapter.submitList(sampleData)
    }
}