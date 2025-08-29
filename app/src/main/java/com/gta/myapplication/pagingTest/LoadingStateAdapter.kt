package com.gta.myapplication.pagingTest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class LoadingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        // 这里使用简单布局，实际项目中应该创建自定义布局
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return LoadingStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(
        itemView: View,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Loading -> {
                    textView.text = "加载中..."
                }
                is LoadState.Error -> {
                    textView.text = "加载失败，点击重试"
                    itemView.setOnClickListener { retry() }
                }
                is LoadState.NotLoading -> {
                    textView.text = ""
                }
            }
        }
    }
}