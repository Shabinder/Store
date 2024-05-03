package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


interface RetriesManager<K : StoreXPaging.Key> {
    suspend fun resetFor(params: PagingSource.LoadParams<K>)
    suspend fun getCountFor(params: PagingSource.LoadParams<K>): Int
    suspend fun incrementFor(params: PagingSource.LoadParams<K>)
}