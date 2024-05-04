package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mobilenativefoundation.storex.paging.*


class RealPager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    dispatcher: CoroutineDispatcher,
    pagingStateProvider: PagingStateProvider<Id, K, V, E>,
    private val loader: Loader<Id, K, V, E>,
    private val launchEffects: List<LaunchEffect>,
    private val aggregatingStrategy: AggregatingStrategy<Id, K, V, E>,
    private val fetchingStateManager: FetchingStateManager<Id>
) : Pager<Id, K, V, E> {

    private val coroutineScope = CoroutineScope(dispatcher)

    init {
        coroutineScope.launch {
            launchEffects.forEach { it() }
        }
    }

    override operator fun invoke(anchorPosition: Flow<Id>) {
        coroutineScope.launch {
            anchorPosition.collectLatest { anchorPosition ->
                fetchingStateManager.update { it.copy(anchorPosition = anchorPosition) }
                loader(anchorPosition)
            }
        }
    }

    override val state: StateFlow<StoreX.Paging.State<Id, K, V, E>> = pagingStateProvider.stateFlow()

    override val pagingItems: Flow<StoreX.Paging.Items<Id, V>> = state.map { aggregatingStrategy.aggregate(it) }

    override val flow: Flow<StoreX.Paging.State<Id, K, V, E>> = pagingStateProvider.stateFlow()
}