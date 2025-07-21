package com.gta.myapplication.itemDecorationTest

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(
    private val dividerHeight: Int = 4,
    private val mcolor: Int = Color.GRAY,
    private val paddingStart: Int = 16,
    private val paddingEnd: Int = 16
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        this.color = mcolor
        style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // 为每个item下方添加间距（除了最后一个）
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) { // 不绘制最后一个item的分割线
            val child = parent.getChildAt(i)

            val left = parent.paddingLeft + paddingStart
            val right = parent.width - parent.paddingRight - paddingEnd
            val top = child.bottom
            val bottom = top + dividerHeight

            c.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                paint
            )
        }
    }
}