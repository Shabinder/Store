package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FetchingState<Id: Comparable<Id>>(
    val anchorPosition: Id? = null,
    val prefetchPosition: Id? = null,
)

interface FetchingStateProvider <Id: Comparable<Id>>{
    fun stateFlow(): StateFlow<FetchingState<Id>>
    suspend fun getState(): FetchingState<Id>
}

interface FetchingStateManager<Id: Comparable<Id>>: FetchingStateProvider<Id> {
    suspend fun update(reducer: (FetchingState<Id>) -> FetchingState<Id>)
}

class RealFetchingStateManager <Id: Comparable<Id>>: FetchingStateManager<Id> {
    private val _state = MutableStateFlow<FetchingState<Id>>(FetchingState())

    override suspend fun update(reducer: (FetchingState<Id>) -> FetchingState<Id>) {
        _state.value  = reducer(_state.value)
    }

    override fun stateFlow(): StateFlow<FetchingState<Id>> {
        return _state.asStateFlow()
    }

    override suspend fun getState(): FetchingState<Id> {
        return _state.value
    }

}