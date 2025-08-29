package com.gta.myapplication.pagingTest

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class UserRepository {

    fun getUsersFlow(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,           // 每页加载20条数据
                enablePlaceholders = false, // 不显示占位符
                prefetchDistance = 5,    // 提前5个位置开始预加载
                initialLoadSize = 20     // 初始加载大小
            ),
            pagingSourceFactory = { UserPagingSource() }
        ).flow
    }
}