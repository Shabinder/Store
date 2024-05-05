package org.mobilenativefoundation.market

import org.mobilenativefoundation.storex.paging.Identifiable

interface StatefulMarket<S : StatefulMarket.State> : Market<S> {

    interface State : Market.State {
        val subStates: Map<String, SubState>
    }

    sealed interface SubState

    data class NormalizedState<Id : Comparable<Id>, V : Identifiable<Id>, E : Any>(
        val allIds: List<Id> = emptyList(),
        val byId: Map<Id, ItemState<Id, V, E>> = emptyMap()
    ) : SubState


    sealed interface ItemState<Id : Comparable<Id>, out V : Identifiable<Id>,out  E : Any> {
        data class Initial<Id: Comparable<Id>, V: Identifiable<Id>, E : Any>(val id: Id) : ItemState<Id, V, E>
        data class Loading<Id: Comparable<Id>, V: Identifiable<Id>, E : Any>(val id: Id) : ItemState<Id, V, E>
        data class Error<Id: Comparable<Id>, V: Identifiable<Id>, E : Any>(val error: E) : ItemState<Id, V, E>
        data class Data<Id : Comparable<Id>, V : Identifiable<Id>>(
            val value: V,
            val status: Status,
            val lastModified: Long,
            val lastRefreshed: Long,
        ) : SubState {
            enum class Status {
                Idle,
                Downloading,
                Uploading,
            }
        }
    }


    sealed interface PagingState<Id : Comparable<Id>, V : Identifiable<Id>, E : Any> : SubState {
        val anchorPosition: Id?
        val prefetchPosition: Id?

        data class Initial<Id : Comparable<Id>, V : Identifiable<Id>, E : Any>(
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, V, E>

        data class Loading<Id : Comparable<Id>, V : Identifiable<Id>, E : Any>(
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, V, E>

        data class Error<Id : Comparable<Id>, V : Identifiable<Id>, E : Any>(
            val value: E,
            override val prefetchPosition: Id?,
            override val anchorPosition: Id?,
        ) : PagingState<Id, V, E>

        data class Data<Id : Comparable<Id>, V : Identifiable<Id>, E : Any>(
            val allIds: List<Id>,
            override val anchorPosition: Id?,
            override val prefetchPosition: Id?,
            val lastModified: Long,
            val lastRefreshed: Long,
            val status: Status<E>
        ) : PagingState<Id, V, E> {

            sealed interface Status<out E : Any> {
                data object Idle : Status<Nothing>
                data class ErrorLoadingMore<E : Any>(val error: E) : Status<E>
                data object LoadingMore : Status<Nothing>
                data object Refreshing : Status<Nothing>
            }

        }
    }
}