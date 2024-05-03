package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mobilenativefoundation.storex.paging.*

class RealQueueManager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    dispatcher: CoroutineDispatcher,
    private val pagingConfig: PagingConfig,
    private val fetchingStrategy: FetchingStrategy<Id, K, V, E>,
    private val mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E>,
    private val pagingSourceController: PagingSourceController<K>,
    private val pagingStateProvider: PagingStateProvider<Id, K, V, E>,
    private val placeholderFactory: PlaceholderFactory<Id, V>,
) : QueueManager<K> {
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher)


    private val queue: ArrayDeque<PagingSource.LoadParams<K>> = ArrayDeque()
    override fun enqueue(params: PagingSource.LoadParams<K>) {
        queue.addLast(params)

        coroutineScope.launch {
            processQueue()
        }
    }

    private suspend fun processQueue() {
        while (queue.isNotEmpty()) {
            val nextPagingParams = queue.removeFirst()
            if (fetchingStrategy.shouldFetch(
                    nextPagingParams,
                    pagingStateProvider.getState(),
                    pagingConfig,
                )
            ) {

                // Add placeholders to paging buffer
                mutablePagingBuffer.put(
                    nextPagingParams,
                    createPlaceholders(pagingConfig.pageSize, nextPagingParams.key)
                )

                pagingSourceController.lazyLoad(nextPagingParams)
            }
        }
    }

    private fun createPlaceholders(count: Int, key: K) = PagingSource.LoadResult.Data<Id, K, V, E>(
        items = List(count) { placeholderFactory() },
        key = key,
        nextKey = null,
        itemsBefore = null,
        itemsAfter = null,
        origin = StoreX.Paging.DataSource.PLACEHOLDER,
    )
}