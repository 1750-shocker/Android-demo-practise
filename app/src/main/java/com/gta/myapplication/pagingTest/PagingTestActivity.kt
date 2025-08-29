package com.gta.myapplication.pagingTest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gta.myapplication.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingTestActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paging_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        swipeRefresh = findViewById(R.id.swipe_refresh)
        recyclerView.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()
        observeUsers()
        setupSwipeRefresh()
    }
    private fun setupRecyclerView() {
        adapter = UserAdapter()

        // 添加加载状态适配器
        val loadingAdapter = LoadingStateAdapter { adapter.retry() }

        // 将主适配器和加载状态适配器组合
        recyclerView.adapter = adapter.withLoadStateFooter(loadingAdapter)

        // 监听加载状态
        adapter.addLoadStateListener { loadState ->
            // 可以根据加载状态更新UI，比如显示/隐藏进度条
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    // 显示刷新进度条
                }
                is LoadState.NotLoading -> {
                    // 隐藏刷新进度条
                }
                is LoadState.Error -> {
                    // 显示错误信息
                }
            }
        }
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            viewModel.users.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        adapter.addLoadStateListener { loadState ->
            swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
        }
    }
}