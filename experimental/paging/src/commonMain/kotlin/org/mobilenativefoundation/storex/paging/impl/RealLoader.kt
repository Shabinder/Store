package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


class RealLoader<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val middleware: List<Middleware<K>>,
    private val queueManager: QueueManager<K>,
    private val strategy: PagingSource.LoadParams.Strategy,
    private val keyFactory: KeyFactory<Id, K>,
) : Loader<Id, K, V, E> {

    override suspend operator fun invoke(offset: Id) {
        val params = PagingSource.LoadParams(
            key = keyFactory.create(offset),
            strategy = strategy
        )
        enqueue(applyMiddleware(params, 0))
    }

    private suspend fun applyMiddleware(
        params: PagingSource.LoadParams<K>,
        index: Int
    ): PagingSource.LoadParams<K> {
        return if (index < middleware.size) {
            middleware[index].apply(params) { nextParams ->
                applyMiddleware(nextParams, index + 1)
            }
        } else {
            params
        }
    }

    private fun enqueue(params: PagingSource.LoadParams<K>) {
        queueManager.enqueue(params)
    }
}