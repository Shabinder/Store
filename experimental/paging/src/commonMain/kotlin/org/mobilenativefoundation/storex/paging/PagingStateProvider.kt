package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PagingStateProvider<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    fun stateFlow(): StateFlow<StoreX.Paging.State<Id, K, V, E>>
    suspend fun getState(): StoreX.Paging.State<Id, K, V, E>

    fun flow(): Flow<StoreX.Paging.State<Id, K, V, E>>
}