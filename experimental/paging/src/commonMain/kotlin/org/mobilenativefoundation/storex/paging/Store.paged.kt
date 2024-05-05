package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse


@Suppress("UNCHECKED_CAST")
private fun <Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> page(
    params: PagingSource.LoadParams<K>,
    throwableConverter: (Throwable) -> E,
    messageConverter: (String) -> E,
    streamer: (request: StoreReadRequest<K>) -> Flow<StoreReadResponse<PagingSource.LoadResult.Data<Id, K, V, E>>>
): Flow<PagingSource.LoadResult<Id, K, V, E>> {
    val readRequest = when (params.strategy) {
        is PagingSource.LoadParams.Strategy.CacheFirst -> StoreReadRequest.cached(
            params.key,
            params.strategy.refresh
        )

        PagingSource.LoadParams.Strategy.SkipCache -> StoreReadRequest.fresh(params.key)
    }

    return streamer(readRequest).mapNotNull { response ->
        when (response) {
            is StoreReadResponse.Data -> response.value

            is StoreReadResponse.Error.Exception -> {
                PagingSource.LoadResult.Error(
                    throwableConverter(response.error),
                )
            }

            is StoreReadResponse.Error.Message -> {
                PagingSource.LoadResult.Error(
                    messageConverter(response.message)
                )
            }

            is StoreReadResponse.Loading -> PagingSource.LoadResult.Loading()
            is StoreReadResponse.NoNewData -> null
            is StoreReadResponse.Error.Custom<*> -> {
                PagingSource.LoadResult.Error(response.error as E)
            }

            StoreReadResponse.Initial -> PagingSource.LoadResult.Loading()
        }
    }
}


fun <Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>.paged(
    params: PagingSource.LoadParams<K>,
    throwableConverter: (Throwable) -> E,
    messageConverter: (String) -> E,
): Flow<PagingSource.LoadResult<Id, K, V, E>> {
    return page(params, throwableConverter, messageConverter, ::stream)
}

@ExperimentalStoreApi
fun <Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> MutableStore<K, PagingSource.LoadResult.Data<Id, K, V, E>>.paged(
    params: PagingSource.LoadParams<K>,
    throwableConverter: (Throwable) -> E,
    messageConverter: (String) -> E,
): Flow<PagingSource.LoadResult<Id, K, V, E>> {
    return page(params, throwableConverter, messageConverter) {
        this.stream<Any>(it)
    }
}