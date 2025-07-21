package com.gta.myapplication.recyclerViewTest


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Collections

class ListTestViewModel : ViewModel() {

    private val _items = MutableLiveData<List<Item>>(emptyList())
    val items: LiveData<List<Item>> = _items

    private var nextId = 1
    init {
        // 初始化一些假数据
        loadItems()
    }

    private fun loadItems() {
        val initialList = (1..20).map { Item(id = it, title = "Item $it", description = "Description $it") }
        _items.value = initialList
    }
    /**
     * 处理Item被拖拽移动的逻辑
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

    // 新增：删除单个项目
    fun deleteItem(itemId: Int) {
        val currentList = _items.value?.toMutableList() ?: return
        val updatedList = currentList.filter { it.id != itemId }
        _items.value = updatedList
    }
}

