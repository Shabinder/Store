package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.flow.*
import org.mobilenativefoundation.storex.paging.Identifiable
import org.mobilenativefoundation.storex.paging.MutablePagingBuffer
import org.mobilenativefoundation.storex.paging.PagingStateManager
import org.mobilenativefoundation.storex.paging.StoreX


class RealPagingStateManager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    initialState: StoreX.Paging.State<Id, K, V, E>,
    private val mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E>,
) : PagingStateManager<Id, K, V, E> {
    private val _state = MutableStateFlow(initialState)
    override suspend fun update(nextState: StoreX.Paging.State<Id, K, V, E>) {
        _state.value = nextState
    }

    override suspend fun update(reducer: (StoreX.Paging.State<Id, K, V, E>) -> StoreX.Paging.State<Id, K, V, E>) {
        val nextState = reducer(_state.value)
        update(nextState)
    }

    override suspend fun mutate(mutator: (MutablePagingBuffer<Id, K, V, E>) -> Unit) {
        mutator(mutablePagingBuffer)
    }

    override fun stateFlow(): StateFlow<StoreX.Paging.State<Id, K, V, E>> {
        return _state.asStateFlow()
    }

    override suspend fun getState(): StoreX.Paging.State<Id, K, V, E> {
        return _state.value
    }

    override fun flow(): Flow<StoreX.Paging.State<Id, K, V, E>> = _state.asSharedFlow()
}