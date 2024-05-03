package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.flow.*
import org.mobilenativefoundation.storex.paging.*



class RealPagingStateManager<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error>(
    initialState: StoreXPaging.State<Id, K, V, E>,
    private val mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E>,
) : PagingStateManager<Id, K, V, E> {
    private val _state = MutableStateFlow(initialState)
    override suspend fun update(nextState: StoreXPaging.State<Id, K, V, E>) {
        _state.value = nextState
    }

    override suspend fun update(reducer: (StoreXPaging.State<Id, K, V, E>) -> StoreXPaging.State<Id, K, V, E>) {
        val nextState = reducer(_state.value)
        update(nextState)
    }

    override suspend fun mutate(mutator: (MutablePagingBuffer<Id, K, V, E>) -> Unit) {
        mutator(mutablePagingBuffer)
    }

    override fun stateFlow(): StateFlow<StoreXPaging.State<Id, K, V, E>> {
        return _state.asStateFlow()
    }

    override suspend fun getState(): StoreXPaging.State<Id, K, V, E> {
        return _state.value
    }

    override fun flow(): Flow<StoreXPaging.State<Id, K, V, E>> = _state.asSharedFlow()
}