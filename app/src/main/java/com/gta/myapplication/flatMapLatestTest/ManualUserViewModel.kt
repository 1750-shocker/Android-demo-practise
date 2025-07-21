package com.gta.myapplication.flatMapLatestTest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.delay

class ManualUserViewModel : ViewModel() {
    // 用户控制通道
    private val _userActions = MutableSharedFlow<String>()

    // 订单流（持续生成）
    private val ordersFlow = flow {
        var orderNum = 1
        while (true) {
            emit("订单$orderNum")
            orderNum++
            delay(500) // 每500ms生成一个新订单
        }
    }

    // 处理订单（模拟耗时）
    private suspend fun processOrder(user: String, order: String): String {
        delay(300) // 模拟处理耗时
        return "$user → $order"
    }

    // 对外暴露的订单流
    @OptIn(ExperimentalCoroutinesApi::class)
    val orders: Flow<String> = _userActions
        .distinctUntilChanged() // 避免重复用户
        .flatMapLatest { user ->
            ordersFlow
                .take(3) // 每个用户最多处理3个订单
                .map { order -> processOrder(user, order) }
                .onCompletion { println("$user 的订单流已取消") }
        }

    // 手动切换用户
    suspend fun switchUser(user: String) {
        _userActions.emit(user)
    }
}