package com.gta.myapplication.recyclerViewTest

import com.gta.myapplication.databinding.ActivityMainBinding
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityListTestBinding

class ListTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListTestBinding
    private lateinit var viewModel: ListTestViewModel
    private lateinit var adapter: ItemAdapter

    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_test)
        viewModel = ViewModelProvider(this)[ListTestViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupFabMenu()
        observeViewModel()
        setupTouchInterceptor()
    }

    private fun setupRecyclerView() {

        // 1. 滑动删除要用的，创建Callback实例，并将事件委托给ViewModel
        //第一个是长按拖动，和那个onLongClick冲突了，所以我在内部禁用了这个
        val itemTouchHelperCallback = MyItemTouchHelperCallback(
            onItemMove = { fromPosition, toPosition ->
                // 当拖拽发生时，通知ViewModel
                viewModel.onItemMoved(fromPosition, toPosition)
                // **重要**: 立即通知adapter，以获得流畅的拖拽动画
                // ViewModel更新数据后，submitList会最终修正状态
                adapter.notifyItemMoved(fromPosition, toPosition)
            },
            onItemSwiped = { position ->
                // 当滑动发生时，通知ViewModel
                viewModel.onItemDeleted(position)
            }
        )
        // 2. 创建ItemTouchHelper并附加到RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        // 传入删除项目的回调，这个就是删除按钮的逻辑
        adapter = ItemAdapter { itemId ->
            viewModel.deleteItem(itemId)
        }

        binding.recyclerView.adapter = adapter

        // 根据屏幕方向选择布局管理器
        setLayoutManagerBasedOnOrientation()

        // 添加 ItemDecoration
        val bottomMargin = resources.getDimensionPixelSize(R.dimen.last_item_bottom_margin)
        binding.recyclerView.addItemDecoration(LastItemDecoration(bottomMargin))
    }

    // 设置全局触摸拦截器，点击空白区域时隐藏删除按钮
    private fun setupTouchInterceptor() {
        binding.touchInterceptor.setOnClickListener {
            // 如果当前有展开的 FAB 菜单，关闭它
            if (isFabMenuOpen) {
                toggleFabMenu()
            }

            // 触发适配器刷新，确保所有项目的删除按钮都被隐藏
            adapter.notifyDataSetChanged()
        }
    }

    private fun setLayoutManagerBasedOnOrientation() {
        binding.recyclerView.layoutManager = if (resources.configuration.orientation ==
            Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏使用 GridLayoutManager
            GridLayoutManager(this, 3)
        } else {
            // 竖屏使用 LinearLayoutManager
            LinearLayoutManager(this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 屏幕方向改变时更新布局管理器
        setLayoutManagerBasedOnOrientation()
    }

    private fun setupFabMenu() {
        // 设置主按钮点击事件
        binding.fabMain.setOnClickListener {
            toggleFabMenu()
        }

        // 设置各个功能按钮的点击事件
        binding.fabAddAll.setOnClickListener {
            viewModel.addAllItems()
            toggleFabMenu()
        }

        binding.fabAddOne.setOnClickListener {
            viewModel.addOneItem()
            toggleFabMenu()
        }

        binding.fabDeleteAll.setOnClickListener {
            viewModel.deleteAllItems()
            toggleFabMenu()
        }
    }

    private fun toggleFabMenu() {
        isFabMenuOpen = if (!isFabMenuOpen) {
            // 展开菜单
            binding.fabAddAll.show()
            binding.fabAddOne.show()
            binding.fabDeleteAll.show()
            true
        } else {
            // 收起菜单
            binding.fabAddAll.hide()
            binding.fabAddOne.hide()
            binding.fabDeleteAll.hide()
            false
        }
    }

    private fun observeViewModel() {
        viewModel.items.observe(this) { items ->
            adapter.submitList(items.toList())
        }
    }
}
