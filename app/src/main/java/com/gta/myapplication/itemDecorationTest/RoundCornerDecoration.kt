package com.gta.myapplication.itemDecorationTest
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RoundCornerDecoration(
    private val cornerRadius: Float = 16f,
    private val margin: Float = 16f,
    private val mcolor: Int = Color.WHITE
) : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = mcolor
        style = Paint.Style.FILL
    }
    private val rect = RectF()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(margin.toInt(), margin.toInt() / 2, margin.toInt(), margin.toInt() / 2)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            rect.set(
                child.left.toFloat(),
                child.top.toFloat() + margin / 2,
                child.right.toFloat(),
                child.bottom.toFloat() - margin / 2
            )

            c.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
    }
}