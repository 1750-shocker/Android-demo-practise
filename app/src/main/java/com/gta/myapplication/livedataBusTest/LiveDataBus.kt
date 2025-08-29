package com.gta.myapplication.livedataBusTest

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

// LiveDataBus.kt
object LiveDataBus {
    private val eventMap = mutableMapOf<String, MutableLiveData<Event<*>>>()
    private val stickyEventMap = mutableMapOf<String, MutableLiveData<Event<*>>>()

    // 获取普通事件通道（默认不粘性）
    fun <T> with(eventKey: String): MutableLiveData<Event<T>> {
        return eventMap.getOrPut(eventKey) {
            MutableLiveData()
        } as MutableLiveData<Event<T>>
    }

    // 获取粘性事件通道
    fun <T> withSticky(eventKey: String): MutableLiveData<Event<T>> {
        return stickyEventMap.getOrPut(eventKey) {
            MutableLiveData()
        } as MutableLiveData<Event<T>>
    }

    // 发送普通事件
    fun <T> post(eventKey: String, value: T) {
        with<T>(eventKey).postValue(Event(value))
    }

    // 发送粘性事件
    fun <T> postSticky(eventKey: String, value: T) {
        withSticky<T>(eventKey).postValue(Event(value))
    }

    // 清理资源
    fun removeEvent(eventKey: String) {
        eventMap.remove(eventKey)
        stickyEventMap.remove(eventKey)
    }
}

// 观察普通事件（自动防粘性）
inline fun <reified T> LiveDataBus.observeEvent(
    owner: LifecycleOwner,
    key: String,
    crossinline onEvent: (T) -> Unit
) {
    with<T>(key).observe(owner) { event ->
        event?.getContentIfNotHandled()?.let { data ->
            onEvent(data)
        }
    }
}

// 观察粘性事件（手动处理粘性）
inline fun <reified T> LiveDataBus.observeStickyEvent(
    owner: LifecycleOwner,
    key: String,
    crossinline onEvent: (T) -> Unit
) {
    withSticky<T>(key).observe(owner) { event ->
        event?.peekContent()?.let { data ->
            onEvent(data)
        }
    }
}