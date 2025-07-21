package com.gta.myapplication.recyclerViewTest


import androidx.recyclerview.widget.DiffUtil

/**
 * 当 ListTestViewModel 更新 _items.value 时
 * ListAdapter 收到新数据列表
 * DiffUtil 自动比较新旧列表：
 * 先对所有项调用 areItemsTheSame() 匹配对应关系
 * 对匹配的项调用 areContentsTheSame() 检查是否需要更新
 * 根据比较结果：
 * 添加/删除/移动相应项
 * 标记需要更新的项（触发 onBindViewHolder）
 * 这种机制可以最小化不必要的视图更新，提高 RecyclerView 的性能。
 */
class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    //在调用 ListAdapter.submitList() 或 notifyDataSetChanged() 时触发
    //用于判断两个数据项是否代表同一个实体
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }
//只有在 areItemsTheSame() 返回 true 后才会调用
//用于判断两个数据项的内容是否完全相同
//如果返回 false，会触发该位置的重新绑定（调用 onBindViewHolder）
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}
