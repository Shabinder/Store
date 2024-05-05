package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.CoroutineDispatcher
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.storex.paging.impl.StorePagingSource
import org.mobilenativefoundation.storex.paging.impl.StorePagingSourceStreamProvider


@OptIn(ExperimentalStoreApi::class)
class PagingSourceBuilder<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val throwableConverter: (Throwable) -> E,
    private val messageConverter: (String) -> E,
) {
    private var pageStore: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>? = null
    private var mutablePageStore: MutableStore<K, PagingSource.LoadResult.Data<Id, K, V, E>>? = null
    private var itemStore: Store<Id, V>? = null
    private var mutableItemStore: MutableStore<Id, V>? = null

    private var onEachPagingSourceLoadResult: ((key: K, PagingSource.LoadResult<Id, K, V, E>) -> Unit)? = null
    private var onEachItemStoreResponse: ((id: Id, StoreReadResponse<V>) -> Unit)? = null

    fun itemStore(
        store: Store<Id, V>
    ) = apply { this.itemStore = store }

    fun mutableItemStore(
        store: MutableStore<Id, V>
    ) = apply { this.mutableItemStore = store }

    fun pageStore(
        store: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>
    ) = apply { this.pageStore = store }

    fun mutablePageStore(
        store: MutableStore<K, PagingSource.LoadResult.Data<Id, K, V, E>>
    ) = apply { this.mutablePageStore = store }

    fun onEachPagingSourceLoadResult(handler: (key: K, PagingSource.LoadResult<Id, K, V, E>) -> Unit) = apply {
        this.onEachPagingSourceLoadResult = handler
    }

    fun onEachItemStoreResponse(handler: (id: Id, StoreReadResponse<V>) -> Unit) = apply {
        this.onEachItemStoreResponse = handler
    }

    internal fun build(): PagingSource<Id, K, V, E> {
        fun createPageStream(params: PagingSource.LoadParams<K>) =
            mutablePageStore?.paged(params, throwableConverter, messageConverter) ?: pageStore?.paged(
                params,
                throwableConverter,
                messageConverter
            ) ?: error("A page Store is required")

        val createItemStream =
            mutableItemStore?.let { mutableItemStore ->
                { id: Id -> mutableItemStore.stream<Any>(StoreReadRequest.cached(id, false)) }
            } ?: itemStore?.let { itemStore ->
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