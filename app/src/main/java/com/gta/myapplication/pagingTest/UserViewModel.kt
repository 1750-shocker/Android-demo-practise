package com.gta.myapplication.pagingTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    val users: Flow<PagingData<User>> = repository
        .getUsersFlow()
        .cachedIn(viewModelScope) // 缓存数据，避免配置更改时重新加载
}