package com.gta.myapplication.pagingTest

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class UserPagingSource : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize

            // 模拟网络延迟
            delay(1000)

            // 模拟数据加载
            val users = generateUsers(currentPage, pageSize)

            LoadResult.Page(
                data = users,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (users.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    // 模拟生成用户数据
    private fun generateUsers(page: Int, size: Int): List<User> {
        val startIndex = (page - 1) * size
        return (startIndex until startIndex + size).map { index ->
            User(
                id = index + 1,
                name = "用户 ${index + 1}",
                email = "user${index + 1}@example.com"
            )
        }
    }
}