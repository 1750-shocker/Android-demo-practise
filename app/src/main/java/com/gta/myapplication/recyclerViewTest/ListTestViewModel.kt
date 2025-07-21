package com.gta.myapplication.recyclerViewTest


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Collections

class ListTestViewModel : ViewModel() {

    //不把数据源给adapter了，因为用了ListAdapter，用submitList
    private val _items = MutableLiveData<List<Item>>(emptyList())
    //专供observe
    val items: LiveData<List<Item>> = _items

    private var nextId = 1
    init {
        // 初始化一些假数据
        addAllItems()
    }
    /**
     * 处理Item被拖拽移动的逻辑
     * 这个方法要放在数据源所在地，通过回调设置给别人
     */
    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        // 1. 获取当前的列表
        val currentList = _items.value?.toMutableList() ?: return

        // 2. 在新的可变列表中交换元素位置
        Collections.swap(currentList, fromPosition, toPosition)

        // 3. 将更新后的列表提交给LiveData，触发UI更新
        _items.value = currentList
    }

    /**
     * 处理Item被滑动删除的逻辑
     */
    fun onItemDeleted(position: Int) {
        //修改数据源
        val currentList = _items.value?.toMutableList() ?: return
        currentList.removeAt(position)
        _items.value = currentList
    }

    fun addOneItem() {
        val currentList = _items.value?.toMutableList() ?: mutableListOf()
        val newItem = Item(
            id = nextId++,
            title = "Item #${nextId - 1}",
            description = "Description for item #${nextId - 1}"
        )
        currentList.add(newItem)
        _items.value = currentList
    }

    fun addAllItems() {
        val currentList = _items.value?.toMutableList() ?: mutableListOf()
        val startId = nextId
        repeat(20) {
            val newItem = Item(
                id = nextId++,
                title = "Item #${nextId - 1}",
                description = "Description for item #${nextId - 1}"
            )
            currentList.add(newItem)
        }
        _items.value = currentList
    }

    fun deleteAllItems() {
        _items.value = emptyList()
        // 重置 ID 计数器
        nextId = 1
    }

    // 新增：删除单个项目，这个方法最终设置到布局中的删除按钮
    fun deleteItem(itemId: Int) {
        val currentList = _items.value?.toMutableList() ?: return
        val updatedList = currentList.filter { it.id != itemId }
        _items.value = updatedList
    }
}

