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
    private val fetchingStateProvider: FetchingStateProvider<Id>,
) : QueueManager<K> {
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher)

    // TODO: This won't work if anchor passes prefetch
    private val queue: ArrayDeque<PagingSource.LoadParams<K>> = ArrayDeque()
    // TODO: What about refresh, need to reset this

    private val processedParams: MutableSet<PagingSource.LoadParams<K>> = mutableSetOf()

    override fun enqueue(params: PagingSource.LoadParams<K>) {
        queue.addLast(params)

        coroutineScope.launch {
            processQueue()
        }
    }

    private suspend fun processQueue() {
        while (queue.isNotEmpty()) {
            val nextPagingParams = queue.removeFirst()

            if (nextPagingParams in processedParams) return

            if (fetchingStrategy.shouldFetch(
                    nextPagingParams,
                    fetchingStateProvider.getState(),
                    pagingStateProvider.getState(),
                    pagingConfig,
                )
            ) {

                println("SHOULD FETCH")

                // Add placeholders to paging buffer
                mutablePagingBuffer.put(
                    nextPagingParams,
                    createPlaceholders(pagingConfig.pageSize, nextPagingParams.key)
                )

                pagingSourceController.lazyLoad(nextPagingParams)
                println("LAZY LOADING")

                processedParams.add(nextPagingParams)
            }
        }
    }

    private fun createPlaceholders(count: Int, key: K) = PagingSource.LoadResult.Data<Id, K, V, E>(
        items = List(count) { placeholderFactory() },
        key = key,
        nextOffset = null,
        itemsBefore = null,
        itemsAfter = null,
        origin = StoreX.Paging.DataSource.PLACEHOLDER,
    )
}