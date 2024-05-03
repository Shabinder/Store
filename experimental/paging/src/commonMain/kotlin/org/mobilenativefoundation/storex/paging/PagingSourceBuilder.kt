package org.mobilenativefoundation.storex.paging

import org.mobilenativefoundation.storex.paging.impl.StorePagingSource
import org.mobilenativefoundation.storex.paging.impl.StorePagingSourceStreamProvider
import kotlinx.coroutines.CoroutineDispatcher
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

class PagingSourceBuilder<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val pageStore: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>,
    private val throwableConverter: (Throwable) -> E,
    private val messageConverter: (String) -> E
) {
    private var itemStore: Store<Id, V>? = null

    fun itemStore(
        itemStore: Store<Id, V>
    ) = apply { this.itemStore = itemStore }

    internal fun build(): PagingSource<Id, K, V, E> {
        fun createPageStream(params: PagingSource.LoadParams<K>) =
            pageStore.paged(params, throwableConverter, messageConverter)

        val createItemStream = itemStore?.let { itemStore ->
            { id: Id -> itemStore.stream(StoreReadRequest.cached(id, false)) }
        }

        val streamProvider = StorePagingSourceStreamProvider(
            createPageStream = ::createPageStream,
            createItemStream = createItemStream,
        )

        return StorePagingSource(
            dispatcher = dispatcher,
            streamProvider = streamProvider
        )
    }
}