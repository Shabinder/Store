package org.mobilenativefoundation.storex.paging

interface PagingStateManager<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> :
    PagingStateProvider<Id, K, V, E> {
    suspend fun update(nextState: StoreXPaging.State<Id, K, V, E>)
    suspend fun update(reducer: (StoreXPaging.State<Id, K, V, E>) -> StoreXPaging.State<Id, K, V, E>)
    suspend fun mutate(mutator: (MutablePagingBuffer<Id, K, V, E>) -> Unit)
}