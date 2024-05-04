package org.mobilenativefoundation.market

import org.mobilenativefoundation.storex.paging.Identifiable

interface StatefulMarket<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any, S : StatefulMarket.State<Id, K, V, E>> :
    Market<S> {

    interface State<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> : Market.State {
        val subStates: Map<String, SubState<Id, K, V, E>>
    }

    sealed interface SubState<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>

    data class NormalizedState<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
        val allIds: List<Id>,
        val byId: Map<Id, ItemState<Id, K, V, E>>
    ) : SubState<Id, K, V, E>


    sealed interface ItemState<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
        data object Initial : ItemState<Nothing, Nothing, Nothing, Nothing>
        data object Loading : ItemState<Nothing, Nothing, Nothing, Nothing>
        data class Error<E : Any>(val error: E) : ItemState<Nothing, Nothing, Nothing, E>
        data class Data<Id : Comparable<Id>, V : Identifiable<Id>>(
            val value: V,
            val status: Status,
            val lastModified: Long,
            val lastRefreshed: Long,
        ) : SubState<Id, Nothing, V, Nothing> {
            enum class Status {
                Idle,
                Downloading,
                Uploading,
            }
        }
    }


    sealed interface PagingState<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> : SubState<Id, K, V, E> {
        val anchorPosition: Id?
        val prefetchPosition: Id?

        data class Initial<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, K, V, E>

        data class Loading<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, K, V, E>

        data class Error<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
            val value: E,
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, K, V, E>

        data class Data<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
            val allIds: List<Id>,
            override val anchorPosition: Id?,
            override val prefetchPosition: Id?,
            val lastModified: Long,
            val lastRefreshed: Long,
            val status: Status
        ) : PagingState<Id, K, V, E> {
            enum class Status {
                Idle,
                LoadingMore,
                ErrorLoadingMore,
                Refreshing
            }
        }
    }
}