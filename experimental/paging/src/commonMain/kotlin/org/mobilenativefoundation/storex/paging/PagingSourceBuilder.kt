package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.CoroutineDispatcher
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.storex.paging.impl.StorePagingSource
import org.mobilenativefoundation.storex.paging.impl.StorePagingSourceStreamProvider

class PagingSourceBuilder<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val pageStore: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>,
    private val throwableConverter: (Throwable) -> E,
    private val messageConverter: (String) -> E,
) {
    private var itemStore: Store<Id, V>? = null

    private var onEachPagingSourceLoadResult: ((key: K, PagingSource.LoadResult<Id, K, V, E>) -> Unit)? = null
    private var onEachItemStoreResponse: ((id: Id, StoreReadResponse<V>) -> Unit)? = null

    fun itemStore(
        itemStore: Store<Id, V>
    ) = apply { this.itemStore = itemStore }

    fun onEachPagingSourceLoadResult(handler: (key: K, PagingSource.LoadResult<Id, K, V, E>) -> Unit) = apply {
        this.onEachPagingSourceLoadResult = handler
    }

    fun onEachItemStoreResponse(handler: (id: Id, StoreReadResponse<V>) -> Unit) = apply {
        this.onEachItemStoreResponse = handler
    }

    internal fun build(): PagingSource<Id, K, V, E> {
        fun createPageStream(params: PagingSource.LoadParams<K>) =
            pageStore.paged(params, throwableConverter, messageConverter)

        val createItemStream = itemStore?.let { itemStore ->
            { id: Id -> itemStore.stream(StoreReadRequest.cached(id, false)) }
        }

        val streamProvider = StorePagingSourceStreamProvider(
            createPageStream = ::createPageStream,
            createItemStream = createItemStream,
            onEachPagingSourceLoadResult,
            onEachItemStoreResponse
        )

        return StorePagingSource(
            dispatcher = dispatcher,
            streamProvider = streamProvider
        )
    }
}