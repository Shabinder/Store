package org.mobilenativefoundation.storex.paging

interface PagingSource<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    fun load(params: LoadParams<K>, onStateTransition: OnStateTransition<Id, K, V, E>)

    data class LoadParams<K : Any>(
        val key: K,
        val strategy: Strategy
    ) {
        sealed interface Strategy {
            data class CacheFirst(val refresh: Boolean) : Strategy
            data object SkipCache : Strategy
        }
    }

    sealed interface LoadResult<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>, out E : Any> {

        data class Error<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>, out E : Any>(
            val value: E,
        ) : LoadResult<Id, K, V, E>

        data class Data<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>, out E : Any>(
            val items: List<StoreX.Paging.Data.Item<Id, V>>,
            val key: K,
            val nextOffset: Id?,
            val itemsBefore: Int?,
            val itemsAfter: Int?,
            val origin: StoreX.Paging.DataSource,
            val extras: Map<String, Any> = mapOf()
        ) : LoadResult<Id, K, V, E>

        data class Loading<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>, out E : Any>(
            val extras: Map<String, Any> = mapOf()
        ) : LoadResult<Id, K, V, E>
    }

    data class OnStateTransition<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
        val onLoading: suspend () -> Unit,
        val onError: suspend (LoadResult.Error<Id, K, V, E>) -> Unit,
        val onData: suspend (LoadResult.Data<Id, K, V, E>) -> Unit
    )
}