package com.gta.myapplication.recyclerViewTest

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LastItemDecoration(private val bottomMargin: Int) : RecyclerView.ItemDecoration() {

    //当 RecyclerView 测量(measure)和布局(layout)子视图时
    //在 RecyclerView 首次加载数据时
    //当数据发生变化导致列表需要重新布局时
    //当屏幕旋转或布局尺寸变化时
    //recyclerView.adapter = adapter // 设置适配器时
    //adapter.notifyDataSetChanged() // 数据变化时
    //recyclerView.layoutManager = layoutManager // 切换布局管理器时
    //recyclerView.invalidate() // 强制重绘时
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if (itemCount == 0) return

        val layoutManager = parent.layoutManager

        // 处理 GridLayoutManager (横屏模式)
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val itemsInLastRow = itemCount % spanCount
            val lastRowStart = itemCount - (if (itemsInLastRow == 0) spanCount else itemsInLastRow)

            // 如果是最后一行的元素
            if (position >= lastRowStart) {
                outRect.bottom = bottomMargin
            }
        }
        // 处理 LinearLayoutManager (竖屏模式)
        else {
            // 如果是最后一个元素
            if (position == itemCount - 1) {
                outRect.bottom = bottomMargin
            }
        }
    }
}
