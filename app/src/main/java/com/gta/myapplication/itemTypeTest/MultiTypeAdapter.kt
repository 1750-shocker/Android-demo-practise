package com.gta.myapplication.itemTypeTest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gta.myapplication.R

class MultiTypeAdapter : ListAdapter<ListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val VIEW_TYPE_EXPANDABLE = 1
        const val VIEW_TYPE_GRID = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ExpandableItem -> VIEW_TYPE_EXPANDABLE
            is GridItem -> VIEW_TYPE_GRID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EXPANDABLE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_expandable, parent, false)
                ExpandableViewHolder(view)
            }
            VIEW_TYPE_GRID -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid, parent, false)
                GridViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExpandableViewHolder -> holder.bind(getItem(position) as ExpandableItem)
            is GridViewHolder -> holder.bind(getItem(position) as GridItem)
        }
    }

    class ExpandableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
        private var isExpanded = false
        private var needsExpansion = false

        fun bind(item: ExpandableItem) {
            titleText.text = item.title
            contentText.text = item.content
            isExpanded = false

            // 重置为收起状态
            updateExpandState()

            // 检查是否需要展开功能
            contentText.post {
                checkIfExpansionNeeded()
            }

            expandIcon.setOnClickListener {
                if (needsExpansion) {
                    isExpanded = !isExpanded
                    updateExpandState()
                }
            }
        }

        private fun checkIfExpansionNeeded() {
            // 临时设置为无限行数来测量完整文本
            val maxLines = contentText.maxLines
            contentText.maxLines = Int.MAX_VALUE

            // 强制重新测量
            contentText.measure(
                View.MeasureSpec.makeMeasureSpec(contentText.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            val lineCount = contentText.lineCount
            needsExpansion = lineCount > 2

            // 恢复原来的行数限制
            contentText.maxLines = maxLines

            // 根据是否需要展开来显示/隐藏箭头
            expandIcon.visibility = if (needsExpansion) View.VISIBLE else View.INVISIBLE
        }

        private fun updateExpandState() {
            if (isExpanded) {
                contentText.maxLines = Int.MAX_VALUE
                expandIcon.rotation = 180f
            } else {
                contentText.maxLines = 2
                expandIcon.rotation = 0f
            }
        }
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.gridTitle)
        private val descText: TextView = itemView.findViewById(R.id.gridDesc)

        fun bind(item: GridItem) {
            titleText.text = item.title
            descText.text = item.description
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }
}