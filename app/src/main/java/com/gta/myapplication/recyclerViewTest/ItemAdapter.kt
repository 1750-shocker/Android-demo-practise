package com.gta.myapplication.recyclerViewTest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gta.myapplication.databinding.ItemLayoutBinding

class ItemAdapter(private val onDeleteItem: (Int) -> Unit) ://?什么是ListAdapter
    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    // 跟踪当前显示删除按钮的项目位置
    private var expandedPosition = RecyclerView.NO_POSITION
    //当 RecyclerView 需要新的条目视图时调用，这个方法里必须返回一个holder实例，可以传一些参数，holder内部会
    //进行细节处理
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //利用 View Binding 将 item_layout.xml 布局 inflate（膨胀）成 binding 对象
        //false 表示暂时不自动添加到父容器，RecyclerView 会自己处理
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding,
            onCardClick = { position ->
                // 点击外部区域隐藏删除按钮
                if (expandedPosition == position) {
                    expandedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(position)
                }
            },
            onLongClick = { position ->
                // 长按显示/隐藏删除按钮
                val previousExpanded = expandedPosition

                // 如果之前有展开的项目，先收起它
                if (previousExpanded != RecyclerView.NO_POSITION) {
                    expandedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(previousExpanded)
                }

                // 如果点击的不是已展开的项目，则展开它，用boolean控制databinding布局
                if (previousExpanded != position) {
                    expandedPosition = position
                    //通知 RecyclerView 指定位置(position)的数据已变更，触发对应位置的 onBindViewHolder() 方法重新执行
                    notifyItemChanged(position)
                }

                true // 返回 true 表示已处理长按事件
            },
            onDeleteItem = onDeleteItem
        )
    }

    //当用户滚动列表，新的项进入屏幕时调用，这个方法里必须更新holder的内容
    //当调用 notifyItemChanged() 或类似方法通知数据变化时，布局变化时
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)//调用ListAdapter的getItem方法获取当前项
        holder.bind(item, position == expandedPosition)
    }

    class ItemViewHolder(
        private val binding: ItemLayoutBinding,
        private val onCardClick: (Int) -> Unit,
        private val onLongClick: (Int) -> Boolean,
        private val onDeleteItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // setOnClickListener 没有返回值要求：
            //点击事件不需要返回值
            //可以直接调用回调函数，无需关心事件消费
            //内部处理完成后自动结束
            binding.root.setOnClickListener {
                // 使用 adapterPosition 获取当前位置
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCardClick(position)
                }
            }

            // setOnLongClickListener 需要明确返回一个Boolean值：
            //使用return@setOnLongClickListener是为了明确指定从哪个lambda返回，同时保留返回值
            //必须返回true表示消费了长按事件（阻止后续事件传递）
            //必须返回false表示未消费事件（允许继续传递）
            binding.root.setOnLongClickListener {
                // 使用 adapterPosition 获取当前位置
                val position = adapterPosition
                //验证是否有效
                if (position != RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener onLongClick(position)
                }
                false//如果位置无效返回false表示未处理
            }
        }
        //绑定数据到布局，并立即执行绑定刷新，不使用databinding，这个类里也要处理绑定和数据填充
        fun bind(item: Item, isExpanded: Boolean) {
            //数据赋值到布局
            binding.item = item
            // 控制删除按钮的可见性，两个布局的过渡动画会被自动执行
            binding.deleteVisible = isExpanded

            // 往布局里设置删除按钮点击事件
            binding.deleteClickListener = View.OnClickListener {
                onDeleteItem(item.id)
                // 阻止事件冒泡到卡片
                it.isClickable = true
            }
            //更新所有绑定到已修改变量的视图表达式
            binding.executePendingBindings()
        }
    }
}
