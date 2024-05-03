package org.mobilenativefoundation.storex.paging

interface PagingSource<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    fun load(params: LoadParams<K>, onStateTransition: OnStateTransition<Id, K, V, E>)

    data class LoadParams<K : StoreXPaging.Key>(
        val key: K,
        val strategy: Strategy
    ) {
        sealed interface Strategy {
            data class CacheFirst(val refresh: Boolean) : Strategy
            data object SkipCache : Strategy
        }
    }

    sealed interface LoadResult<Id : Comparable<Id>, out K : StoreXPaging.Key, out V : Identifiable<Id>, out E : StoreXPaging.Error> {

        data class Error<Id : Comparable<Id>, out K : StoreXPaging.Key, out V : Identifiable<Id>, out E : StoreXPaging.Error>(
            val value: E,
        ) : LoadResult<Id, K, V, E>

        data class Data<Id : Comparable<Id>, out K : StoreXPaging.Key, out V : Identifiable<Id>, out E : StoreXPaging.Error>(
            val items: List<StoreXPaging.Data.Item<Id, V>>,
            val key: K,
            val nextKey: K?,
            val itemsBefore: Int?,
            val itemsAfter: Int?,
            val origin: StoreXPaging.DataSource,
            val extras: Map<String, Any> = mapOf()
        ) : LoadResult<Id, K, V, E>

        data class Loading<Id : Comparable<Id>, out K : StoreXPaging.Key, out V : Identifiable<Id>, out E : StoreXPaging.Error>(
            val extras: Map<String, Any> = mapOf()
        ) : LoadResult<Id, K, V, E>
    }

    data class OnStateTransition<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error>(
        val onLoading: suspend () -> Unit,
        val onError: suspend (LoadResult.Error<Id, K, V, E>) -> Unit,
        val onData: suspend (LoadResult.Data<Id, K, V, E>) -> Unit
    )
}