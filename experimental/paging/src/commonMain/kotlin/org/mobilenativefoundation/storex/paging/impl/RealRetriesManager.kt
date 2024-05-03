package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RealRetriesManager<K : StoreXPaging.Key> : RetriesManager<K> {
    private val retryCounts = mutableMapOf<PagingSource.LoadParams<K>, Int>()
    private val mutex = Mutex()
    override suspend fun resetFor(params: PagingSource.LoadParams<K>) {
        mutex.withLock { retryCounts[params] = 0 }
    }

    override suspend fun getCountFor(params: PagingSource.LoadParams<K>): Int {
        return mutex.withLock { retryCounts[params] ?: 0 }
    }

    override suspend fun incrementFor(params: PagingSource.LoadParams<K>) {
        mutex.withLock {
            val prevCount = retryCounts[params] ?: 0
            val nextCount = prevCount + 1
            retryCounts[params] = nextCount
        }
    }
}