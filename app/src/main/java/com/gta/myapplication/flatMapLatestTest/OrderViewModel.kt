package com.gta.myapplication.flatMapLatestTest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class OrderViewModel : ViewModel() {
    // 模拟用户流（每1秒切换一次）
    private val users = flow {
        listOf("用户A", "用户B", "用户C").forEach { user ->
            emit(user)
            delay(1000)
        }
    }

    // 模拟订单处理
    // 修改为Flow模拟实时数据
    private val ordersFlow = flow {
        listOf("订单1", "订单2", "订单3").forEach { order ->
            emit(order)
            delay(500) // 每个订单间隔500ms
        }
    }

    private suspend fun processOrder(user: String, order: String): String {
        delay(500) // 模拟处理耗时
        return "$user → $order"
    }

    // 错误方式：嵌套collect
    fun getOrdersNested(): Flow<String> =
        flow {
            users.collect { user ->
                ordersFlow.collect { order ->
                    emit(processOrder(user, order))
                }
            }
        }

    // 正确方式：flatMapLatest
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOrdersFlatMap(): Flow<String> =
        users.flatMapLatest { user ->
            ordersFlow
                .take(3)
                .map { order ->
                    processOrder(user, order)
                }
        }
}