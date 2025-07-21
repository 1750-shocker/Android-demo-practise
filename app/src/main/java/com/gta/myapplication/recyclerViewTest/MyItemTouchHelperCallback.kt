package com.gta.myapplication.recyclerViewTest

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * 自定义的ItemTouchHelper.Callback，用于处理拖拽和滑动事件。
 *
 * @param adapter 你需要操作数据源的Adapter
 * @param onSwipedCallback 一个回调函数，当Item被滑掉时调用，通知外部执行删除操作
 */
//新 Callback 不再关心 Adapter 是谁，它只负责两件事
//定义拖拽和滑动的方向，将发生的“移动”和“滑动”事件，通过回调函数（Lambda）暴露出去
class MyItemTouchHelperCallback(
    private val onItemMove: (fromPosition: Int, toPosition: Int) -> Unit,
    private val onItemSwiped: (position: Int) -> Unit
) : ItemTouchHelper.Callback() {

    /**
     * 指定可以支持的拖拽和滑动方向。
     * 这是最重要的方法之一。
     * 使用 makeMovementFlags(dragFlags, swipeFlags) 来构造返回值。
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
    }

    /**
     * 当一个Item被拖拽到另一个Item的位置上时调用。
     *
     * @param recyclerView 关联的RecyclerView
     * @param viewHolder 被拖拽的ViewHolder
     * @param target 拖拽的目标ViewHolder
     * @return 如果你处理了移动操作，返回true；否则返回false。
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        // 将移动事件回调出去
        onItemMove(fromPosition, toPosition)
        return true
    }

    /**
     * 当一个Item被滑动时调用。
     *
     * @param viewHolder 被滑动的ViewHolder
     * @param direction 滑动的方向
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        // 将滑动事件回调出去
        onItemSwiped(position)
    }

    // --- 以下是可选的重写方法，用于自定义UI效果 ---

    /**
     * 当Item被拖拽或滑动，状态改变时调用。
     * 可以在这里改变Item的背景色，以提供视觉反馈。
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.alpha = 0.4f // 拖拽时变半透明
        }
    }

    /**
     * 当用户与Item的交互结束（松手），并且Item的动画也完成后调用。
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.alpha = 1.0f // 松手后恢复
    }

    /**
     * 返回是否支持长按拖拽。默认为true。
     * 如果返回false，你需要自己调用 itemTouchHelper.startDrag(viewHolder) 来手动触发拖拽。
     */
    override fun isLongPressDragEnabled(): Boolean = false

    /**
     * 返回是否支持滑动。默认为true。
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
}