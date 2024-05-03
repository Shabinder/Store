package org.mobilenativefoundation.storex.paging

interface PagingStateManager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> :
    PagingStateProvider<Id, K, V, E> {
    suspend fun update(nextState: StoreX.Paging.State<Id, K, V, E>)
    suspend fun update(reducer: (StoreX.Paging.State<Id, K, V, E>) -> StoreX.Paging.State<Id, K, V, E>)
    suspend fun mutate(mutator: (MutablePagingBuffer<Id, K, V, E>) -> Unit)
}