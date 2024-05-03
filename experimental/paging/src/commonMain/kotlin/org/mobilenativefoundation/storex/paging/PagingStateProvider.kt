package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PagingStateProvider<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    fun stateFlow(): StateFlow<StoreXPaging.State<Id, K, V, E>>
    suspend fun getState(): StoreXPaging.State<Id, K, V, E>

    fun flow(): Flow<StoreXPaging.State<Id, K, V, E>>
}